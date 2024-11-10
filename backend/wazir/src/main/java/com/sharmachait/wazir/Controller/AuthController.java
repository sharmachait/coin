package com.sharmachait.wazir.Controller;

import com.sharmachait.wazir.Config.Jwt.JwtConstants;
import com.sharmachait.wazir.Config.Jwt.JwtProvider;
import com.sharmachait.wazir.Model.Dto.LoginDto;
import com.sharmachait.wazir.Model.Dto.UserDto;
import com.sharmachait.wazir.Model.Entity.*;
import com.sharmachait.wazir.Model.Response.AuthResponse;
import com.sharmachait.wazir.Repository.IForgotPasswordTokenRepository;
import com.sharmachait.wazir.Repository.IUserRepository;
import com.sharmachait.wazir.Service.EmailService;
import com.sharmachait.wazir.Service.ForgotPasswordTokenService.ForgotPasswordTokenService;
import com.sharmachait.wazir.Service.TwoFactorOtpService.TwoFactorOtpService;
import com.sharmachait.wazir.Service.UserService.IUserService;
import com.sharmachait.wazir.Service.VerificationCodeService.IVerificationCodeService;
import com.sharmachait.wazir.Utils.OtpUtils;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@Data
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private IForgotPasswordTokenRepository forgotPasswordTokenRepository;
    @Autowired
    private ForgotPasswordTokenService forgotPasswordService;
    @Autowired
    private TwoFactorOtpService twoFactorOtpService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IUserService userService;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private IVerificationCodeService verificationCodeService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody WazirUser wazirUser){

        WazirUser alreadyExists = userRepository.findByEmail(wazirUser.getEmail());
        if(alreadyExists != null) {
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setMessage("Email already exists");
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(errorResponse);
        }

        WazirUser newWazirUser = new WazirUser();
        newWazirUser.setEmail(wazirUser.getEmail());
        newWazirUser.setPassword(encoder.encode(wazirUser.getPassword()));
        newWazirUser.setFullName(wazirUser.getFullName());



        String auths = newWazirUser.getRole().toString()+",";
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(auths);
        Authentication auth = new UsernamePasswordAuthenticationToken(wazirUser.getEmail(), null, authorities);

        String jwt;
        try{
            jwt = JwtProvider.generateToken(auth);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null,false,"Unauthorized",false,null));
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        authResponse.setMessage("User registered successfully");
        WazirUser savedWazirUser = userRepository.save(newWazirUser);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto user) throws Exception {
        WazirUser wazirUser = userRepository.findByEmail(user.getEmail());
        if(wazirUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null,false,"Unauthorized",false,null));
        }
        Authentication auth;
        try{
            auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(),
                            user.getPassword())
            );
//            auth = authenticate(username, password);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null,false,"Unauthorized",false,null));
        }

        String jwt;
        try{
            jwt = JwtProvider.generateToken(auth);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null,false,"Unauthorized",false,null));
        }

        if(wazirUser.getTwoFactorAuth().isEnabled()){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two-factor authentication required");
            authResponse.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOtp();
            try{
                TwoFactorOtp old = twoFactorOtpService.findByUserId(wazirUser.getId());
                twoFactorOtpService.deleteTwoFactorOtp(old);
            } catch (NoSuchElementException e) {
                System.out.println(e.getMessage()+", Creating new otp");
            }
            TwoFactorOtp newTwoFactorOtp = twoFactorOtpService.createTwoFactorOtp(wazirUser,otp,jwt);
            emailService.sendVerificationOtpEmail(wazirUser.getEmail(),otp);
            authResponse.setSession(newTwoFactorOtp.getId().toString());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(authResponse);
        }
        else{
            SecurityContextHolder.getContext().setAuthentication(auth);
            AuthResponse authResponse = new AuthResponse();
            authResponse.setJwt(jwt);
            authResponse.setStatus(true);
            authResponse.setMessage("Logged in successfully");
//          authResponse.setSession();
//          authResponse.setTwoFactorAuthEnabled();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(authResponse);
        }
    }
    @PostMapping("/twoFactorAuth/{otp}")
    public ResponseEntity<AuthResponse> verifyTwoFactorOtp(
            @PathVariable String otp
            , @RequestParam Long id
    )  {
        TwoFactorOtp twoFactorOtp = twoFactorOtpService.findById(id);
        if(twoFactorOtp == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null,false,"Unauthorized",false,null));
        }
        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOtp, otp)) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two-factor authentication verified");
            authResponse.setTwoFactorAuthEnabled(true);
            authResponse.setStatus(true);
            authResponse.setSession(id.toString());
            authResponse.setJwt(twoFactorOtp.getJwt());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(authResponse);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null,false,"Unauthorized",false,null));
        }
    }


    @PostMapping("/forgotpassword/requestchange")
    public ResponseEntity<String> requestResetPassword(
            @RequestBody String email
    ){
        try{
            WazirUser user = userService.findUserByEmail(email);
            String otp = OtpUtils.generateOtp();
            String sendTo = user.getEmail();

            if(user.getTwoFactorAuth().getSendTo().equals(VERIFICATION_TYPE.MOBILE))
                sendTo = user.getMobile();

            ForgotPasswordToken oldToken = forgotPasswordService.findByUserId(user.getId());
            if(oldToken!=null) {
                forgotPasswordService.deleteToken(oldToken);
            }
            ForgotPasswordToken token = forgotPasswordService.createToken(
                    user,
                    otp,
                    user.getTwoFactorAuth().getSendTo(),
                    sendTo);

            if(user.getTwoFactorAuth().getSendTo().equals(VERIFICATION_TYPE.EMAIL)) {
                emailService.sendVerificationOtpEmail(sendTo,token.getCode());
            }
            return ResponseEntity.ok("Otp sent successfully!");
        } catch (Exception e) {
            //handle
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bad request");
        }
    }

    @PatchMapping("/forgotpassword/verify")
    public ResponseEntity<String> verifyResetPassword(
            @RequestBody String email,
            @RequestBody String otp
    ){
        try{
            WazirUser user = userService.findUserByEmail(email);
            ForgotPasswordToken oldToken = forgotPasswordService.findByUserId(user.getId());
            if(oldToken==null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Bad request");
            }

            boolean isVerified = forgotPasswordService.verifyForgotPasswordToken(oldToken,otp);

            if(isVerified) {
                oldToken.setVerified(true);
                forgotPasswordTokenRepository.save(oldToken);
                return ResponseEntity.ok("Otp verified successfully!");
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Bad request");
            }
        } catch (Exception e) {
            //handle
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bad request");
        }
    }

    @PatchMapping("/forgotpassword/reset")
    public ResponseEntity<String> resetPassword(
            @RequestBody String email,
            @RequestBody String newPassword
    ){
        try{
            WazirUser user = userService.findUserByEmail(email);
            ForgotPasswordToken oldToken = forgotPasswordService.findByUserId(user.getId());
            if(oldToken==null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Bad request");
            }

            boolean isVerified = oldToken.isVerified();

            if(isVerified) {
                userService.updatePassword(user.getEmail(),newPassword);
                forgotPasswordService.deleteToken(oldToken);
                return ResponseEntity.ok("Otp verified successfully! Password updated!");
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Bad request");
            }
        } catch (Exception e) {
            //handle
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bad request");
        }
    }
}

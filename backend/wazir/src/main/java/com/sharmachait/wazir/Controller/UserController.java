package com.sharmachait.wazir.Controller;

import com.sharmachait.wazir.Config.Jwt.JwtConstants;
import com.sharmachait.wazir.Config.Jwt.JwtProvider;
import com.sharmachait.wazir.Model.Dto.UserDto;
import com.sharmachait.wazir.Model.Entity.VERIFICATION_TYPE;
import com.sharmachait.wazir.Model.Entity.VerificationCode;
import com.sharmachait.wazir.Model.Entity.WazirUser;
import com.sharmachait.wazir.Service.EmailService;
import com.sharmachait.wazir.Service.UserService.IUserService;
import com.sharmachait.wazir.Service.UserService.UserService;
import com.sharmachait.wazir.Service.VerificationCodeService.IVerificationCodeService;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Data
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IVerificationCodeService verificationCodeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private final ModelMapper modelMapper;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getUserProfile(@RequestHeader(JwtConstants.JWT_HEADER) String jwtHeader, Authentication auth){
        try{
            WazirUser user =userService.findUserByJwt(jwtHeader);
            UserDto userDto = modelMapper.map(user, UserDto.class);
            System.out.println(auth.getName());
            System.out.println(auth.getAuthorities());
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            //handle
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bad request");
        }
    }
    @PostMapping("/request/twofactorauth/{verificationType}")
    public ResponseEntity<String> requestTwoFactorAuth(
            @RequestHeader(JwtConstants.JWT_HEADER) String jwtHeader,
            @PathVariable VERIFICATION_TYPE verificationType){
        try{
            WazirUser user =userService.findUserByJwt(jwtHeader);

            VerificationCode verificationCode = verificationCodeService
                    .getVerificationCodeByUserId(user.getId());
            if(verificationCode!=null){
                verificationCodeService.deleteVerificationCodeById(verificationCode.getId());
            }

            try{
                VerificationCode code = verificationCodeService.sendVerificationCode(user, verificationType);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Otp email not sent please try again");
            }
            return ResponseEntity.ok("Otp sent successfully!");
        } catch (Exception e) {
            //handle
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bad request");
        }
    }

    @PatchMapping("/enable/twofactorauth/{otp}")
    public ResponseEntity<?> enableTwoFactorAuth(
            @RequestHeader(JwtConstants.JWT_HEADER) String jwtHeader,
            @PathVariable String otp){
        try{
            String email = JwtProvider.getEmailFromToken(jwtHeader);
            WazirUser user = userService.findUserByJwt(jwtHeader);
            VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user.getId());
            String sendTo;
            if(verificationCode.getVerificationType().equals(VERIFICATION_TYPE.EMAIL)){
                sendTo = verificationCode.getEmail();
            }else{
                sendTo = verificationCode.getMobile();
            }
            boolean isVerified = verificationCodeService.verifyVerificationCode(verificationCode, otp);
            if(isVerified){

                WazirUser updatedUser = userService.enableTwoFactorAuthentication(
                        email,
                        verificationCode.getVerificationType(),
                        sendTo);
                verificationCodeService.deleteVerificationCodeById(verificationCode.getId());
            }
            UserDto userDto = modelMapper.map(user, UserDto.class);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            //handle
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bad request");
        }
    }


}

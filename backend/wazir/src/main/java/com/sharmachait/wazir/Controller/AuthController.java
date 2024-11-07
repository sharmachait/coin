package com.sharmachait.wazir.Controller;

import com.sharmachait.wazir.Config.Jwt.JwtProvider;
import com.sharmachait.wazir.Model.Dto.LoginDto;
import com.sharmachait.wazir.Model.Entity.WazirUser;
import com.sharmachait.wazir.Model.Response.AuthResponse;
import com.sharmachait.wazir.Repository.IUserRepository;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PasswordEncoder encoder;
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

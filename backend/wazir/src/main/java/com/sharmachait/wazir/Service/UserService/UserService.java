package com.sharmachait.wazir.Service.UserService;

import com.sharmachait.wazir.Config.Jwt.JwtProvider;
import com.sharmachait.wazir.Model.Entity.TwoFactorAuth;
import com.sharmachait.wazir.Model.Entity.VERIFICATION_TYPE;
import com.sharmachait.wazir.Model.Entity.WazirUser;
import com.sharmachait.wazir.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public WazirUser findUserByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        WazirUser user = userRepository.findByEmail(email);
        if(user==null)
            throw new Exception("Invalid JWT token, wrong email claim");
        return user;
    }

    @Override
    public WazirUser findUserByEmail(String email) throws Exception {
        WazirUser user = userRepository.findByEmail(email);
        if(user==null)
            throw new Exception("Invalid JWT token, wrong email claim");
        return user;
    }

    @Override
    public WazirUser findUserById(Long id) throws NoSuchElementException {
        Optional<WazirUser> user = userRepository.findById(id);
        return user.get();
    }

    @Override
    public WazirUser enableTwoFactorAuthentication(String email, VERIFICATION_TYPE verificationType, String sendTo) throws Exception {
        WazirUser user = findUserByEmail(email);
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }

    @Override
    public WazirUser updatePassword(String email, String newPassword) throws Exception {
        WazirUser user = findUserByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
}

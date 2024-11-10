package com.sharmachait.wazir.Service.ForgotPasswordTokenService;

import com.sharmachait.wazir.Model.Entity.ForgotPasswordToken;
import com.sharmachait.wazir.Model.Entity.VERIFICATION_TYPE;
import com.sharmachait.wazir.Model.Entity.WazirUser;
import com.sharmachait.wazir.Repository.IForgotPasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ForgotPasswordTokenService implements IForgotPasswordTokenService {
    @Autowired
    private IForgotPasswordTokenRepository forgotPasswordTokenRepository;

    @Override
    public ForgotPasswordToken createToken(WazirUser user,
                                           String code,
                                           VERIFICATION_TYPE verificationType,
                                           String sendTo) {
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setUser(user);
        token.setSendTo(sendTo);
        token.setVerificationType(verificationType);
        token.setCode(code);
        return forgotPasswordTokenRepository.save(token);
    }

    @Override
    public ForgotPasswordToken findById(Long id) throws NoSuchElementException {
        Optional<ForgotPasswordToken> optional = forgotPasswordTokenRepository.findById(id);
        return optional.get();
    }

    @Override
    public ForgotPasswordToken findByUserId(Long userId) {
        return forgotPasswordTokenRepository.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPasswordToken token) {
        forgotPasswordTokenRepository.delete(token);
    }

    @Override
    public boolean verifyForgotPasswordToken(ForgotPasswordToken forgotPasswordToken, String otp) {
        return forgotPasswordToken.getCode().equals(otp);
    }
}

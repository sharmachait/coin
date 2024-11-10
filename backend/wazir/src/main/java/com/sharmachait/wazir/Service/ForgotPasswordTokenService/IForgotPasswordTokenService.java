package com.sharmachait.wazir.Service.ForgotPasswordTokenService;

import com.sharmachait.wazir.Model.Entity.ForgotPasswordToken;
import com.sharmachait.wazir.Model.Entity.VERIFICATION_TYPE;
import com.sharmachait.wazir.Model.Entity.WazirUser;

import java.util.NoSuchElementException;

public interface IForgotPasswordTokenService {
    ForgotPasswordToken createToken(WazirUser user,
                                    String code,
                                    VERIFICATION_TYPE verificationType,
                                    String sendTo);
    ForgotPasswordToken findById(Long id) throws NoSuchElementException;
    ForgotPasswordToken findByUserId(Long userId);
    void deleteToken(ForgotPasswordToken token);
    boolean verifyForgotPasswordToken(ForgotPasswordToken forgotPasswordToken, String otp);
}

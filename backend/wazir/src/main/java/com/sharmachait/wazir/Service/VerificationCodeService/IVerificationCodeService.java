package com.sharmachait.wazir.Service.VerificationCodeService;

import com.sharmachait.wazir.Model.Entity.VERIFICATION_TYPE;
import com.sharmachait.wazir.Model.Entity.VerificationCode;
import com.sharmachait.wazir.Model.Entity.WazirUser;
import jakarta.mail.MessagingException;

import java.util.NoSuchElementException;

public interface IVerificationCodeService {
    VerificationCode sendVerificationCode(WazirUser user, VERIFICATION_TYPE verificationType) throws MessagingException;
    VerificationCode getVerificationCodeById(Long id) throws NoSuchElementException;
    VerificationCode getVerificationCodeByUserId(Long userId);
    void deleteVerificationCodeById(Long id);
    boolean verifyVerificationCode(VerificationCode verificationCode, String otp);
}

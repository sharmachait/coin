package com.sharmachait.wazir.Service.VerificationCodeService;

import com.sharmachait.wazir.Model.Entity.VERIFICATION_TYPE;
import com.sharmachait.wazir.Model.Entity.VerificationCode;
import com.sharmachait.wazir.Model.Entity.WazirUser;
import com.sharmachait.wazir.Repository.IVerificationCodeRepository;
import com.sharmachait.wazir.Service.EmailService;
import com.sharmachait.wazir.Utils.OtpUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class VerificationCodeService implements IVerificationCodeService {
    @Autowired
    private IVerificationCodeRepository verificationCodeRepository;
    @Autowired
    private EmailService emailService;
    @Override
    public VerificationCode sendVerificationCode(WazirUser user,
                                                 VERIFICATION_TYPE verificationType) throws MessagingException {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setUser(user);
        verificationCode.setVerificationType(verificationType);
        verificationCode.setCode(OtpUtils.generateOtp());
        if(verificationCode.getVerificationType().equals(VERIFICATION_TYPE.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(),verificationCode.getCode());
        }
        return verificationCodeRepository.save(verificationCode);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws NoSuchElementException {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(id);
        return verificationCode.get();
    }

    @Override
    public VerificationCode getVerificationCodeByUserId(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(Long id) {
        verificationCodeRepository.deleteById(id);
    }

    @Override
    public boolean verifyVerificationCode(VerificationCode verificationCode, String otp) {
        return verificationCode.getCode().equals(otp);
    }
}

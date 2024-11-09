package com.sharmachait.wazir.Service.TwoFactorOtpService;

import com.sharmachait.wazir.Model.Entity.TwoFactorOtp;
import com.sharmachait.wazir.Model.Entity.WazirUser;
import com.sharmachait.wazir.Repository.ITwoFactorOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TwoFactorOtpService implements ITwoFactorOtpService {

    @Autowired
    private ITwoFactorOtpRepository twoFactorOtpRepository;

    @Override
    public TwoFactorOtp createTwoFactorOtp(WazirUser user, String otp, String jwt) {
        TwoFactorOtp twoFactorOtp = new TwoFactorOtp();
        twoFactorOtp.setOtp(otp);
        twoFactorOtp.setJwt(jwt);
//        twoFactorOtp.setId(id);
        twoFactorOtp.setUser(user);
        return twoFactorOtpRepository.save(twoFactorOtp);
    }

    @Override
    public TwoFactorOtp findByUserId(Long userId) throws NoSuchElementException {
        return twoFactorOtpRepository.findByUserId(userId).get();
    }

    @Override
    public TwoFactorOtp findById(Long id) throws NoSuchElementException {
        Optional<TwoFactorOtp> otp = twoFactorOtpRepository.findById(id);
        return otp.get();
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp) {
        return twoFactorOtp.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp) {
        twoFactorOtpRepository.delete(twoFactorOtp);
    }
}

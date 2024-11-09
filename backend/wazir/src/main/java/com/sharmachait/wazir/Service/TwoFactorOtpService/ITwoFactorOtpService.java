package com.sharmachait.wazir.Service.TwoFactorOtpService;
import com.sharmachait.wazir.Model.Entity.WazirUser;
import com.sharmachait.wazir.Model.Entity.TwoFactorOtp;
public interface ITwoFactorOtpService {
    TwoFactorOtp createTwoFactorOtp(WazirUser user, String otp, String jwt);
    TwoFactorOtp findByUserId(Long userId);
    TwoFactorOtp findById(Long id);
    boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp);
    void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);
}

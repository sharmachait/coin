package com.sharmachait.wazir.Service.UserService;

import com.sharmachait.wazir.Model.Entity.VERIFICATION_TYPE;
import com.sharmachait.wazir.Model.Entity.WazirUser;
import java.util.NoSuchElementException;

public interface IUserService {
    public WazirUser findUserByJwt(String jwt) throws Exception;
    public WazirUser findUserByEmail(String email) throws Exception;
    public WazirUser findUserById(Long id) throws NoSuchElementException;
    public WazirUser enableTwoFactorAuthentication(
            String email,
            VERIFICATION_TYPE verificationType,
            String sendTo
    ) throws Exception;
    public WazirUser updatePassword(String email, String newPassword) throws Exception;
}

package com.sharmachait.wazir.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sharmachait.wazir.Model.Entity.ForgotPasswordToken;

@Repository
public interface IForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Long> {
    ForgotPasswordToken findByUserId(Long userId);
}

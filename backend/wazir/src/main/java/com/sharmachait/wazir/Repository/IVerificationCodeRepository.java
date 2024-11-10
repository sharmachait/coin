package com.sharmachait.wazir.Repository;

import com.sharmachait.wazir.Model.Entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    public VerificationCode findByUserId(Long userId);
}

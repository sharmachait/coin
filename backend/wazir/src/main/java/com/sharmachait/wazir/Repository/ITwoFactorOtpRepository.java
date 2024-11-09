package com.sharmachait.wazir.Repository;

import com.sharmachait.wazir.Model.Entity.TwoFactorOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITwoFactorOtpRepository extends JpaRepository<TwoFactorOtp, Long> {
    Optional<TwoFactorOtp> findByUserId(Long userId);
}

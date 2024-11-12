package com.sharmachait.wazir.Repository;

import com.sharmachait.wazir.Model.Entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByUserId(Long userId);
}

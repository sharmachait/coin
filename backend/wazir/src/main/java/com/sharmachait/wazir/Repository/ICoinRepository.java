package com.sharmachait.wazir.Repository;

import com.sharmachait.wazir.Model.Entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICoinRepository extends JpaRepository<Coin, String> {
}

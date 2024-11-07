package com.sharmachait.wazir.Repository;

import com.sharmachait.wazir.Model.Entity.WazirUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<WazirUser, Long> {
    WazirUser findByEmail(String email);
}

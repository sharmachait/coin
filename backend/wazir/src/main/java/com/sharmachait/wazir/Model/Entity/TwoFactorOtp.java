package com.sharmachait.wazir.Model.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TwoFactorOtp {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String otp;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private WazirUser user;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String jwt;
}

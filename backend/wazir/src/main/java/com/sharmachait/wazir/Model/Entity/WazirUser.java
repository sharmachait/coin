package com.sharmachait.wazir.Model.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class WazirUser {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String fullName;
    @Column(unique = true)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)// to make JPA ignore it when reading from the database
    private String password;
    private String mobile;
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;
    @Embedded// to indicate to JPA that the properties of the class TwoFactorAuthService should be considered part of this table
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    @OneToOne(mappedBy = "user")
    private TwoFactorOtp twoFactorOtp;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY)
    private Set<Order> orders;
}

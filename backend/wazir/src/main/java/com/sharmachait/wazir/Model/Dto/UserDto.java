package com.sharmachait.wazir.Model.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sharmachait.wazir.Model.Entity.TwoFactorAuth;
import com.sharmachait.wazir.Model.Entity.USER_ROLE;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String fullname;
    private String email;
    private String mobile;
    private USER_ROLE role;
    private TwoFactorAuth twoFactorAuth;
}
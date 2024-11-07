package com.sharmachait.wazir.Model.Entity;

import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VERIFICATION_TYPE sendTo;
}
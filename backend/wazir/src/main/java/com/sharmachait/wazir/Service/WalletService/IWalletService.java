package com.sharmachait.wazir.Service.WalletService;

import com.sharmachait.wazir.Exceptions.InsuffecientFundsException;
import com.sharmachait.wazir.Model.Entity.Order;
import com.sharmachait.wazir.Model.Entity.Wallet;
import com.sharmachait.wazir.Model.Entity.WazirUser;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

public interface IWalletService {
    Wallet getUserWallet(WazirUser user);
    Wallet addBalance(Wallet wallet, BigDecimal money);
    Wallet findWalletById(Long id) throws NoSuchElementException;
    void walletToWalletTransfer(WazirUser sender, Wallet receiverWallet, BigDecimal amount) throws InsuffecientFundsException;
    void parOrderAmount(Order order, WazirUser user) throws Exception;
}

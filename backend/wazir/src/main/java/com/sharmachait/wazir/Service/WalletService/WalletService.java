package com.sharmachait.wazir.Service.WalletService;

import com.sharmachait.wazir.Exceptions.InsuffecientFundsException;
import com.sharmachait.wazir.Model.Entity.Order;
import com.sharmachait.wazir.Model.Entity.Wallet;
import com.sharmachait.wazir.Model.Entity.WazirUser;
import com.sharmachait.wazir.Repository.IWalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class WalletService implements IWalletService {

    @Autowired
    private IWalletRepository walletRepository;

    @Override
    public Wallet getUserWallet(WazirUser user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if(wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            wallet = walletRepository.save(wallet);
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, BigDecimal money) {
        BigDecimal balance = wallet.getBalance();
        balance = balance.add(money);
        wallet.setBalance(balance);
        walletRepository.save(wallet);
        return wallet;
    }

    @Override
    public Wallet findWalletById(Long id) throws NoSuchElementException {
        Optional<Wallet> wallet = walletRepository.findById(id);
        return wallet.get();
    }

    @Transactional
    @Override
    public void walletToWalletTransfer(WazirUser sender, Wallet receiverWallet, BigDecimal amount) throws InsuffecientFundsException {
        Wallet senderWallet = getUserWallet(sender);
        BigDecimal balance = senderWallet.getBalance();
        if(balance.compareTo(amount) >= 0) {
            senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
            receiverWallet.setBalance(receiverWallet.getBalance().add(amount));
            walletRepository.save(senderWallet);
            walletRepository.save(receiverWallet);
            return ;
        }
        throw new InsuffecientFundsException("Insufficient funds to transfer from " + sender + " to " + receiverWallet);
    }

    @Override
    public void parOrderAmount(Order order, WazirUser user) {
        return ;
    }
}

package org.example.wallet.service;

import org.example.wallet.exception.InsufficientFundsException;
import org.example.wallet.exception.WalletNotFoundException;
import org.example.wallet.model.Wallet;
import org.example.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@Transactional
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    public Wallet getWallet(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + walletId));
    }

    public void updateBalance(UUID walletId, BigDecimal amount, String operationType) {
        Wallet wallet = getWallet(walletId);

        synchronized (wallet) {
            if ("WITHDRAWAL".equals(operationType) && wallet.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds in wallet: " + walletId);
            }

            BigDecimal newBalance = "DEPOSIT".equals(operationType)
                    ? wallet.getBalance().add(amount)
                    : wallet.getBalance().subtract(amount);

            wallet.setBalance(newBalance);
            walletRepository.save(wallet);
        }
    }
}

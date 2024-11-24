package org.example.wallet.controller;

import org.example.wallet.service.WalletService;
import org.example.wallet.model.Wallet;
import org.example.wallet.dto.WalletOperationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable("walletId") String walletId) {
            UUID uuid = UUID.fromString(walletId);  // Преобразование в UUID
            Wallet wallet = walletService.getWallet(uuid);
            return ResponseEntity.ok(wallet);

    }

    @PostMapping
    public ResponseEntity<Void> updateBalance(@RequestBody @Valid WalletOperationRequest request) {
        walletService.updateBalance(request.getWalletId(), request.getAmount(), request.getOperationType());
        return ResponseEntity.ok().build();
    }
}

package com.wallet.controller;

import com.wallet.dto.WalletRequestDto;
import com.wallet.dto.WalletResponseDto;
import com.wallet.entity.Wallet;
import com.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(final WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@Valid @RequestBody WalletRequestDto walletRequestDto) {
        walletService.updateBalance(walletRequestDto);
    }

    @GetMapping(value = "/{walletId}")
    public WalletResponseDto getWallet(@PathVariable("walletId") UUID walletId) {
        Wallet wallet = walletService.getWallet(walletId);
        return new WalletResponseDto(wallet.getId(), wallet.getBalance());
    }
}

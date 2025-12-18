package com.wallet.service;

import com.wallet.dto.WalletRequestDto;
import com.wallet.entity.Wallet;

import java.util.UUID;

public interface WalletService {

    void updateBalance(WalletRequestDto walletRequestDto);

    Wallet getWallet(UUID walletId);
}

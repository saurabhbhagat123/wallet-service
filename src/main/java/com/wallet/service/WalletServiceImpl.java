package com.wallet.service;

import com.wallet.dto.OperationType;
import com.wallet.dto.WalletRequestDto;
import com.wallet.entity.Wallet;
import com.wallet.exception.InsufficientFundsException;
import com.wallet.exception.WalletNotFoundException;
import com.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(final WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    @Override
    public void updateBalance(final WalletRequestDto walletRequestDto) {
        getWallet(walletRequestDto.getWalletId());
        if (OperationType.DEPOSIT == walletRequestDto.getOperationType()) {
            walletRepository.deposit(walletRequestDto.getWalletId(),walletRequestDto.getAmount());
        } else if (OperationType.WITHDRAW == walletRequestDto.getOperationType()) {
            int rowsUpdated = walletRepository.withdraw(walletRequestDto.getWalletId(), walletRequestDto.getAmount());
            if (rowsUpdated == 0) {
                throw new InsufficientFundsException();
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Wallet getWallet(final UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(WalletNotFoundException::new);
    }
}

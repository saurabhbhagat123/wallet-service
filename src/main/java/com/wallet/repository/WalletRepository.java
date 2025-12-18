package com.wallet.repository;

import com.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    @Modifying
    @Query("""
        UPDATE Wallet w
        SET w.balance = w.balance + :amount
        WHERE w.id = :walletId
    """)
    void deposit(UUID walletId, long amount);

    @Modifying
    @Query("""
        UPDATE Wallet w
        SET w.balance = w.balance - :amount
        WHERE w.id = :walletId
          AND w.balance >= :amount
    """)
    int withdraw(UUID walletId, long amount);
}

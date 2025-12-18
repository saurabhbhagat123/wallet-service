package com.wallet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequestDto {

    @NotNull
    private UUID walletId;
    @NotNull
    private OperationType operationType;
    @NotNull
    @Positive
    private long amount;
}

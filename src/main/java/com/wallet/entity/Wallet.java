package com.wallet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "wallet")
@Data
public class Wallet {

    @Id
    private UUID id;

    private long balance;
}

package com.wallet.controller;

import com.wallet.entity.Wallet;
import com.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;

    private UUID walletId;

    @BeforeEach
    void setUp() {
        walletId = UUID.randomUUID();

        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(1000);

        walletRepository.save(wallet);
    }

    @Test
    void testCanDepositSuccessfully() throws Exception {
        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "walletId": "%s",
                  "operationType": "DEPOSIT",
                  "amount": 500
                }
            """.formatted(walletId)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/wallets/{id}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1500));
    }

    @Test
    void testCanWithdrawSuccessfully() throws Exception {
        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "walletId": "%s",
                  "operationType": "WITHDRAW",
                  "amount": 400
                }
            """.formatted(walletId)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/wallets/{id}", walletId))
                .andExpect(jsonPath("$.balance").value(600));
    }

    @Test
    void failTestWhenFundsAreInsufficient() throws Exception {
        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "walletId": "%s",
                  "operationType": "WITHDRAW",
                  "amount": 5000
                }
            """.formatted(walletId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INSUFFICIENT_FUNDS"));
    }

    @Test
    void failTestWhenJsonNotValid() throws Exception {
        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "walletId": "%s",
                  "operationType": "DEPOSIT",
                  "amount":
                }
            """.formatted(walletId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_JSON"));
    }

    @Test
    void failTestWhenAmountIsNegative() throws Exception {
        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "walletId": "%s",
                  "operationType": "DEPOSIT",
                  "amount": -10
                }
            """.formatted(walletId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"));
    }

    @Test
    void testCanGetWalletSuccessfully() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/{id}", walletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    void failTestWhenWalletNotFound() throws Exception {
        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "walletId": "%s",
                  "operationType": "DEPOSIT",
                  "amount": 100
                }
            """.formatted(UUID.randomUUID())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("WALLET_NOT_FOUND"));
    }
}
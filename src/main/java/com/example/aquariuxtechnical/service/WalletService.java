package com.example.aquariuxtechnical.service;

import com.example.aquariuxtechnical.dto.UserWalletDTO;
import com.example.aquariuxtechnical.entity.UserWallet;
import com.example.aquariuxtechnical.repository.UserWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletService {
    @Autowired
    private UserWalletRepository walletRepository;

    public List<UserWalletDTO> getUserWallets(Long userId) {
        List<UserWallet> wallets = walletRepository.findByUserId(userId);

        return wallets.stream()
                      .map(this::mapToWalletDTO)
                      .collect(Collectors.toList());
    }

    private UserWalletDTO mapToWalletDTO(UserWallet wallet) {
        UserWalletDTO dto = new UserWalletDTO();
        dto.setSymbol(wallet.getSymbol());
        dto.setBalance(wallet.getBalance());

        return dto;
    }
}

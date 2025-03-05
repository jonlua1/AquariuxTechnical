package com.example.aquariuxtechnical.repository;

import com.example.aquariuxtechnical.entity.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {
    List<UserWallet> findByUserId(Long userId);
}

package com.example.comlakecrawler.db;

import com.example.comlakecrawler.utils.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserAccount,Long> {
}

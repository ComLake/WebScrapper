package com.example.comlakecrawler.repository;

import com.example.comlakecrawler.utils.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserAccount,Long> {
}

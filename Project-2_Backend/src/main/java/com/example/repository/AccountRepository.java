package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    // PT01 -- D.B. C.R.U.D. for User Registration (POST - localhost:8080/register)
    // PT02 -- D.B. C.R.U.D. for User Login (POST - localhost:8080/login)
    public boolean existsByUsername(String username);
    public List<Account> findByUsername(String username);

}

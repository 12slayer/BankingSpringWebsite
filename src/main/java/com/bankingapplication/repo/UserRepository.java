package com.bankingapplication.repo;

import com.bankingapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

User findByAccountNumber(Long accountNumber);
 @Modifying
 @Transactional
 void deleteByAccountNumber(Long accountNumber);


}


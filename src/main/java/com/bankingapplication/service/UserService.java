package com.bankingapplication.service;

import com.bankingapplication.model.User;
import com.bankingapplication.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User addUser(User user){
        User savedUser=userRepository.save(user);
        return savedUser;
    }
    public List<User> getAllUser(){
        return userRepository.findAll();
    }
    public void deleteUser(int userId){
        userRepository.deleteById(userId);

    }
    public User findUser(int userId){
        return userRepository.getOne(userId);
    }
    public User findUserByAccountNumber(Long accountNumber){
        return userRepository.findByAccountNumber(accountNumber);
    }

        public User deleteUserByAccountNumber(Long accountNumber) {
            userRepository.deleteByAccountNumber(accountNumber);
            return null;

    }
}

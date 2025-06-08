package com.syp.service;

import com.syp.model.User;
import com.syp.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthenticationService {
    private final UserRepository userRepo = new UserRepository();

    public boolean login(String username, String password) {
        Optional<User> opt = userRepo.findByUsername(username);
        if (opt.isEmpty()) {
            return false;
        }
        User u = opt.get();
        return BCrypt.checkpw(password, u.getPasswordHash());
    }
}

package com.cse.api.service;

import java.util.regex.Pattern;

import javax.security.auth.message.AuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cse.api.model.User;
import com.cse.api.repository.UserRepository;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User validateUser(String email, String password) throws AuthException {
        if (email != null)
            email = email.toLowerCase();
        try {
            User userFound = userRepository.findByEmailAddress(email);
            if (!BCrypt.checkpw(password, userFound.getPassword())) {
                throw new AuthException("Invalid email or password");
            }

            // System.out.println("userfound: " + userFound.getFirstName());
            return userFound;

        } catch (Exception e) {
            throw new AuthException("Invalid email or password");
        }
    }

    // public User logsdata(String email) {

    // email = email.toLowerCase();

    // User userdata = userRepository.findByEmailAddress(email);
    // // System.out.print("my desired data are: " + userdata);
    // String em = userdata.getEmail();
    // String ftName = userdata.getFirstName();
    // String lname = userdata.getLastName();
    // int rol = userdata.getRole();
    // System.out.println(em);
    // System.out.println(ftName);
    // System.out.println(lname);
    // System.out.println(rol);
    // return userdata;
    // }

    public User registerUser(User user) throws AuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        String email = user.getEmail();
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashedPassword);
        if (email != null)
            email = email.toLowerCase();
        if (!pattern.matcher(email).matches())
            throw new AuthException("Invalid email format");
        long count = userRepository.getCountByEmail(email);
        if (count > 0)
            throw new AuthException("Email already in use");
        return userRepository.save(user);
    }

}

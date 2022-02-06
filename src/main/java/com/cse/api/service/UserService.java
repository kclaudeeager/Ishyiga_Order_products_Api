package com.cse.api.service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.security.auth.message.AuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cse.api.model.User;
import com.cse.api.repository.UserRepository;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Map<String,Object> validateUser(String email, String password)  {
        Map result=new HashMap<String,Object>();
        try{
        if (email != null)
            email = email.toLowerCase();
            User userFound = userRepository.findByEmailAddress(email);
            if (!BCrypt.checkpw(password, userFound.getPassword())) {

                result.put("error","Invalid email or password");
                return  result;
            }

            // System.out.println("userfound: " + userFound.getFirstName());
            result.put("User",userFound);
            System.out.print("userFound  >>>>>>>> "+userFound);
            return  result;
        }
        catch(Exception exception){
            result.put("error","Invalid email or password");
                return  result;
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

    public Map<String,Object> registerUser(User user) {
        Map result=new HashMap<String,Object>();
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        String email = user.getEmail();
        String company=user.getCompany();
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashedPassword);
        if (email != null)
            email = email.toLowerCase();
        if (!pattern.matcher(email).matches()){
            result.put("error","Invalid email format" );
            return result;
        }
      
    
        long count = userRepository.getCountByEmail(email);
        if (count > 0){
            result.put("error","Email already in use" );
            return result;
        }
        long countCompany = userRepository.getCountByCompany(company);
        if (countCompany > 0){
            result.put("error","Company already in use" );
            return result;
        }
        User user1=userRepository.save(user) ;
        result.put("User", user1);
        return result;
    }

}

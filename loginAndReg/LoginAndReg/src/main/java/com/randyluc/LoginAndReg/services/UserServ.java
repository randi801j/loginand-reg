package com.randyluc.LoginAndReg.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
    
import com.randyluc.LoginAndReg.models.LoginUser;
import com.randyluc.LoginAndReg.models.User;
import com.randyluc.LoginAndReg.repositories.UserRepo;
    
@Service
public class UserServ {
    
    @Autowired
    private UserRepo userRepo;
    
    // This method will be called from the controller
    // whenever a user submits a registration form.
    
    public User register(User newUser, BindingResult result) {
    	
    	Optional<User>potentialUser=userRepo.findByEmail(newUser.getEmail());
    	 // Reject if email is taken (present in database)
    	if(potentialUser.isPresent()) {
    		result.rejectValue("email","Matches", "Email Already Exists");
    	}
        // Reject if password doesn't match confirmation
    	if(!newUser.getPassword().equals(newUser.getConfirm())) {
    	    result.rejectValue("confirm", "Matches", "The Confirm Password must match Password!");
    	}
        // Return null if result has errors
    	if(result.hasErrors()) {
    		return null;
    	}
        // Hash and set password, save user to database
    	String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
    	newUser.setPassword(hashed);
    	return userRepo.save(newUser);
    }

    // This method will be called from the controller
    // whenever a user submits a login form.
        public User login(LoginUser newLoginUser, BindingResult result) {
        // TO-DO - Reject values:
        Optional<User>potentialUser=userRepo.findByEmail(newLoginUser.getEmail());

    	// Find user in the DB by email
        // Reject if NOT present
        if(!potentialUser.isPresent()) {
    		result.rejectValue("email","Matches", "Email Does Not Exists");
    		return null;
    	}
        // Reject if BCrypt password match fails
        User user = potentialUser.get();
        
        if(!BCrypt.checkpw(newLoginUser.getPassword(), user.getPassword())) {
            result.rejectValue("password", "Matches", "Invalid Password!");
        }

        // Return null if result has errors
        if(result.hasErrors()) {
    		return null;
    	}
        // Otherwise, return the user object
        
        return user;	
    }
        
    public User findById(Long id) {
    	Optional <User>potentialUser=userRepo.findById(id);
    	if(potentialUser.isPresent()) {
    		return potentialUser.get();
    	}
    	return null;
    }

}


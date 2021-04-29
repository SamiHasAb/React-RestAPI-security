package org.study.pma.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.study.pma.exceptions.UsernameAlreadyExistsException;
import org.study.pma.models.User;
import org.study.pma.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
    @Autowired
    private UserRepository userRep;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User saveUser(User newUser) {
		 try{
	            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
	            //Username has to be unique (exception)
	            newUser.setUsername(newUser.getUsername());
	            // Make sure that password and confirmPassword match
	            // We don't persist or show the confirmPassword
	            newUser.setConfirmPassword("");
	            return userRep.save(newUser);

	        }catch (Exception e){
	            throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"' already exists");
	        }
	}

 
}

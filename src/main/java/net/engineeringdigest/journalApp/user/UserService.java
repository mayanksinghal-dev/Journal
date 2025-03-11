package net.engineeringdigest.journalApp.user;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.engineeringdigest.journalApp.user.entity.User;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User createNewUser(User user){
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        return userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public Optional<User> findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public void remove(String userName){
        userRepository.deleteByUserName(userName);
    }
}
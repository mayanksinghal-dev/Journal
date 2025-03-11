package net.engineeringdigest.journalApp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import net.engineeringdigest.journalApp.user.entity.User;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        try{
            User user = userRepository.findByUserName(username).get();
            if(user!=null){
                return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
            }
            
            throw new UsernameNotFoundException("User not found with username: "+ username);
        }
        catch(UsernameNotFoundException usrExp){
            usrExp.printStackTrace();
            throw new UsernameNotFoundException("User not found with username: "+ username);
        }
    }
}
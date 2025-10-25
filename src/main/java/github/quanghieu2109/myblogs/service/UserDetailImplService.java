package github.quanghieu2109.myblogs.service;

import github.quanghieu2109.myblogs.entity.User;
import github.quanghieu2109.myblogs.repository.UserRepository;
import github.quanghieu2109.myblogs.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailImplService implements  UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new UserDetailsImpl(user);
    }
}

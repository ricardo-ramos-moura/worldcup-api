package com.worldcup.service;

import com.worldcup.repository.WorldCupUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorldCupUserDetailService implements UserDetailsService {

    private final WorldCupUserRepository worldCupUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(worldCupUserRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("World Cup User not found"));
    }
}

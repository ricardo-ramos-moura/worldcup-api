package com.worldcup.repository;

import com.worldcup.domain.WorldCupUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WorldCupUserRepository extends JpaRepository<WorldCupUser, Long> {
    WorldCupUser findByUsername(String username);


}

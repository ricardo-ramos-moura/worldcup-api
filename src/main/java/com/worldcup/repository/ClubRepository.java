package com.worldcup.repository;

import com.worldcup.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByName(String name);


}

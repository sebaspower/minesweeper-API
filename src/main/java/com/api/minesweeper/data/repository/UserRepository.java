package com.api.minesweeper.data.repository;

import com.api.minesweeper.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByName(String name);
}

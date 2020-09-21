package com.example.minesweeper.data.repository;

import com.example.minesweeper.data.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {
    Game findById(long id);
    Game findByUserId(long id);
}

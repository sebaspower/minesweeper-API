package com.api.minesweeper.data.repository;

import com.api.minesweeper.data.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {
    Game findById(long id);
    Game [] findByUserIdAndFinished(long id, boolean finished);
    Game getFirstByUserIdAndFinishedOrderByStartedDesc(long id, boolean finished);
}

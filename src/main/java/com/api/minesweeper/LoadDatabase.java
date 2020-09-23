package com.api.minesweeper;


import com.api.minesweeper.data.entity.Game;
import com.api.minesweeper.data.entity.User;
import com.api.minesweeper.data.repository.GameRepository;
import com.api.minesweeper.data.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            repository.save(new User("burglar"));
            repository.save(new User("thief"));
            repository.save(new User("sebas"));
            repository.save(new User("juan"));
        };
    }

    @Bean
    CommandLineRunner initDatabase2(GameRepository repository) {
        return args -> {
            repository.save(new Game(1,2,2,1));
            repository.save(new Game(2,2,2,2));
            repository.save(new Game(3,3,2,3));
            repository.save(new Game(4,4,2,4));
            repository.save(new Game(20,20,10,4));
        };
    }
}

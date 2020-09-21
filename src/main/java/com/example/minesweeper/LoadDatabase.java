package com.example.minesweeper;


import com.example.minesweeper.data.entity.User;
import com.example.minesweeper.data.entity.Game;
import com.example.minesweeper.data.repository.UserRepository;
import com.example.minesweeper.data.repository.GameRepository;
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
        };
    }
}

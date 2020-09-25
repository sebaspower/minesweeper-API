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
            //repository.deleteAll();
        };
    }

    @Bean
    CommandLineRunner initDatabase2(GameRepository repository) {
        return args -> {
            //repository.deleteAll();
        };
    }
}

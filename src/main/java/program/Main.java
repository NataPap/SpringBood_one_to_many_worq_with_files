package program;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import program.storage.StorageProperties;
import program.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class) //підключаємо конфігураційний клас
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            //storageService.deleteAll();
            try {
                storageService.init(); //створення папки для збереження файлів
            }
            catch(Exception ex) {
                System.out.println("----propblem cteate folder--------");
            }
        };
    }
}
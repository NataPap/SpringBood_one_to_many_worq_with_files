package program.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

// створюємо клас для конфігурації нашого інтерфейсу StorageService
@ConfigurationProperties("storage")
@Data
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "uploaded";// визначає папку для збереження файлів

}
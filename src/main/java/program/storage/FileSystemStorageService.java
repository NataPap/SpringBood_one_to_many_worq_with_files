package program.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Stream;


// створюємо клас, що реалізовуватиме інтерфейс StorageService для завантаження,
// збереження та забезпечення подальшої роботи з файлами
@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation; // створення статичної змінної для папки, якою оперуватиме FileSystem

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation()); //присвоєння шляху папки через конструктор
    }

    // метод завантаження і збереження файла з використанням інтерфейсу MultipartFile
    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    // метод відображення усіх файлів в папці
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    // метод повернення шляху файла за його ім'ям
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    //метод зберігання файла та повернення його ім'я
    @Override
    public String store(String base64) {
        try {
            if (base64.isEmpty()) { //перевірка чи файл не пустий, якщо пустий - ексепшин
                throw new StorageException("Failed to store empty base64 ");
            }
            UUID uuid = UUID.randomUUID(); //генерування унікального імені файлу
            String randomFileName = uuid.toString()+".jpg"; //переведення в стрінг з доданням розширення
            String [] charArray = base64.split(","); // поділ стрінгового імені файлу на 2 частини
            java.util.Base64.Decoder decoder = Base64.getDecoder(); // збереження в декодер
            byte[] bytes = new byte[0]; // створює байт-масив
            bytes = decoder.decode(charArray[1]); //записує дані другої половини в байт-масив
            String directory= "uploaded/"+randomFileName; //присвоєння шляху збереження файла
            new FileOutputStream(directory).write(bytes); // запис файла байтами
            return randomFileName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file ", e);
        }

    }

    // метод отримання ресурсів запитуваного файлу
    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    // метод видалення всіх файлів
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    // метод ініціалізації (створює папку для берігання файлів)
    @Override
    public void init() {
        try {
            if(!Files.exists(rootLocation)) //перевірка на існування папки
            {
                Files.createDirectory(rootLocation); //якщо не існує, то ми її створюємо
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}

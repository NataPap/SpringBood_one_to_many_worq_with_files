package program.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import program.dto.AnimalAddDto;
import program.dto.AnimalDto;
import program.dto.ImageDto;
import program.dto.ImageUploadDto;
import program.entities.Animal;
import program.entities.ImageEntity;
import program.mapper.ApplicationMapper;
import program.repositories.AnimalRepository;
import program.repositories.ImageRepository;
import program.storage.StorageService;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


@RestController
@RequestMapping
public class AnimalController {

    private final ImageRepository imageRepository;
    private final AnimalRepository animalRepository;
    private final StorageService storageService;
    private final ApplicationMapper applicationMapper;

@Autowired
    public AnimalController(ImageRepository imageRepository, AnimalRepository animalRepository, StorageService storageService, ApplicationMapper applicationMapper) {
        this.imageRepository = imageRepository;
        this.animalRepository = animalRepository;
        this.storageService = storageService;
        this.applicationMapper = applicationMapper;
}


    // завантажити зображення
    @PostMapping("/upload")
    public String upload(@RequestBody ImageUploadDto dto) {
        String imageName = storageService.store(dto.getBase64());
        try {
            ImageEntity image = new ImageEntity(imageName);
            imageRepository.save(image);
        } catch(Exception ex)
        {
            System.out.println("Error "+ ex.getMessage());
        }

        return imageName;
    }

    //отримати зображення по імені файлу
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws Exception {

        Resource file = storageService.loadAsResource(filename);
        String urlFileName =  URLEncoder.encode("сало.jpg", StandardCharsets.UTF_8.toString());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION,"filename=\""+urlFileName+"\"")
                .body(file);
    }

    //додати нову тварину в базу з множиною фото.
    @PostMapping( "/addAnimal")
    public ResponseEntity create(@RequestBody AnimalAddDto add) throws IOException {

        Animal animal =new Animal();
        animal.setName(add.getName());
        animalRepository.save(animal);
        for (String name:add.getImages()) {
            List<ImageEntity> images = imageRepository.findByName(name);
            ImageEntity image = images.get(0);
            image.setAnimal(animal);
            imageRepository.save(image);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }
    @GetMapping("/")
    public List<AnimalDto> list(){
        List<AnimalDto> animalDtos=applicationMapper.ListAnimalByListAnimalDto(animalRepository.findAll());
        for (AnimalDto animalDto:animalDtos) {
            List<ImageDto> fileNames= animalDto.getImages();
            for (ImageDto image:fileNames) {
                try{
                    String fileName = image.getName();
                    String base64 = storageService.loadFile(fileName);
                    image.setName("data:image/jpeg;base64,"+ base64);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        return animalDtos;
    }

}

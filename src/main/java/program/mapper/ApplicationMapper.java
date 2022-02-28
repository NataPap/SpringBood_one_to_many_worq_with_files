package program.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import program.dto.AnimalDto;
import program.entities.Animal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(source = "name", target = "name")
    //AnimalDto AnimalByAnimalDto (Animal animal);
    List<AnimalDto> ListAnimalByListAnimalDto (List<Animal> animals);

//    @Mapping(source = "name", target = "name")
//    ImageDto ImageEntityByImageDto (ImageEntity image);
//    List<ImageDto> ListImageEntityByListImageDto(List<ImageEntity> images);
}

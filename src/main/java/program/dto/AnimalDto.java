package program.dto;

import lombok.Data;
import java.util.List;


@Data
public class AnimalDto {
    private int id;
    private String name;
    private List<ImageDto> images;
}

package program.dto;

import lombok.Data;
import java.util.List;

@Data
public class AnimalAddDto {
    private String name;
    private List<String> images;
}

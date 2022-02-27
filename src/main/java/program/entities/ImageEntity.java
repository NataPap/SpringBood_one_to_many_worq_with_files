package program.entities;

import lombok.*;
import javax.persistence.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_images")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="name",length = 255)
    private String name;
    @ManyToOne
    private Animal animal;

    public ImageEntity(String name){
        this.name = name;
    }
}

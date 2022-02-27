package program.entities;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name", length = 250, nullable = false)
    private String name;
    @OneToMany(mappedBy = "animal",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> Images=new ArrayList<>();
}

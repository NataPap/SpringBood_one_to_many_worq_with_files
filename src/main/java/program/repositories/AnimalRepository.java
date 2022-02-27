package program.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import program.entities.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal,Integer> {
}
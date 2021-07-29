package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {

    Car findCarById(Long id);

    @Query("SELECT c, COUNT(p) FROM Car c,Picture p GROUP BY c ORDER BY COUNT(p) DESC ,c.make")
    List<Car> getCarsOrderByPicturesCountThenByMake();
}

package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Seller;
import softuni.exam.service.SellerService;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    Seller findSellerById(Long id);
}

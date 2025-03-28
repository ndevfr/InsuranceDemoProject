package fr.ndev.insurance.repository;

import fr.ndev.insurance.model.User;
import fr.ndev.insurance.model.Vehicle;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends ListCrudRepository<Vehicle, Long> {

    int countByUser(User user);

    List<Vehicle> findByUser(User user);

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
}
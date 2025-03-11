package fr.ndev.insurance.repository;

import fr.ndev.insurance.model.Vehicle;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends ListCrudRepository<Vehicle, Long> {
}
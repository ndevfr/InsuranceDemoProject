package fr.ndev.insurance.repository;

import fr.ndev.insurance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findById(long id);

    boolean existsByEmail(String email);
}
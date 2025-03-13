package fr.ndev.insurance.repository;

import fr.ndev.insurance.model.InsurancePolicy;
import fr.ndev.insurance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {

    List<InsurancePolicy> findByUser(User user);
}
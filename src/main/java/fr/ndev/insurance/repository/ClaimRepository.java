package fr.ndev.insurance.repository;

import fr.ndev.insurance.model.Claim;
import fr.ndev.insurance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fr.ndev.insurance.model.InsurancePolicy;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    int countByPolicy(InsurancePolicy policy);

    List<Claim> findByPolicy(InsurancePolicy policy);

}
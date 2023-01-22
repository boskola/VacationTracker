package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.model.UsedVacation;

@Repository
public interface UsedVacationRepository extends JpaRepository<UsedVacation, Long>{
	
}

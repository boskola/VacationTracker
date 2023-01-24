package project.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.model.UsedVacation;

@Repository
public interface UsedVacationRepository extends JpaRepository<UsedVacation, Long>{
	Page<UsedVacation> findByVacationStartDateGreaterThanEqualAndVacationEndDateLessThanEqualAndUserId
	(LocalDate vacationStartDate, LocalDate vacationEndDate, Long userId, Pageable pageable);
}

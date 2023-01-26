package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import project.model.Vacation;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long>{
	
	@Query(value="SELECT total_vacation_days FROM vacation WHERE vacation_year = :vacationYear "
			+ "AND user_id = :userId", nativeQuery=true)
	Integer findDaysForVacationYearUserId(@Param("vacationYear") Integer vacationYear, @Param("userId") Long userId);
}

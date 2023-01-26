package project.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import project.model.UsedVacation;

@Repository
public interface UsedVacationRepository extends JpaRepository<UsedVacation, Long>{
	
	Page<UsedVacation> findByVacationStartDateGreaterThanEqualAndVacationEndDateLessThanEqualAndUserId
	(LocalDate vacationStartDate, LocalDate vacationEndDate, Long userId, Pageable pageable);
	
	Page<UsedVacation> findByVacationEndDateLessThanEqualAndUserId
	(LocalDate vacationEndDate, Long userId, Pageable pageable);
	
	Page<UsedVacation> findByVacationStartDateGreaterThanEqualAndUserId
	(LocalDate vacationStartDate, Long userId, Pageable pageable);
	
	Page<UsedVacation> findByUserId (Long userId, Pageable pageable);
		
	@Query(value="SELECT ((SELECT COALESCE(SUM(vacation_end_date + 1 - vacation_start_date),0) FROM used_vacation WHERE user_id = :userId AND DATE_PART('year' ,vacation_end_date) = :year AND DATE_PART('year' ,vacation_start_date) = :year) + \r\n"
			+ "	   (SELECT COALESCE(SUM((SELECT make_date(:year, 12, 31)) + 1 - vacation_start_date),0) FROM used_vacation WHERE user_id = :userId AND DATE_PART('year' ,vacation_end_date) > :year AND DATE_PART('year' ,vacation_start_date) = :year) +\r\n"
			+ "	   (SELECT COALESCE(SUM(vacation_end_date + 1 - (SELECT make_date(:year, 01, 01))),0) FROM used_vacation WHERE user_id = :userId AND DATE_PART('year' ,vacation_end_date) = :year AND DATE_PART('year' ,vacation_start_date) < :year))\r\n"
			+ "", nativeQuery = true)
	Integer search(@Param("year") Integer year, @Param("userId") Long userId);
	
	List<UsedVacation> findByUserId(Long userId);
}

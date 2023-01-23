package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.model.Vacation;

@Repository
public interface VacationDaysRepository extends JpaRepository<Vacation, Long>{

}

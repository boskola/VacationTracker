package project.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import project.model.UsedVacation;

public interface EmployeeService {
	
	UsedVacation save(UsedVacation usedVacation);
	Page<UsedVacation> search(LocalDate vacationStartDate, LocalDate vacationEndDate, Long userId, Integer pageNo);
	Integer searchTotalVacationDays(Integer vacationYear, Long userId);
	Integer searchUsedVacationDays(Integer year, Long userId);
	List<UsedVacation> getUsedVacationDays(Long userId);
}

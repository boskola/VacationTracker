package project.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import project.model.UsedVacation;

public interface EmployeeService {
	
	UsedVacation save(UsedVacation usedVacation);
	Page<UsedVacation> search(LocalDate vacationStartDate, LocalDate vacationEndDate, Long userId, Integer pageNo);
}

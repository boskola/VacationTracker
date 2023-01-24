package project.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import project.model.UsedVacation;
import project.repository.UsedVacationRepository;
import project.service.EmployeeService;

@Service
public class JpaEmployeeService implements EmployeeService {
	
	@Autowired
	private UsedVacationRepository usedVacationRepository;

	@Override
	public UsedVacation save(UsedVacation usedVacation) {
		// TODO Auto-generated method stub
		return usedVacationRepository.save(usedVacation);
	}

	@Override
	public Page<UsedVacation> search(LocalDate vacationStartDate, LocalDate vacationEndDate, Long userId, Integer pageNo) {
		// TODO Auto-generated method stub
		return usedVacationRepository.findByVacationStartDateGreaterThanEqualAndVacationEndDateLessThanEqualAndUserId
				(vacationStartDate, vacationEndDate, userId, PageRequest.of(pageNo,10));
	}

}

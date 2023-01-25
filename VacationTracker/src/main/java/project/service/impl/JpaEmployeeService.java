package project.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import project.model.UsedVacation;
import project.repository.UsedVacationRepository;
import project.repository.VacationRepository;
import project.service.EmployeeService;

@Service
public class JpaEmployeeService implements EmployeeService {
	
	@Autowired
	private UsedVacationRepository usedVacationRepository;
	
	@Autowired
	private VacationRepository vacationRepository;

	@Override
	public UsedVacation save(UsedVacation usedVacation) {
		// TODO Auto-generated method stub
		return usedVacationRepository.save(usedVacation);
	}

	@Override
	public Page<UsedVacation> search(LocalDate vacationStartDate, LocalDate vacationEndDate, Long userId, Integer pageNo) {
		
		if(vacationStartDate==null && vacationEndDate==null) {
			return usedVacationRepository.findByUserId(userId, PageRequest.of(pageNo,10));
		}
		
		if(vacationStartDate==null) {
			return usedVacationRepository.findByVacationEndDateLessThanEqualAndUserId(vacationEndDate, userId, PageRequest.of(pageNo,10));
		}
		
		if(vacationEndDate==null) {
			return usedVacationRepository.findByVacationStartDateGreaterThanEqualAndUserId(vacationStartDate, userId, PageRequest.of(pageNo,10));
		}
		
		return usedVacationRepository.findByVacationStartDateGreaterThanEqualAndVacationEndDateLessThanEqualAndUserId
				(vacationStartDate, vacationEndDate, userId, PageRequest.of(pageNo,10));
	}
	
	@Override
	public Integer search(Integer year, Long userId) {
		// TODO Auto-generated method stub
		Integer result = usedVacationRepository.search(year, userId);
		
		if (result==null) {
			return 0;
		}
		else {
			return result;
		}
	}

	@Override
	public Integer searchVacation(Integer vacationYear, Long userId) {
		// TODO Auto-generated method stub
		Integer result = vacationRepository.findByVacationYearAndUserId(vacationYear, userId);
		
		if (result==null) {
			return 0;
		}
		else {
			return result;
		}
	}
}

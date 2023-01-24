package project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
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

}

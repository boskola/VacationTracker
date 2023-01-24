package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import project.dto.UsedVacationDTO;
import project.message.ResponseMessage;
import project.model.UsedVacation;
import project.model.UserEntity;
import project.repository.UsedVacationRepository;
import project.repository.UserRepository;
import project.util.CSVUtil;

@Controller
@RequestMapping("/api/employee")
public class EmployeeController {
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UsedVacationRepository usedVacationRepository;
	
	@PostMapping("/addUsedVacationDays")
	public ResponseEntity<ResponseMessage> create(@Valid @RequestBody UsedVacationDTO usedVacationDTO){
		String message = "";
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmail).get();
	
			UsedVacation usedVacation = new UsedVacation(CSVUtil.getLocalDate(usedVacationDTO.getVacationStartDate(), DATE_FORMAT), 
					CSVUtil.getLocalDate(usedVacationDTO.getVacationEndDate(), DATE_FORMAT), user);

			usedVacationRepository.save(usedVacation);
			message = "Successfully loaded vacation days for the user: " + user.getUserEmail();
			
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}catch(Exception e) {
			message = "The user is not found!";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
		}
	}
}

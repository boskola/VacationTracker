package project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import project.dto.UsedVacationDTO;
import project.message.ResponseMessage;
import project.model.UsedVacation;
import project.model.UserEntity;
import project.repository.UsedVacationRepository;
import project.repository.UserRepository;
import project.service.EmployeeService;
import project.util.CSVUtil;
import project.util.UsedVacationToUsedVacationDTO;

@Controller
@RequestMapping("/api/employee")
public class EmployeeController {
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UsedVacationRepository usedVacationRepository;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private UsedVacationToUsedVacationDTO toUVDTO;
	
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
	
	@GetMapping("/searchUsedVacationDays")
	public ResponseEntity<List<UsedVacationDTO>> searchUsedVacationDays(
			@RequestParam(required=false) String dateFromParam,
            @RequestParam(required=false) String dateToParam,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo){
		
		Page<UsedVacation> page;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmail).get();
	
			if(dateFromParam != null && dateToParam != null) {
				page = employeeService.search(CSVUtil.getLocalDate(dateFromParam, DATE_FORMAT), CSVUtil.getLocalDate(dateToParam, DATE_FORMAT), user.getId(), pageNo);

				return new ResponseEntity<>(toUVDTO.convert(page.getContent()), HttpStatus.OK);
			}
		}catch(Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/adminSearchUsedVacationDays")
	public ResponseEntity<List<UsedVacationDTO>> adminSearchUsedVacationDays(
			@RequestParam(required=false) String dateFromParam,
            @RequestParam(required=false) String dateToParam,
            @RequestParam(required=false) String userEmailParam,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo){
		
		Page<UsedVacation> page;
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmailParam).get();
	
			if(dateFromParam != null && dateToParam != null) {
				
				page = employeeService.search(CSVUtil.getLocalDate(dateFromParam, DATE_FORMAT), CSVUtil.getLocalDate(dateToParam, DATE_FORMAT), user.getId(), pageNo);
				System.out.println(page);
				return new ResponseEntity<>(toUVDTO.convert(page.getContent()), HttpStatus.OK);
			}
			
			
		}catch(Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}

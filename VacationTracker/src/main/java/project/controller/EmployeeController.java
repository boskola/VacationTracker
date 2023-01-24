package project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	@GetMapping("/search")
	public ResponseEntity<ResponseMessage> searchTotalUsedAvailableVDays(
			@RequestParam(required=true) String daysOption,
			@RequestParam(required=true) String year,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		UserEntity user = userRepository.findByUserEmail(userEmail).get();
		
		Integer yearInt = Integer.parseInt(year);
		
		if(daysOption.equals("total")) {
			Integer totalDays = employeeService.searchVacation(yearInt, user.getId());
			
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(totalDays.toString()));
	
		}else if(daysOption.equals("used")) {
			Integer usedDays = employeeService.search(yearInt, user.getId());
			
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(usedDays.toString()));
			
		}else if(daysOption.equals("available")) {
			Integer totalDays = employeeService.searchVacation(yearInt, user.getId());
			Integer usedDays = employeeService.search(Integer.parseInt(year), user.getId());
			Integer avalableDays = totalDays - usedDays;
			
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(avalableDays.toString()));
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/searchUsedVacationDays")
	public ResponseEntity<List<UsedVacationDTO>> searchUsedVacationDays(
			@RequestParam(required=false) String dateFromParam,
            @RequestParam(required=false) String dateToParam,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo){
		
		Page<UsedVacation> page;

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		UserEntity user = userRepository.findByUserEmail(userEmail).get();

		try {
			page = employeeService.search(CSVUtil.getLocalDate(dateFromParam, DATE_FORMAT), CSVUtil.getLocalDate(dateToParam, DATE_FORMAT), user.getId(), pageNo);
			
			return new ResponseEntity<>(toUVDTO.convert(page.getContent()), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
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
	
	@GetMapping("/adminSearch")
	public ResponseEntity<ResponseMessage> adminSearchTotalUsedAvailableVDays(
			@RequestParam(required=true) String daysOption,
			@RequestParam(required=true) String year,
			@RequestParam(required = true) String userEmail,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo){
		
		UserEntity user = userRepository.findByUserEmail(userEmail).get();
		Integer yearInt = Integer.parseInt(year);
		
		if(daysOption.equals("total")) {
			Integer totalDays = employeeService.searchVacation(yearInt, user.getId());
			
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(totalDays.toString()));
	
		}else if(daysOption.equals("used")) {
			Integer usedDays = employeeService.search(yearInt, user.getId());
			
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(usedDays.toString()));
			
		}else if(daysOption.equals("available")) {
			Integer totalDays = employeeService.searchVacation(yearInt, user.getId());
			Integer usedDays = employeeService.search(yearInt, user.getId());
			Integer avalableDays = totalDays - usedDays;
			
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(avalableDays.toString()));
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/adminSearchUsedVacationDays")
	public ResponseEntity<List<UsedVacationDTO>> adminSearchUsedVacationDays(
			@RequestParam(required=false) String dateFromParam,
            @RequestParam(required=false) String dateToParam,
            @RequestParam(required=true) String userEmailParam,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo){
		
		Page<UsedVacation> page;
		UserEntity user = userRepository.findByUserEmail(userEmailParam).get();
		
		try {
			page = employeeService.search(CSVUtil.getLocalDate(dateFromParam, DATE_FORMAT), CSVUtil.getLocalDate(dateToParam, DATE_FORMAT), user.getId(), pageNo);

			return new ResponseEntity<>(toUVDTO.convert(page.getContent()), HttpStatus.OK);
			
		}catch(Exception e) {
			System.out.println(e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}

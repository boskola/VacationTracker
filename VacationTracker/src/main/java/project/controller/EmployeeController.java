package project.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
			@RequestParam(required=true) String year){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmail).get();
			
			Integer yearInt = Integer.parseInt(year);
			
			if(daysOption.equals("total")) {
				Integer totalDays = employeeService.searchTotalVacationDays(yearInt, user.getId());
				
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(totalDays.toString()));
		
			}else if(daysOption.equals("used")) {
				Integer usedDays = employeeService.searchUsedVacationDays(yearInt, user.getId());
				
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(usedDays.toString()));
				
			}else if(daysOption.equals("available")) {
				Integer totalDays = employeeService.searchTotalVacationDays(yearInt, user.getId());
				Integer usedDays = employeeService.searchUsedVacationDays(Integer.parseInt(year), user.getId());
				Integer availableDays = totalDays - usedDays;
				Integer availableDaysPositive = -availableDays;
				
				if(availableDays<0) {
					
					return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage
						("The user has used more vacation days than intended: " + availableDaysPositive.toString() +
						"! User has no more available vacation days!"));
				}
				
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(availableDays.toString()));
			}
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/adminSearch")
	public ResponseEntity<ResponseMessage> adminSearchTotalUsedAvailableVDays(
			@RequestParam(required=true) String daysOption,
			@RequestParam(required=true) String year,
			@RequestParam(required = true) String userEmail){
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmail).get();
			Integer yearInt = Integer.parseInt(year);
			
			if(daysOption.equals("total")) {
				Integer totalDays = employeeService.searchTotalVacationDays(yearInt, user.getId());
				
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(totalDays.toString()));
		
			}else if(daysOption.equals("used")) {
				Integer usedDays = employeeService.searchUsedVacationDays(yearInt, user.getId());
				
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(usedDays.toString()));
				
			}else if(daysOption.equals("available")) {
				Integer totalDays = employeeService.searchTotalVacationDays(yearInt, user.getId());
				Integer usedDays = employeeService.searchUsedVacationDays(yearInt, user.getId());
				Integer availableDays = totalDays - usedDays;
				Integer availableDaysPositive = -availableDays;
				
				if(availableDays<=0) {
					return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage
						("The user has used more vacation days than intended: " + availableDaysPositive.toString() +
						"! User has no more available vacation days!"));
				}
				
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(availableDays.toString()));
			}
	
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
			page = employeeService.search(CSVUtil.getLocalDate(dateFromParam, DATE_FORMAT), CSVUtil.getLocalDate(dateToParam, DATE_FORMAT), user.getId(), pageNo);
			
			return new ResponseEntity<>(toUVDTO.convert(page.getContent()), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/adminSearchUsedVacationDays")
	public ResponseEntity<List<UsedVacationDTO>> adminSearchUsedVacationDays(
			@RequestParam(required=false) String dateFromParam,
            @RequestParam(required=false) String dateToParam,
            @RequestParam(required=true) String userEmailParam,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo){
		
		Page<UsedVacation> page;
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmailParam).get();
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
		
		LocalDate startDate = CSVUtil.getLocalDate(usedVacationDTO.getVacationStartDate(), DATE_FORMAT);
		LocalDate endDate = CSVUtil.getLocalDate(usedVacationDTO.getVacationEndDate(), DATE_FORMAT);
		
		if(startDate.isAfter(endDate)) {
			message="Wrong dates!";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
		}
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmail).get();
			
			if(!checkInputAdd(startDate, endDate, user)) {
				message="Wrong dates!";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
			}
			
			if(startDate.getYear() == endDate.getYear()){
				Integer year = startDate.getYear();
				
				Integer totalDays = employeeService.searchTotalVacationDays(year, user.getId());
				Integer usedDays = employeeService.searchUsedVacationDays(year, user.getId());
				Integer availableDays = totalDays - usedDays;

				if(availableDays < ChronoUnit.DAYS.between(startDate, endDate)+1) {
					message = "The user has no more available vacation days for this year!";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
				}
			}else if(startDate.getYear() < endDate.getYear()) {
				Integer year1=(startDate.getYear());
				
				Integer totalDays1 = employeeService.searchTotalVacationDays(year1, user.getId());
				Integer usedDays1 = employeeService.searchUsedVacationDays(year1, user.getId());
				Integer availableDays1 = totalDays1 - usedDays1;
				LocalDate lastDay = LocalDate.of(year1, 12, 31);
				
				if(availableDays1 < ChronoUnit.DAYS.between(startDate, lastDay)+1) {
					message = "The user has no more available vacation days for this year!";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
				}
				
				Integer year2=(endDate.getYear());
				
				Integer totalDays2 = employeeService.searchTotalVacationDays(year2, user.getId());
				Integer usedDays2 = employeeService.searchUsedVacationDays(year2, user.getId());
				Integer availableDays2 = totalDays2 - usedDays2;
				LocalDate firstDay = LocalDate.of(year2, 01, 01);
				
				if(availableDays2 < ChronoUnit.DAYS.between(firstDay, lastDay)+1) {
					message = "The user has no more available vacation days for this year!";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
				}
				
			}
	
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
	
	public boolean checkInputAdd(LocalDate fromDate, LocalDate endDate, UserEntity user) {
		try {
			ArrayList<UsedVacation> usedVacationDays = new ArrayList<>();
			usedVacationDays.addAll(employeeService.getUsedVacationDays(user.getId()));
			
			for(UsedVacation usedVacation: usedVacationDays) {
				
				boolean firstCondition = 
					(fromDate.isBefore(usedVacation.getVacationEndDate()) || fromDate.isEqual(usedVacation.getVacationEndDate()));
				
				boolean secondCondition = 
						(endDate.isAfter(usedVacation.getVacationStartDate()) || endDate.isEqual(usedVacation.getVacationStartDate()));
	
				if(firstCondition && secondCondition) {
					return false;
				}
			}
			return true;
			
		}catch (Exception e) {
			return false;
		}
	}
}

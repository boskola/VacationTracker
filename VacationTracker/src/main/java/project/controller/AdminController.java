package project.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import project.dto.InvalidDataDTO;
import project.dto.UsedVacationDTO;
import project.enumeration.Role;
import project.message.ResponseMessage;
import project.model.UsedVacation;
import project.model.UserEntity;
import project.repository.UserRepository;
import project.service.AdminService;
import project.service.EmployeeService;
import project.util.CSVUtil;
import project.util.UsedVacationToUsedVacationDTO;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdminService fileService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private UsedVacationToUsedVacationDTO toUVDTO;
	
	@PostMapping("/importEmployees")
	public ResponseEntity<ResponseMessage> importEmployees(@RequestParam("file") MultipartFile file) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmail).get();
			if (user.getRole()!=Role.ADMIN){
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("You are not allowed to see this page"));
			}
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("User not found!"));
		}
		
		String message = "";

		if (CSVUtil.hasCSVFormat(file)) {
			try {
				fileService.saveEmployees(file, passwordEncoder);
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}
	
	@PostMapping("/importUsedVacation")
	public ResponseEntity<ResponseMessage> importUsedVacation(@RequestParam("file") MultipartFile file) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmail).get();
			if (user.getRole()!=Role.ADMIN){
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("You are not allowed to see this page"));
			}
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("User not found!"));
		}
		
		String message = "";

		if (CSVUtil.hasCSVFormat(file)) {
			try {
				fileService.saveUsedVacation(file);
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}
	
	@PostMapping("/importYearTotalVacationDays")
	public ResponseEntity<ResponseMessage> importYearTotalVacationDays(@RequestParam("file") MultipartFile file) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmail).get();
			if (user.getRole()!=Role.ADMIN){
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("You are not allowed to see this page"));
			}
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("User not found!"));
		}
		
		String message = "";

		if (CSVUtil.hasCSVFormat(file)) {
			try {
				fileService.saveTotalVacationDays(file);
				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}
		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}
	
	@PostMapping("/importTotalVacationDays")
	public ResponseEntity<ResponseMessage> importTotalVacationDays(@RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmail).get();
			if (user.getRole()!=Role.ADMIN){
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("You are not allowed to see this page"));
			}
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("User not found!"));
		}
		
		String message = "";
		
		List<MultipartFile> files = new ArrayList<>();
		
		files.add(file1);
		files.add(file2);
		files.add(file3);
		
		for(MultipartFile file: files) {
			if (CSVUtil.hasCSVFormat(file)) {
				try {
					fileService.saveTotalVacationDays(file);
					message += file.getOriginalFilename() + " ";

				} catch (Exception e) {
					message = "Could not upload the file: " + file.getOriginalFilename() + "!";
					return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
				}
				
			}else {
				message = "Please upload a csv file!";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
			}
		}
		message = "Uploaded files successfully";
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	}
	
	
	
	@GetMapping("/adminSearch")
	public ResponseEntity<ResponseMessage> adminSearchTotalUsedAvailableVDays(
			@RequestParam(required=true) String daysOption,
			@RequestParam(required=true) String year,
			@RequestParam(required = true) String userEmail){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String loggedUserEmail = authentication.getName();
		
		try {
			UserEntity user = userRepository.findByUserEmail(loggedUserEmail).get();
			if (user.getRole()!=Role.ADMIN){
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("You are not allowed to see this page"));
			}
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("User not found!"));
		}
		
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
	
	@GetMapping("/adminSearchUsedVacationDays")
	public ResponseEntity<List<UsedVacationDTO>> adminSearchUsedVacationDays(
			@RequestParam(required=false) String dateFromParam,
            @RequestParam(required=false) String dateToParam,
            @RequestParam(required=true) String userEmailParam,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmail).get();
			if (user.getRole()!=Role.ADMIN){
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Page<UsedVacation> page;
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmailParam).get();
			page = employeeService.search(CSVUtil.getLocalDate(dateFromParam, DATE_FORMAT), CSVUtil.getLocalDate(dateToParam, DATE_FORMAT), user.getId(), pageNo);

			return new ResponseEntity<>(toUVDTO.convert(page.getContent()), HttpStatus.OK);
			
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/checkData")
	public ResponseEntity<List<InvalidDataDTO>> checkData(@RequestParam String yearFrom,
														  @RequestParam String yearTo){
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		
		try {
			UserEntity user = userRepository.findByUserEmail(userEmail).get();
			if (user.getRole()!=Role.ADMIN){
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		List<InvalidDataDTO> searchResult = new ArrayList<>();
		
		try {
			
			List<UserEntity> users = fileService.getAllUsers();
			
			int yearIntFrom = Integer.parseInt(yearFrom); 
			int yearIntTo = Integer.parseInt(yearTo);
			
			if(yearIntFrom > yearIntTo) {
				return new ResponseEntity<>(searchResult, HttpStatus.OK); 
			}
			
			Integer totalDays, usedDays, availableDays;
			
			if(yearIntTo > LocalDate.now().getYear()) {
				yearIntTo = LocalDate.now().getYear();
			}
			
			for(UserEntity user: users) {
				if (user.getId()==1)
					continue;
				for(int i=yearIntFrom; i<=yearIntTo; i++) {
					totalDays = employeeService.searchTotalVacationDays(i, user.getId());
					usedDays = employeeService.searchUsedVacationDays(i, user.getId());
					availableDays = totalDays - usedDays;
					InvalidDataDTO record = new InvalidDataDTO(user.getUserEmail(), availableDays, i);
					if (availableDays<0)
						searchResult.add(record);
				}
			}

			return new ResponseEntity<>(searchResult, HttpStatus.OK);
			
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
package project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import project.message.ResponseMessage;
import project.service.AdminService;
import project.util.CSVUtil;

//@CrossOrigin("http://localhost:5432")
@Controller
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService fileService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/importEmployees")
	public ResponseEntity<ResponseMessage> importEmployees(@RequestParam("file") MultipartFile file) {
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
		String message = "Uploaded files successfully: ";
		
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
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	}

//	@GetMapping("/users")
//	public ResponseEntity<List<UserEntity>> getAllUsers() {
//		try {
//			List<UserEntity> users = fileService.getAllUsers();
//
//			if (users.isEmpty()) {
//				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//			}
//
//			return new ResponseEntity<>(users, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

}

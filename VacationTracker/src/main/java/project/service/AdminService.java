package project.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import project.model.UsedVacation;
import project.model.UserEntity;
import project.repository.UsedVacationRepository;
import project.repository.UserRepository;
import project.util.CSVUtil;

@Service
public class AdminService {
 
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UsedVacationRepository usedVacationRepository;
	
	public void saveEmployees(MultipartFile file, PasswordEncoder passwordEncoder) {
		try {
			CSVUtil csvUtil = new CSVUtil();
			
			List<UserEntity> users = csvUtil.csvToUsers(file.getInputStream(), passwordEncoder);
			userRepository.saveAll(users);
		} catch (IOException e) {
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}
	
	public void saveUsedVacation(MultipartFile file) {
		try {
			CSVUtil csvUtil = new CSVUtil();
			
			List<UsedVacation> usedVacation = csvUtil.csvToUsedVacation(file.getInputStream(), userRepository);
			usedVacationRepository.saveAll(usedVacation);
		} catch (IOException e) {
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}

	public List<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}
	
	public List<UsedVacation> getAllVacation(){
		return usedVacationRepository.findAll();
	}
}

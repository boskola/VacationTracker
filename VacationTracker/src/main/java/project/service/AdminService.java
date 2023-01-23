package project.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import project.model.UsedVacation;
import project.model.UserEntity;

public interface AdminService {
	
	public void saveEmployees(MultipartFile file, PasswordEncoder passwordEncoder);
	
	public void saveUsedVacation(MultipartFile file);
	
	public void saveTotalVacationDays(MultipartFile file);

	public List<UserEntity> getAllUsers();
	
	public List<UsedVacation> getAllVacation();
}

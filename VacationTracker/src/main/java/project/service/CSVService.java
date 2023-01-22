package project.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import project.model.UserEntity;
import project.repository.UserRepository;
import project.util.CSVUtil;

@Service
public class CSVService {
 
	@Autowired
	UserRepository repository;

	public void save(MultipartFile file, PasswordEncoder passwordEncoder) {
		try {
			List<UserEntity> users = CSVUtil.csvToUsers(file.getInputStream(), passwordEncoder);
			repository.saveAll(users);
		} catch (IOException e) {
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}

	public List<UserEntity> getAllUsers() {
		return repository.findAll();
	}
}

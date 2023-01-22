package project.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import project.enumeration.Role;
import project.model.UserEntity;

public class CSVUtil {

	public static String TYPE = "text/csv";

	public static boolean hasCSVFormat(MultipartFile file) {
	
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
	
		return true;
	}
	  
	public static List<UserEntity> csvToUsers(InputStream is, PasswordEncoder passwordEncoder) {
		  
		  try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"))){
			  fileReader.readLine();
			  try (CSVParser csvParser = new CSVParser(fileReader,
		            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())){
				  
		  	      List<UserEntity> users = new ArrayList<UserEntity>();
			      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
		      
			      int i=0;
			      for (CSVRecord csvRecord : csvRecords) {
			    	  
			    	  Role role = Role.EMPLOYEE;
			    	  
			    	  if (i<2)
			    		  role=Role.ADMIN;
			    	  
			    	  UserEntity user = new UserEntity(
			              csvRecord.get("Employee Email"),
			              passwordEncoder.encode(csvRecord.get("Employee Password")),
			              role);
			    	  i++;
		
			    	  users.add(user);
			      }
		      
			      return users;
			  }
			  
			  catch (IOException e) {
				  throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
			  }
		  } 
		  
		  catch (IOException e) {
			  throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		  }
	}
}

package project.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import project.enumeration.Role;
import project.model.UsedVacation;
import project.model.UserEntity;
import project.model.Vacation;
import project.repository.UserRepository;

public class CSVUtil {
	
	public static String TYPE = "text/csv";

	public static boolean hasCSVFormat(MultipartFile file) {
	
		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}
	  
	public List<UserEntity> csvToUsers(InputStream is, PasswordEncoder passwordEncoder) {
		  
		  try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"))){
			  fileReader.readLine();
			  try (CSVParser csvParser = new CSVParser(fileReader,
		            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())){
				  
		  	      List<UserEntity> users = new ArrayList<UserEntity>();
			      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
		      
			      for (CSVRecord csvRecord : csvRecords) {
			    	  
			    	  UserEntity user = new UserEntity(
			              csvRecord.get("Employee Email"),
			              passwordEncoder.encode(csvRecord.get("Employee Password")),
			              Role.EMPLOYEE);
			    	  
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
	
	public List<UsedVacation> csvToUsedVacation(InputStream is, UserRepository userRepository) {
		  
		  try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"))){
			 
			  try (CSVParser csvParser = new CSVParser(fileReader,
		            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())){
				  
		  	      List<UsedVacation> usedVacation = new ArrayList<UsedVacation>();
			      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			      
			      for (CSVRecord csvRecord : csvRecords) {
			    	  
			    	  UsedVacation oneUsedVacation = new UsedVacation(
				              getLocalDate(csvRecord.get("Vacation start date")),
				              getLocalDate(csvRecord.get("Vacation end date")),
				              userRepository.findByUserEmail(csvRecord.get("Employee")).get());

			    	  usedVacation.add(oneUsedVacation);
			      }
			      return usedVacation;
			  }
			  catch (IOException e) {
				  throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
			  }
		  } 
		  catch (IOException e) {
			  throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		  }
	}
	
	public List<Vacation> csvToTotalVacationDays(InputStream is, UserRepository userRepository) {
		  
		  try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"))){
			  String firstLine = fileReader.readLine();
			  String parts[] = firstLine.split(",");
	
			  try (CSVParser csvParser = new CSVParser(fileReader,
		            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())){
				  
		  	      List<Vacation> totalVacationDays = new ArrayList<Vacation>();
			      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			      
			      for (CSVRecord csvRecord : csvRecords) {
			  
			    	  Vacation vacationDays = new Vacation(
				              
			    			  Integer.parseInt(csvRecord.get("Total vacation days")),
			    			  Integer.parseInt(parts[1]),
			    			  userRepository.findByUserEmail(csvRecord.get("Employee")).get());
	
			    	  totalVacationDays.add(vacationDays);
			      }
			      return totalVacationDays;
			  }
			  catch (IOException e) {
				  throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
			  }
		  } 
		  
		  catch (IOException e) {
			  throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		  }
	}
	
    private LocalDate getLocalDate(String stringDate) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        LocalDate date = LocalDate.parse(stringDate, formatter);
        
        return date;
    }

}

package project.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import project.enumeration.Role;
import project.model.User;

public class CSVUtil {
	
	public static String TYPE = "text/csv";

	public static boolean hasCSVFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
	    }

	    	return true;
	  	}
	  
	public static List<User> csvToUsers(InputStream is) {
		  
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"))){
	    	fileReader.readLine();
	    	try (CSVParser csvParser = new CSVParser(fileReader,
		            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())){
	  	      List<User> users = new ArrayList<User>();
		      
		      Iterable<CSVRecord> csvRecords = csvParser.getRecords();
		      
		      int i=0;
		      for (CSVRecord csvRecord : csvRecords) {
		    	  
		    	  Role role = Role.EMPLOYEE;
		    	  
		    	  if (i<2)
		    		  role=Role.ADMIN;
		    	 
		    	  
		    	  User user = new User(
		              csvRecord.get("Employee Email"),
		              csvRecord.get("Employee Password"),
		              role
		            );
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

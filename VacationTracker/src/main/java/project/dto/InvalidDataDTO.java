package project.dto;

public class InvalidDataDTO {
	
	private String userEmail;
	
	private int availableDays;
	
	private int vacationYear;
	
	public InvalidDataDTO() {}

	public InvalidDataDTO(String userEmail, int availableDays, int vacationYear) {
		super();
		this.userEmail = userEmail;
		this.availableDays = availableDays;
		this.vacationYear = vacationYear;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getAvailableDays() {
		return availableDays;
	}

	public void setAvailableDays(int availableDays) {
		this.availableDays = availableDays;
	}

	public int getVacationYear() {
		return vacationYear;
	}

	public void setVacationYear(int vacationYear) {
		this.vacationYear = vacationYear;
	}
}

package project.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class UsedVacationDTO {
	
	@NotEmpty(message = "Please enter start date")
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])", message = "Date is not valid.")
	private String vacationStartDate;
	
	@NotEmpty(message = "Please enter end date")
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])", message = "Date is not valid.")
	private String vacationEndDate;
	
	public UsedVacationDTO() {}

	public String getVacationStartDate() {
		return vacationStartDate;
	}

	public void setVacationStartDate(String vacationStartDate) {
		this.vacationStartDate = vacationStartDate;
	}

	public String getVacationEndDate() {
		return vacationEndDate;
	}

	public void setVacationEndDate(String vacationEndDate) {
		this.vacationEndDate = vacationEndDate;
	}

	@Override
	public String toString() {
		return "UsedVacationDTO [vacationStartDate=" + vacationStartDate + ", vacationEndDate=" + vacationEndDate + "]";
	}
}

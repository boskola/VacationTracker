package project.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class UsedVacation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private LocalDate vacationStartDate;
	
	@Column(nullable = false)
	private LocalDate vacationEndDate;
	
	@ManyToOne
	private UserEntity user;
	
	public UsedVacation() {}

	public UsedVacation(LocalDate vacationStartDate, LocalDate vacationEndDate, UserEntity user) {
		super();
		this.vacationStartDate = vacationStartDate;
		this.vacationEndDate = vacationEndDate;
		this.user = user;
	}
	
	public UsedVacation(LocalDate vacationStartDate, LocalDate vacationEndDate) {
		super();
		this.vacationStartDate = vacationStartDate;
		this.vacationEndDate = vacationEndDate;
		this.user = null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getVacationStartDate() {
		return vacationStartDate;
	}

	public void setVacationStartDate(LocalDate vacationStartDate) {
		this.vacationStartDate = vacationStartDate;
	}

	public LocalDate getVacationEndDate() {
		return vacationEndDate;
	}

	public void setVacationEndDate(LocalDate vacationEndDate) {
		this.vacationEndDate = vacationEndDate;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsedVacation other = (UsedVacation) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "UsedVacation [id=" + id + ", vacationStartDate=" + vacationStartDate + ", vacationEndDate="
				+ vacationEndDate + ", userEmail=" + user.getUserEmail() + "]";
	}
	
	

}

package project.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Vacation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private int totalVacationDays;
	
	@Column(nullable = false)
	private int vacationYear;
	
	@ManyToOne
	private UserEntity user;
	
	public Vacation() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTotalVacationDays() {
		return totalVacationDays;
	}

	public void setTotalVacationDays(int totalVacationDays) {
		this.totalVacationDays = totalVacationDays;
	}

	public int getVacationYear() {
		return vacationYear;
	}

	public void setVacationYear(int vacationYear) {
		this.vacationYear = vacationYear;
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
		Vacation other = (Vacation) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Vacation [id=" + id + ", totalVacationDays=" + totalVacationDays + ", vacationYear=" + vacationYear
				+ ", userEmail=" + user.getUserEmail() + "]";
	}
	

}

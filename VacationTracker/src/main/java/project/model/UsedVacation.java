package project.model;

import java.time.LocalDateTime;
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
	private LocalDateTime vacationStartDate;
	
	@Column(nullable = false)
	private LocalDateTime vacationEndDate;
	
	@ManyToOne
	private UserEntity user;
	
	public UsedVacation() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getVacationStartDate() {
		return vacationStartDate;
	}

	public void setVacationStartDate(LocalDateTime vacationStartDate) {
		this.vacationStartDate = vacationStartDate;
	}

	public LocalDateTime getVacationEndDate() {
		return vacationEndDate;
	}

	public void setVacationEndDate(LocalDateTime vacationEndDate) {
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

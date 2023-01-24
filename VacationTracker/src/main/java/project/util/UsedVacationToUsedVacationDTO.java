package project.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import project.dto.UsedVacationDTO;
import project.model.UsedVacation;

@Component
public class UsedVacationToUsedVacationDTO implements Converter<UsedVacation, UsedVacationDTO>{

	@Override
	public UsedVacationDTO convert(UsedVacation usedVacation) {
		
		UsedVacationDTO usedVacationDTO = new UsedVacationDTO();
		
		usedVacationDTO.setVacationStartDate(usedVacation.getVacationStartDate().toString());
		usedVacationDTO.setVacationEndDate(usedVacation.getVacationEndDate().toString());
		
		return usedVacationDTO;
	}
	
	public List<UsedVacationDTO> convert(List<UsedVacation>usedVacation){
		
		List<UsedVacationDTO> usedVacationDTO = new ArrayList<>();
		
		for(UsedVacation uv: usedVacation) {
			usedVacationDTO.add(convert(uv));
		}
		
		return usedVacationDTO;
	}

}

package restR.logic.converters;

import org.springframework.stereotype.Component;

import restR.entities.RoleEnum;
import restR.entities.RoleEnumInDB;
@Component
public class RoleConverter {
	
	public RoleEnum toBoundaryEnum(RoleEnumInDB role) {
		return RoleEnum.valueOf(role.name().toUpperCase());
	}

	public RoleEnumInDB toEntityEnum(RoleEnum role) {
		return RoleEnumInDB.valueOf(role.name().toLowerCase());
	}


}

package restR.logic.converters;

import org.springframework.stereotype.Component;

import restR.boundaries.NewUserBoundary;
import restR.boundaries.UserBoundary;
import restR.entities.UserEntity;
import restR.general.UserId;

@Component
public class UserConverter {
	private String delimeter = "/";
	private RoleConverter roleConverter;
	
	

	public UserConverter(RoleConverter roleConverter) {
		this.roleConverter = roleConverter;
	}

	public UserBoundary toBoundary(UserEntity entity) {
		UserBoundary rv = new UserBoundary();
		rv.setAvatar(entity.getAvatar());
		System.err.println(entity.getUserId());
		rv.setUserId(new UserId(entity.getUserId()));
		rv.setUsername(entity.getUsername());
		rv.setRole(roleConverter.toBoundaryEnum(entity.getRole()));

		return rv;
	}

	public UserEntity toEntity(NewUserBoundary boundary, String superApp) {
		UserEntity rv = new UserEntity();
			rv.setAvatar(boundary.getAvatar());
			rv.setRole(roleConverter.toEntityEnum(boundary.getRole()));
			rv.setUserId(superApp + delimeter + boundary.getEmail());
			rv.setUsername(boundary.getUsername());
		return rv;

	}
}

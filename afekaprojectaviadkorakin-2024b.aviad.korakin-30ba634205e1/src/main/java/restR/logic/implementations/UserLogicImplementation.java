package restR.logic.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import restR.boundaries.NewUserBoundary;
import restR.boundaries.UserBoundary;
import restR.crud.UserCrud;
import restR.entities.RoleEnum;
import restR.entities.UserEntity;
import restR.general.UserId;
import restR.logic.converters.RoleConverter;
import restR.logic.converters.UserConverter;
import restR.logic.exceptions.RestRBadRequestException;
import restR.logic.exceptions.RestRNotFoundException;
import restR.logic.exceptions.RestRUnauthorizedException;
import restR.logic.logicInterfaces.UserLogic;
import restR.logic.validTests.ValidTestsString;
import restR.logic.validTests.ValidTestsUser;

@Service
public class UserLogicImplementation implements UserLogic {
	private UserCrud userCrud;
	private UserConverter userConverter;
	private RoleConverter roleConverter;
	private ValidTestsUser validTestsUser;
	private ValidTestsString validTestsString;
	private EmailLogicImplemention sender;
	private String superappName;

	public UserLogicImplementation(UserCrud userCrud, UserConverter userConverter,RoleConverter roleConverter, ValidTestsUser validTestsUser,
			EmailLogicImplemention sender, ValidTestsString validTestsString) {
		this.userCrud = userCrud;
		this.userConverter = userConverter;
		this.validTestsUser = validTestsUser;
		this.sender = sender;
		this.validTestsString = validTestsString;
		this.roleConverter=roleConverter;
	}

	@Override
	@Transactional
	public UserBoundary storeInDatabase(NewUserBoundary newuser) {
		validTestsUser.TestNotNullsorBlankParams(newuser);
		if (validTestsUser.isValidEmail(newuser.getEmail()) == false)
			throw new RestRBadRequestException("The email " + newuser.getEmail() + " isn't allowed");
		validTestsUser.ifExistThrows(new UserId(superappName, newuser.getEmail()));
		UserEntity entity = this.userConverter.toEntity(newuser, superappName);
		entity = this.userCrud.save(entity);
		try {
			sender.registerMessage(newuser.getEmail(), entity);
		} catch (MessagingException e) {
			throw new RestRBadRequestException("Can't send the email");
		}
		return this.userConverter.toBoundary(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary getSpecificUserFromDatabase(String eSuperapp, String email) {
		return this.userCrud.findById(new UserId(eSuperapp, email).toString())
				.map(entity -> this.userConverter.toBoundary(entity)).orElseThrow(
						() -> new RestRNotFoundException("Couldn't find user with email " + email + " in database."));
	}

	@Override
	@Transactional
	public void updateById(String eSuperapp, String email, UserBoundary boundary) {
		UserEntity existing = validTestsUser.ifExistReturn(new UserId(eSuperapp, email));
		if (!validTestsString.isNullorBlank(boundary.getUsername()))
			existing.setUsername(boundary.getUsername());
		if (!validTestsString.isNullorBlank(boundary.getAvatar()))
			existing.setAvatar(boundary.getAvatar());
		if (boundary.getRole() != null)
			existing.setRole(roleConverter.toEntityEnum(boundary.getRole()));
		this.userCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteAll(String userSuperapp, String email) {
		if (validTestsUser.isRole(new UserId(userSuperapp, email), RoleEnum.ADMIN) == false)
			throw new RestRUnauthorizedException("Denied! out of your premmisions");
		this.userCrud.deleteAll();

	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> ExportAllUsers(String userSuperapp, String email, int size, int page) {
		if (validTestsUser.isRole(new UserId(userSuperapp, email), RoleEnum.ADMIN) == false)
			throw new RestRUnauthorizedException("Denied! out of your premmisions");

		return this.userCrud.findAll(PageRequest.of(page, size, Direction.ASC, "role", "username")).stream().parallel().map
				(this.userConverter::toBoundary).peek(System.err::println).toList();
	}

	@Override
	@Deprecated
	public List<UserBoundary> ExportAllUsers() {
		throw new RestRBadRequestException("deprecated operation");
		/*
		 * List<UserBoundary> returnedList =
		 * this.userCrud.findAll().stream().map(this.userConverter::toBoundary)
		 * .peek(System.err::println).toList(); if (returnedList.isEmpty()) throw new
		 * RestRNotFoundException("NO DATA."); return returnedList;
		 */
	}

	@Value("${spring.application.name:supperapp}")
	private void setSuperappName(String superappName) {
		this.superappName = superappName;
	}

}

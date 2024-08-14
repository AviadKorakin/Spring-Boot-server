package restR.logic.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import restR.boundaries.MiniAppCommandBoundary;
import restR.crud.MiniAppCommandCrud;
import restR.entities.MiniAppCommandEntity;
import restR.entities.RoleEnum;
import restR.general.CommandId;
import restR.general.TargetObject;
import restR.general.UserId;
import restR.logic.validTests.ValidTestsObject;
import restR.logic.validTests.ValidTestsUser;
import restR.logic.commands.Command;
import restR.logic.commands.DefaultCommand;
import restR.logic.converters.MiniAppCommandConverter;
import restR.logic.exceptions.RestRBadRequestException;
import restR.logic.exceptions.RestRUnauthorizedException;
import restR.logic.logicInterfaces.MiniAppCommandLogic;
import restR.logic.validTests.ValidTestsMiniAppCommand;

@Service
public class MiniAppCommandLogicImplementation implements MiniAppCommandLogic {
	private MiniAppCommandCrud miniAppCommandCrud;
	private MiniAppCommandConverter miniAppCommandConverter;
	private ValidTestsObject validTestsObject;
	private ValidTestsUser validTestsUser;
	private ValidTestsMiniAppCommand validTestsMiniAppCommand;
	private String superappName;
	private ApplicationContext applicationContext;
	private DefaultCommand defaultCommand;
	private Log log = LogFactory.getLog(MiniAppCommandLogicImplementation.class);




	public MiniAppCommandLogicImplementation(MiniAppCommandCrud miniAppCommandCrud,
			MiniAppCommandConverter miniAppCommandConverter, ValidTestsObject validTestsObject,
			ValidTestsUser validTestsUser, ValidTestsMiniAppCommand validTestsMiniAppCommand,
			ApplicationContext applicationContext, DefaultCommand defaultCommand) {
		super();
		this.miniAppCommandCrud = miniAppCommandCrud;
		this.miniAppCommandConverter = miniAppCommandConverter;
		this.validTestsObject = validTestsObject;
		this.validTestsUser = validTestsUser;
		this.validTestsMiniAppCommand = validTestsMiniAppCommand;
		this.applicationContext = applicationContext;
		this.defaultCommand = defaultCommand;
	}

	@Override
	@Transactional
	public List<Object> invokeaCommand(MiniAppCommandBoundary newMiniCmd, String miniAppName) {
		validTestsMiniAppCommand.TestNotNullsorBlankParams(newMiniCmd);
		if (validTestsUser.getRole(newMiniCmd.getInvokedBy().getUserId()) != RoleEnum.MINIAPP_USER)
			throw new RestRUnauthorizedException("Denied! out of your premmisions");

		//validTestsObject.ifExistAndActiveReturn(newMiniCmd.getTargetObject().getObjectId());
		if (newMiniCmd.getTargetObject() == null)
			newMiniCmd.setTargetObject(new TargetObject());
		if (newMiniCmd.getCommandId() == null)
			newMiniCmd.setCommandId(new CommandId());
		newMiniCmd.getCommandId().setMiniapp(miniAppName);
		newMiniCmd.getCommandId().setSuperapp(superappName);

		do {
			newMiniCmd.getCommandId().setId(UUID.randomUUID().toString());
		} while (validTestsMiniAppCommand.isExist(newMiniCmd.getCommandId()));

		newMiniCmd.setInvocationTimestamp(new Date());

		MiniAppCommandEntity entity = this.miniAppCommandConverter.toEntity(newMiniCmd);
		entity = this.miniAppCommandCrud.save(entity);
		//this.miniAppCommandConverter.toBoundary(entity);
		this.log.debug("** about to play: " + newMiniCmd.getCommand());
		Command command = null;
		try {
			command = this.applicationContext.getBean(newMiniCmd.getCommand(), Command.class);
		}catch (Exception e) {
			this.log.warn("** could not find implementation for " + newMiniCmd.getCommand() + ", using defaultGame instead");
			command = this.defaultCommand;
		}
		this.log.trace("** game implemented using " + command.getClass().getSimpleName() + " class");
		try {
			return command.invoke(newMiniCmd);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new RestRBadRequestException(e.getMessage());
		}
	}

	@Override
	@Deprecated
	public List<MiniAppCommandBoundary> ExportAllMiniAppCommands() {
		throw new RestRBadRequestException("deprecated operation");

		/*
		 * List<MiniAppCommandBoundary> returnedList =
		 * this.miniAppCommandCrud.findAll().stream()
		 * .map(this.miniAppCommandConverter::toBoundary).peek(System.err::println).
		 * toList(); if (returnedList.isEmpty()) throw new
		 * RestRNotFoundException("NO DATA."); return returnedList;
		 */
	}

	@Override
	@Deprecated
	public List<MiniAppCommandBoundary> ExportSpecificMiniAppCommands(String miniApp) {
		throw new RestRBadRequestException("deprecated operation");
		/*
		 * List<MiniAppCommandBoundary> returnedList =
		 * this.miniAppCommandCrud.findAllByMiniApp(miniApp).stream()
		 * .map(this.miniAppCommandConverter::toBoundary).peek(System.err::println).
		 * toList(); if (returnedList.isEmpty()) throw new
		 * RestRNotFoundException("Couldn't find miniapp named " + miniApp +
		 * " in database"); return returnedList;
		 */
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> ExportSpecificMiniAppCommands(String userSuperapp, String email, String miniApp,
			int size, int page) {
		if (validTestsUser.isRole(new UserId(userSuperapp, email), RoleEnum.ADMIN) == false)
			throw new RestRUnauthorizedException("Denied! out of your premmisions");

		return this.miniAppCommandCrud
				.findAllByMiniapp(miniApp, PageRequest.of(page, size, Direction.ASC, "commandId", "command")).stream()
				.map(this.miniAppCommandConverter::toBoundary).peek(System.err::println).toList();
	}

	@Override
	@Transactional
	public void deleteAll(String userSuperapp, String email) {

		if (validTestsUser.isRole(new UserId(userSuperapp, email), RoleEnum.ADMIN) == false)
			throw new RestRUnauthorizedException("Denied! out of your premmisions");

		this.miniAppCommandCrud.deleteAll();

	}

	@Override
	@Transactional(readOnly = true)
	public List<MiniAppCommandBoundary> ExportAllMiniAppCommands(String userSuperapp, String email, int size,
			int page) {
		if (validTestsUser.isRole(new UserId(userSuperapp, email), RoleEnum.ADMIN) == false)
			throw new RestRUnauthorizedException("Denied! out of your premmisions");

		List<MiniAppCommandEntity> entities = this.miniAppCommandCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, "commandId", "command")).toList();

		List<MiniAppCommandBoundary> rv = new ArrayList<>();

		for (MiniAppCommandEntity entity : entities) {
			rv.add(this.miniAppCommandConverter.toBoundary(entity));
		}

		return rv;
	}

	@Value("${spring.application.name:supperapp}")
	private void setSuperappName(String superappName) {
		this.superappName = superappName;
	}

}

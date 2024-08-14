package restR.entities;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import restR.logic.converters.MapToJsonConverter;

@Entity
@Table(name = "COMMANDS_TBL")
public class MiniAppCommandEntity {
	@Id
	private String commandId; 
	
	private String miniapp;
	
	private String targetObject;

	@Temporal(TemporalType.TIMESTAMP)
	private Date invocationTimestamp;
	
	private String invokedBy;
	
	private String command;
	
	@Convert(converter = MapToJsonConverter.class)
	@Lob
	private Map<String,Object> commandAttributes;

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getMiniapp() {
		return miniapp;
	}

	public void setMiniapp(String miniapp) {
		this.miniapp = miniapp;
	}

	public String getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}

	public String getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(String invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getCommandAttributes() {
		return commandAttributes;
	}

	public void setCommandAttributes(Map<String, Object> commandAttributes) {
		this.commandAttributes = commandAttributes;
	}
	
	
	
	

}

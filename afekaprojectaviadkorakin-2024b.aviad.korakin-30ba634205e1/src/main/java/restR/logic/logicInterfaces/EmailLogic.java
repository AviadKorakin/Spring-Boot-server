package restR.logic.logicInterfaces;

import java.util.ArrayList;

import jakarta.mail.MessagingException;
import restR.entities.UserEntity;
import restR.general.MenuItem;

public interface EmailLogic {
	void sendSimpleMessage(String emailTo, String subject, String body);

	void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment)
			throws MessagingException;

	void registerMessage(String to, UserEntity user) throws MessagingException;

	public void ReservationMessage(String to, String tableNumber, String locationDesc, int day, int hour)
			throws MessagingException;

	public void OrderMessage(String to, ArrayList<MenuItem> menuItems, double totalPrice) throws MessagingException ;
}

package restR.logic.implementations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import restR.entities.UserEntity;
import restR.general.MenuItem;
import restR.general.UserId;
import restR.logic.logicInterfaces.EmailLogic;

@Service
public class EmailLogicImplemention implements EmailLogic {
	private JavaMailSender emailSender;
	private final String myEmail = "restaurantemailmaker@gmail.com";
	private final String myPassword = "aumjywjqjcnfitnc";

	public EmailLogicImplemention() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername(myEmail);
		mailSender.setPassword(myPassword);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		this.emailSender = mailSender;

	}

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(myEmail);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}

	@Override
	public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment)
			throws MessagingException {
		// ...

		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom("restaurantemailmaker@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);
		/*
		 * FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
		 * helper.addAttachment("Invoice", file);
		 */
		emailSender.send(message);
		// ...
	}

	public void ReservationMessage(String to, String tableNumber, String locationDesc, int day, int hour)
			throws MessagingException {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true); // Set true for multipart message

		helper.setFrom("restaurantemailmaker@gmail.com");
		helper.setTo(to);
		helper.setSubject("Your Reservation is Ready!");
		String time = null;
		switch (day) {
		case 0:
			time = "Sunday at " + hour + ":00";
		case 1:
			time = "Monday at " + hour + ":00";
		case 2:
			time = "Tuesday at " + hour + ":00";
		case 3:
			time = "Wenday at " + hour + ":00";
		case 4:
			time = "Thursday at " + hour + ":00";
		case 5:
			time = "Friday at " + hour + ":00";
		case 6:
			time = "Saturday at " + hour + ":00";
		}
		// Build the HTML content with user information
		String htmlContent = buildReservationMessage(tableNumber, locationDesc, time);
		helper.setText(htmlContent, true); // Set true for HTML content

		emailSender.send(message);
	}

	public void registerMessage(String to, UserEntity user) throws MessagingException {

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true); // Set true for multipart message

		helper.setFrom("restaurantemailmaker@gmail.com");
		helper.setTo(to);
		helper.setSubject("Welcome to Our Premium Restaurant, " + user.getUsername() + "!");

		// Build the HTML content with user information
		String htmlContent = buildRegistrationMessage(user);
		helper.setText(htmlContent, true); // Set true for HTML content

		emailSender.send(message);
	}

	public void OrderMessage(String to, ArrayList<MenuItem> menuItems, double totalPrice) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom("restaurantemailmaker@gmail.com");
		helper.setTo(to);
		helper.setSubject("Your Order Summary");
		String htmlContent = buildOrderHTML(menuItems, totalPrice);

		// Create a temporary file for the receipt
		File receiptFile = null;
		try {
			receiptFile = createReceiptFile(menuItems, totalPrice);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Build the HTML content with order information
		helper.addAttachment("order_receipt.html", receiptFile);
		helper.setText(htmlContent, true);
		emailSender.send(message);
	}

	private static String buildOrderHTML(ArrayList<MenuItem> menuItems, double totalPrice) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html><html><body style=\"background-color: #f8f8f8; font-family: sans-serif;\">");
		sb.append("<h1 style=\"color: #c02942; text-align: center;\">Your Order Summary</h1>");
		sb.append("<p style=\"color: #333; font-size: 16px;\">Thank you for your order! Here are the details:</p>");
		sb.append("<br>");

		// Build the order items table
		sb.append("<table style=\"width: 100%; border-collapse: collapse;\">");
		sb.append("<thead>");
		sb.append("<tr style=\"background-color: #c02942; color: white;\">");
		sb.append("<th style=\"padding: 8px; border: 1px solid #ddd;\">Name</th>");
		sb.append("<th style=\"padding: 8px; border: 1px solid #ddd;\">Description</th>");
		sb.append("<th style=\"padding: 8px; border: 1px solid #ddd;\">Price</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");

		for (MenuItem item : menuItems) {
			sb.append("<tr style=\"background-color: #f2f2f2;\">");
			sb.append("<td style=\"padding: 8px; border: 1px solid #ddd;\">" + item.getName() + "</td>");
			sb.append("<td style=\"padding: 8px; border: 1px solid #ddd;\">" + item.getDescription() + "</td>");
			sb.append("<td style=\"padding: 8px; border: 1px solid #ddd;\">" + String.format("%.2f", item.getPrice())
					+ "</td>");
			sb.append("</tr>");
		}

		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("<br>");

		// Display the total price
		sb.append("<p style=\"color: #333; font-size: 16px; font-weight: bold;\">Total Price:"
				+ String.format(" %.2f NIS", totalPrice) + "</p>");
		sb.append("<br>");
		sb.append(
				"<p style=\"color: #333; font-size: 16px;\">We hope you enjoy your meal! Thank you for choosing our restaurant.</p>");
		sb.append("<br>");
		sb.append("<p style=\"color: #333; font-size: 16px;\">Sincerely,</p>");
		sb.append("<p style=\"color: #333; font-size: 16px; font-weight: bold;\">The Restaurant Team</p>");
		sb.append("</body></html>");

		return sb.toString();
	}

	private static File createReceiptFile(ArrayList<MenuItem> menuItems, double totalPrice) throws IOException {
		File tempFile = File.createTempFile("order_receipt", ".html");
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html><html><body style=\"background-color: #f8f8f8; font-family: sans-serif;\">");
		sb.append("<h1 style=\"color: #c02942; text-align: center;\">Your Order Summary</h1>");
		sb.append("<p style=\"color: #333; font-size: 16px;\">Thank you for your order! Here are the details:</p>");
		sb.append("<br>");

		// Build the order items table
		sb.append("<table style=\"width: 100%; border-collapse: collapse;\">");
		sb.append("<thead>");
		sb.append("<tr style=\"background-color: #c02942; color: white;\">");
		sb.append("<th style=\"padding: 8px; border: 1px solid #ddd;\">Name</th>");
		sb.append("<th style=\"padding: 8px; border: 1px solid #ddd;\">Description</th>");
		sb.append("<th style=\"padding: 8px; border: 1px solid #ddd;\">Price</th>");
		sb.append("<th style=\"padding: 8px; border: 1px solid #ddd;\">Image</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");

		for (MenuItem item : menuItems) {
			sb.append("<tr style=\"background-color: #f2f2f2;\">");
			sb.append("<td style=\"padding: 8px; border: 1px solid #ddd;\">" + item.getName() + "</td>");
			sb.append("<td style=\"padding: 8px; border: 1px solid #ddd;\">" + item.getDescription() + "</td>");
			sb.append("<td style=\"padding: 8px; border: 1px solid #ddd;\">" + String.format("%.2f", item.getPrice())
					+ "</td>");
			sb.append("<td style=\"padding: 8px; border: 1px solid #ddd;\"><img src=\"" + item.getImgURL() + "\" alt=\""
					+ item.getName() + " Image\" style=\"width: 100px; height: auto;\"></td>");
			sb.append("</tr>");
		}

		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("<br>");

		// Display the total price
		sb.append("<p style=\"color: #333; font-size: 16px; font-weight: bold;\">Total Price:"
				+ String.format(" %.2f NIS", totalPrice) + "</p>");
		sb.append("<br>");
		sb.append(
				"<p style=\"color: #333; font-size: 16px;\">We hope you enjoy your meal! Thank you for choosing our restaurant.</p>");
		sb.append("<br>");
		sb.append("<p style=\"color: #333; font-size: 16px;\">Sincerely,</p>");
		sb.append("<p style=\"color: #333; font-size: 16px; font-weight: bold;\">The Restaurant Team</p>");
		sb.append("</body></html>");

		try (FileWriter writer = new FileWriter(tempFile)) {
			writer.write(sb.toString());
		}

		return tempFile;
	}

	private String buildRegistrationMessage(UserEntity user) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html><body style=\"background-color: #f8f8f8; font-family: sans-serif;\">");
		sb.append("<h1 style=\"color: #c02942; text-align: center;\">Welcome to Our Restaurant, " + user.getUsername()
				+ "!</h1>");
		sb.append(
				"<p style=\"color: #333; font-size: 16px;\">We're thrilled to have you join our exclusive community of food lovers.</p>");
		sb.append("<br>");

		sb.append("<br>");
		sb.append("<p style=\"color: #333; font-size: 16px;\"><b>Here are your login credentials:</b></p>");
		sb.append("<ul style=\"list-style-type: disc; padding-left: 20px;\">");
		sb.append("<li><b>Username:</b> " + user.getUsername() + "</li>");
		sb.append("<li><b>Role:</b> " + user.getRole() + "</li>");
		sb.append("<li><b>Super App:</b> " + new UserId(user.getUserId()).getSuperapp() + "</li>");
		sb.append("</ul>");
		sb.append("<br>");
		sb.append("<p style=\"color: #333; font-size: 16px;\"><i>**Important Notes:**</i></p>");
		sb.append("<ul style=\"list-style-type: disc; padding-left: 20px;\">");
		sb.append("<li><i>We highly recommend exploring our mobile app for an enhanced dining experience.</i></li>");
		sb.append("</ul>");
		sb.append(
				"<p style=\"color: #333; font-size: 16px;\">We hope you enjoy a delightful dining experience with us! Explore our exquisite menu and make a reservation today.</p>");
		sb.append("<br>");
		sb.append("<p style=\"color: #333; font-size: 16px;\">Sincerely,</p>");
		sb.append("<p style=\"color: #333; font-size: 16px; font-weight: bold;\">The Restaurant Team</p>");
		sb.append("</body></html>");
		return sb.toString();
	}

	private String buildReservationMessage(String tableNumber, String locationDesc, String time) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html><body style=\"background-color: #ffffff; font-family: sans-serif;\">");
		sb.append("<h1 style=\"color: #c02942; text-align: center;\">Premium Reservation Confirmation</h1>");
		sb.append("<p style=\"color: #333; font-size: 16px;\">Dear Valued Customer,</p>");
		sb.append(
				"<p style=\"color: #333; font-size: 16px;\">Thank you for choosing our restaurant for your dining experience. We are delighted to confirm your reservation details as follows:</p>");
		sb.append("<br>");
		sb.append("<p style=\"color: #333; font-size: 16px;\"><b>Reservation Details:</b></p>");
		sb.append("<ul style=\"list-style-type: disc; padding-left: 20px;\">");
		sb.append("<li><b>Table Number:</b> " + tableNumber + "</li>");
		sb.append("<li><b>Location:</b> " + locationDesc + "</li>");
		sb.append("<li><b>Date:</b> " + time + "</li>");
		sb.append("</ul>");
		sb.append("<br>");
		sb.append("<p style=\"color: #333; font-size: 16px;\"><i>**Special Notes:**</i></p>");
		sb.append("<ul style=\"list-style-type: disc; padding-left: 20px;\">");
		sb.append("<li><i>Please arrive 10 minutes before your reservation time.</i></li>");
		sb.append("<li><i>Our dress code is smart casual. We look forward to serving you.</i></li>");
		sb.append("</ul>");
		sb.append(
				"<p style=\"color: #333; font-size: 16px;\">We hope you enjoy a luxurious dining experience with us! If you have any special requests or dietary requirements, please do not hesitate to let us know.</p>");
		sb.append("<br>");
		sb.append("<p style=\"color: #333; font-size: 16px;\">Warm regards,</p>");
		sb.append("<p style=\"color: #333; font-size: 16px; font-weight: bold;\">The Restaurant Team</p>");
		sb.append("</body></html>");
		return sb.toString();
	}

}

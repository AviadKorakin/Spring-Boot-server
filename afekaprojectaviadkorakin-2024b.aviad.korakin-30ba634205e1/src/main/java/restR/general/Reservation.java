package restR.general;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class Reservation {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Date date;
	private InvokedBy invokedBy;
	private String phoneNumber;
	private String notes;
	public Reservation() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Reservation(Date date, InvokedBy invokedBy, String phoneNumber, String notes) {
		super();
		this.date = date;
		this.invokedBy = invokedBy;
		this.phoneNumber = phoneNumber;
		this.notes = notes;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public InvokedBy getInvokedBy() {
		return invokedBy;
	}
	public void setInvokedBy(InvokedBy invokedBy) {
		this.invokedBy = invokedBy;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
}

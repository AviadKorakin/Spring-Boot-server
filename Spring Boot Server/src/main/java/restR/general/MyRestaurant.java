package restR.general;

import org.springframework.stereotype.Component;

@Component
public class MyRestaurant {

	private String name="Afeka Restaurant";
	private Location loc= new Location(32.115139,34.817804);
	private Integer tableMaximumCapacity=8;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Location getLoc() {
		return loc;
	}
	public void setLoc(Location loc) {
		this.loc = loc;
	}
	public Integer getTableMaximumCapacity() {
		return tableMaximumCapacity;
	}
	public void setTableMaximumCapacity(Integer tableMaximumCapacity) {
		this.tableMaximumCapacity = tableMaximumCapacity;
	}
	public MyRestaurant() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MyRestaurant(String name, Location loc, Integer tableMaximumCapacity) {
		super();
		this.name = name;
		this.loc = loc;
		this.tableMaximumCapacity = tableMaximumCapacity;
	}

	
	
	
}

package restR.general;


import java.util.ArrayList;

public class MenuItem implements Comparable<MenuItem> {
    private String category;
    private String name;
    private String description;
    private Double price;
    private String imgURL;
    private ArrayList<String> allergens;

   
    public MenuItem() {
		super();
		// TODO Auto-generated constructor stub
	}


	public MenuItem(String category, String name, String description, Double price, String imgURL,
			ArrayList<String> allergens) {
		super();
		this.category = category;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgURL = imgURL;
		this.allergens = allergens;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	public String getImgURL() {
		return imgURL;
	}


	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}


	public ArrayList<String> getAllergens() {
		return allergens;
	}


	public void setAllergens(ArrayList<String> allergens) {
		this.allergens = allergens;
	}


	@Override
    public int compareTo(MenuItem o) {
        return this.category.compareTo(o.category);
    }
}




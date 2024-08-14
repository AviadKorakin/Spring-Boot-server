package restR.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import restR.boundaries.NewUserBoundary;
import restR.boundaries.ObjectBoundary;
import restR.boundaries.UserBoundary;
import restR.entities.RoleEnum;
import restR.general.CreatedBy;
import restR.general.Location;
import restR.general.ObjectId;
import restR.logic.logicInterfaces.ObjectLogic;
import restR.logic.logicInterfaces.UserLogic;

@Component
@Profile("manualTests")
public class RestInitializer implements CommandLineRunner {
	private UserLogic userlogic;
	private ObjectLogic objlogic;
	private UserBoundary admin;

	public RestInitializer(UserLogic userlogic, ObjectLogic objlogic) {
		super();
		this.userlogic = userlogic;
		this.objlogic = objlogic;
		NewUserBoundary nub = new NewUserBoundary("admin@admin.com", RoleEnum.ADMIN, "admin", "admin");
		admin = this.userlogic.storeInDatabase(nub);
	}

	@Override
	public void run(String... args) throws Exception {
		this.objlogic.deleteAll(admin.getUserId().getSuperapp(), admin.getUserId().getEmail());
		this.userlogic.deleteAll(admin.getUserId().getSuperapp(), admin.getUserId().getEmail());

		UserBoundary userSuper = this.userlogic.storeInDatabase(
				new NewUserBoundary("example1@example.example", RoleEnum.SUPERAPP_USER, "super", "super"));
		UserBoundary useMini = this.userlogic.storeInDatabase(
				new NewUserBoundary("example2@example.example", RoleEnum.MINIAPP_USER, "mini", "mini"));

		String[] titles = { "Iron Steak", "Caesar Salad", "Chocolate Cake", "Margarita", "Garlic Bread",
				"Spaghetti Carbonara", "Fruit Salad", "Lemonade", "Chicken Wings", "Grilled Salmon", "Ice Cream Sundae",
				"Iced Tea", "Bruschetta", "Beef Tacos", "Cheesecake" };
		String[] descriptions = { "Perfect cut of meat", "Fresh and crispy", "Rich and creamy", "Refreshing cocktail",
				"Crispy and buttery", "Creamy and delicious", "Fresh and sweet", "Cool and refreshing",
				"Spicy and tangy", "Perfectly grilled", "Sweet and delightful", "Cool and refreshing",
				"Fresh tomatoes and basil", "Flavorful and spicy", "Smooth and rich" };

		String[] imgURLs = {
				"https://www.allrecipes.com/thmb/boDrz1QzNNLeOEW5jjlfZKUIb_I=/0x512/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/150685-perfect-flat-iron-steak-DDMFS-4x3-3249da60e56343388122d1c615eb4c88.jpg",
				"https://www.seriouseats.com/thmb/Fi_FEyVa3_-_uzfXh6OdLrzal2M=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/the-best-caesar-salad-recipe-06-40e70f549ba2489db09355abd62f79a9.jpg",

				"https://images.immediate.co.uk/production/volatile/sites/2/2015/05/6522.jpg?quality=90&webp=true&resize=300,272",
				"https://cdn.loveandlemons.com/wp-content/uploads/2024/04/margarita-recipe.jpg",
				"https://theloopywhisk.com/wp-content/uploads/2023/12/Gluten-Free-Garlic-Bread_1200px-featured-2.jpg",
				"https://recipes.net/wp-content/uploads/2023/05/jamie-olivers-spaghetti-carbonara-recipe_6019122c01c76dc7c355280e963e5280-768x768.jpeg",
				"https://healthyfitnessmeals.com/wp-content/uploads/2022/05/Fruit-salad-recipe-3.jpg",
				"https://cdn.loveandlemons.com/wp-content/uploads/2022/06/lemonade.jpg",
				"https://whisperofyum.com/wp-content/uploads/2023/10/whisper-of-yum-honey-hot-wings.jpg",
				"https://hips.hearstapps.com/hmg-prod/images/how-to-grill-salmon-recipe1-1655870645.jpg?crop=0.8888888888888888xw:1xh;center,top&resize=1200:*",
				"https://www.stefanofaita.com/wp-content/uploads/2023/02/sundae-a-la-creme-glacee-maison-.jpg",
				"https://www.thespruceeats.com/thmb/jk3sZ3Jtq2WPnd31DrB-FR1qfs0=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/summer-peach-tea-cocktail-recipe-761506-hero-01-f949acc1ed22404da03ce72648412bcf.jpg",
				"https://www.lifeasastrawberry.com/wp-content/uploads/2012/11/warm-bruschetta-1.jpg",
				"https://danosseasoning.com/wp-content/uploads/2022/03/Beef-Tacos-768x575.jpg",
				"https://www.allrecipes.com/thmb/v8JZdIICA1oerzX0L-KzyW2w9hM=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/8350-chantals-new-york-cheesecake-DDMFS-4x3-426569e82b4142a6a1ed01e068544245.jpg"

		};
		double[] prices = { 85.2, 15.0, 12.5, 8.0, 6.5, 18.0, 10.0, 4.0, 12.0, 20.0, 9.0, 3.5, 7.5, 14.0, 8.5};
		List<List<String>> allergensList = List.of(List.of("potato", "hamin"), List.of("gluten", "dairy"),
				List.of("nuts", "soy"), List.of("none"), List.of("gluten"), List.of("gluten", "dairy"), List.of("none"),
				List.of("none"), List.of("soy"), List.of("fish"), List.of("dairy"), List.of("none"), List.of("gluten"),
				List.of("gluten", "dairy"), List.of("dairy"));
		String[] categories = { "Main", "Starters", "Dessert", "Drinks", "Starters", "Main", "Dessert", "Drinks",
				"Starters", "Main", "Dessert", "Drinks", "Starters", "Main", "Dessert" };

		for (int i = 0; i < titles.length; i++) {
			Map<String, Object> FoodobjectDetails = new HashMap<>();
			FoodobjectDetails.put("title", titles[i]);
			FoodobjectDetails.put("description", descriptions[i]);
			FoodobjectDetails.put("price", prices[i]);
			FoodobjectDetails.put("imgURL", imgURLs[i]);
			FoodobjectDetails.put("allergens", allergensList.get(i));

			ObjectBoundary foodObject = new ObjectBoundary(new ObjectId(), "MenuItem", categories[i],
					new Location(0.0, 0.0), true, new CreatedBy(userSuper.getUserId()), FoodobjectDetails);

			this.objlogic.storeInDatabase(userSuper.getUserId().getSuperapp(), userSuper.getUserId().getEmail(),
					foodObject);
		}

		Integer[] tableNumbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		String[] tableCapacity = { "4", "4", "4", "5", "5", "4", "2", "2", "4" };

		String[] locationDescs = { "Outside", "Smokers", "Outside", "Outside", "Inside", "Inside", "Inside", "Outside",
				"Smokers", "Smokers" };
		for (int i = 0; i < tableNumbers.length; i++) {
			Map<String, Object> tableObjectDetails = new HashMap<>();
			tableObjectDetails.put("tableNumber", tableNumbers[i]);
			tableObjectDetails.put("locationDesc", locationDescs[i]);
			tableObjectDetails.put("Reservations", null);

			ObjectBoundary tableObject = new ObjectBoundary(new ObjectId(), "Table", tableCapacity[i],
					new Location(32.115139, 34.817804), true, new CreatedBy(userSuper.getUserId()), tableObjectDetails);
			this.objlogic.storeInDatabase(userSuper.getUserId().getSuperapp(), userSuper.getUserId().getEmail(),
					tableObject);
		}

	}

}

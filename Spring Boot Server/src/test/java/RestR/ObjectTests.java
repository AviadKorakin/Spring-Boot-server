package RestR;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.annotation.PostConstruct;
import restR.boundaries.NewUserBoundary;
import restR.boundaries.ObjectBoundary;
import restR.boundaries.UserBoundary;
import restR.entities.RoleEnum;
import restR.general.CreatedBy;
import restR.general.Location;
import restR.general.ObjectId;
import restR.general.UserId;

@SpringBootTest(webEnvironment = WebEnvironment.NONE) 
public class ObjectTests { //TODO redo text stuff 
	private String userUrl;
	private String objUrl;
	private String adminUrl;
	private int port;
	private String superapp;
	private RestClient userRestClient;
	private RestClient objRestClient;
	private RestClient adminRestClient;
	private NewUserBoundary newAdmin;
	UserBoundary admin;

	@PostConstruct
	public void init() {
	    // Print a message to indicate the beginning of the test
	    System.err.println("beginning test");
	    this.superapp = "2024b.aviad.korakin";
	    // Set the base URL for the user-related REST client
	    this.userUrl = "http://localhost:" + port + "/superapp/users";
	    // Set the base URL for the object-related REST client
	    this.objUrl = "http://localhost:" + port + "/superapp/objects";
	    // Set the base URL for the admin-related REST client
	    this.adminUrl = "http://localhost:" + port +"/superapp/admin";
	    // Create and initialize the restClient for user operations
	    this.userRestClient = RestClient.create(userUrl);
	    // Create and initialize the restClient for object operations
	    this.objRestClient = RestClient.create(objUrl);
	    // Create and initialize the adminClient for admin operations
	    this.adminRestClient = RestClient.create(adminUrl);
	    
	    // Create a new admin user boundary object with predefined properties
		newAdmin = new NewUserBoundary("admin@admin.admin", RoleEnum.ADMIN, "admin", "admin");
	}
	
	// This method is annotated with @BeforeEach, meaning it will be executed before each test method 
		// in the current test class. It's used to set up the test environment.
		@BeforeEach
		public void setup() {
			// Print a message to indicate the setup phase
			System.err.println("set up");

			// Create the admin user in the database by sending a POST request to the user
			// endpoint
			admin = this.userRestClient.post().body(newAdmin).retrieve().body(UserBoundary.class);

			// Delete all users in the database by sending a DELETE request to the admin
			// endpoint
			// The delete request uses query parameters to specify the admin's superapp and
			// email
			
			this.adminRestClient.delete()
			.uri(uriBuilder -> uriBuilder.path("/objects").queryParam("userSuperapp", admin.getUserId().getSuperapp())
					.queryParam("userEmail", admin.getUserId().getEmail()).build())
			.retrieve();
			
			this.adminRestClient.delete()
					.uri(uriBuilder -> uriBuilder.path("/users").queryParam("userSuperapp", admin.getUserId().getSuperapp())
							.queryParam("userEmail", admin.getUserId().getEmail()).build())
					.retrieve();

		}
		
		@AfterEach
		public void tearDown() {
			System.err.println("tear down");
			// DELETE database
			this.adminRestClient.delete()
			.uri(uriBuilder -> uriBuilder.path("/objects").queryParam("userSuperapp", admin.getUserId().getSuperapp())
					.queryParam("userEmail", admin.getUserId().getEmail()).build())
			.retrieve();
		}
		
		@Test
		public void testStoreObjectAdminUser() throws Exception {
			String username = "johndoe";

			// GIVEN a user with Invalid email
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.ADMIN, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			
			
			 Map<String, Object> objectDetails = new HashMap<>();
			 objectDetails.put("name", "John");
			ObjectBoundary newObj = new ObjectBoundary(new ObjectId(superapp,"123"),
					"test","test_alias",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails);

			// THEN storing the user should throw an exception
			assertThrows(HttpClientErrorException.Unauthorized.class, () -> {
				this.objRestClient.post().uri(uriBuilder -> uriBuilder
						.queryParam("userSuperapp", user.getUserId().getSuperapp())
						.queryParam("userEmail", user.getUserId().getEmail()).build())
				.body(newObj).retrieve().body(ObjectBoundary.class);
			});
		}
		
		@Test
		public void testStoreObjectMiniAppUser() throws Exception {
			String username = "johndoe";

			// GIVEN a user with Invalid email
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.MINIAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			
			
			 Map<String, Object> objectDetails = new HashMap<>();
			 objectDetails.put("name", "John");
			ObjectBoundary newObj = new ObjectBoundary(new ObjectId(superapp,"123"),
					"test","test_alias",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails);

			// THEN storing the user should throw an exception
			assertThrows(HttpClientErrorException.Unauthorized.class, () -> {
				this.objRestClient.post().uri(uriBuilder -> uriBuilder
						.queryParam("userSuperapp", user.getUserId().getSuperapp())
						.queryParam("userEmail", user.getUserId().getEmail()).build())
				.body(newObj).retrieve().body(ObjectBoundary.class);
			});
		}
		
		
		
		@Test
		public void testStoreObjectSuperAppUser() { 
			String username = "johndoe";

			// GIVEN a user with Invalid email
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			
			
			 Map<String, Object> objectDetails = new HashMap<>();
			 objectDetails.put("name", "John");
			ObjectBoundary newObj = new ObjectBoundary(new ObjectId(superapp,"123"),
					"test","test_alias",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails);

		
			// THEN storing the user should throw an exception
			assertThat(
				this.objRestClient.post().uri(uriBuilder -> uriBuilder
						.queryParam("userSuperapp", user.getUserId().getSuperapp())
						.queryParam("userEmail", user.getUserId().getEmail()).build())
				.body(newObj).retrieve().body(ObjectBoundary.class));
				
		}
		
		
		@Test
		public void testStoreObjectNullDetails() throws Exception {
			String username = "johndoe";

			// GIVEN a user with Invalid email
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			
			
			 Map<String, Object> objectDetails = new HashMap<>();
			ObjectBoundary newObj = new ObjectBoundary(new ObjectId(superapp,"123"),
					"test","test_alias",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails);

			// THEN storing the user should throw an exception
			assertThrows(HttpClientErrorException.BadRequest.class, () -> {
				this.objRestClient.post().uri(uriBuilder -> uriBuilder
						.queryParam("userSuperapp", user.getUserId().getSuperapp())
						.queryParam("userEmail", user.getUserId().getEmail()).build())
				.body(newObj).retrieve().body(ObjectBoundary.class);
			});
		}
		
		
		
		@Test
		public void testStoreObjectNullType() throws Exception {
			String username = "johndoe";

			// GIVEN a user with Invalid email
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			
			
			 Map<String, Object> objectDetails = new HashMap<>();
			 objectDetails.put("name", "John");
			ObjectBoundary newObj = new ObjectBoundary(new ObjectId(superapp,"123"),
					"test",null,new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails);

			// THEN storing the user should throw an exception
			assertThrows(HttpClientErrorException.BadRequest.class, () -> {
				this.objRestClient.post().uri(uriBuilder -> uriBuilder
						.queryParam("userSuperapp", user.getUserId().getSuperapp())
						.queryParam("userEmail", user.getUserId().getEmail()).build())
				.body(newObj).retrieve().body(ObjectBoundary.class);
			});
		}
		
		@Test
		public void testStoreObjectNullAlias() throws Exception {
			String username = "johndoe";

			// GIVEN a user with Invalid email
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			
			
			 Map<String, Object> objectDetails = new HashMap<>();
			 objectDetails.put("name", "John");
			ObjectBoundary newObj = new ObjectBoundary(new ObjectId(superapp,"123"),
					null,"test_alias",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails);

			// THEN storing the user should throw an exception
			assertThatThrownBy(() -> 
				this.objRestClient.post().uri(uriBuilder -> uriBuilder
						.queryParam("userSuperapp", user.getUserId().getSuperapp())
						.queryParam("userEmail", user.getUserId().getEmail()).build())
				.body(newObj).retrieve().toBodilessEntity())
			.isInstanceOf(HttpStatusCodeException.class)
			.extracting("statusCode.value")
			.isEqualTo(400);
		}
		
		@Test
		public void testUpdateAliasobjSuperAPP() throws Exception {
			String username = "johndoe";

			// GIVEN a user with Invalid email
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			
			
			 Map<String, Object> objectDetails = new HashMap<>();
			 objectDetails.put("name", "John");
			ObjectBoundary newObj = new ObjectBoundary(new ObjectId(superapp,"123"),
					"test","test_alias",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails);

			
			ObjectBoundary savedobj = this.objRestClient.post().uri(uriBuilder -> uriBuilder
							.queryParam("userSuperapp", user.getUserId().getSuperapp())
							.queryParam("userEmail", user.getUserId().getEmail()).build())
					.body(newObj).retrieve().body(ObjectBoundary.class);

			
		    String uri = UriComponentsBuilder.fromUriString("/{superapp}/{id}")
		            .queryParam("userSuperapp", user.getUserId().getSuperapp())
		            .queryParam("userEmail", user.getUserId().getEmail())
		            .buildAndExpand(savedobj.getObjectId().getSuperapp(), savedobj.getObjectId().getId())
		            .toUriString();
		    
			savedobj.setAlias(username);
			this.objRestClient.put()
				.uri(uri)
				.body(savedobj)
				.retrieve().body(void.class);
											

			// THEN the updated user should match the changes
			ObjectBoundary updatedObj = this.objRestClient.get()
					.uri(uri)
					.retrieve().body(ObjectBoundary.class);

			assertThat(updatedObj.getAlias()).isEqualTo(username);
		}
		
		
		@Test
		public void testUpdateAliasobjMiniApp() throws Exception {
			String username = "johndoe";

			// GIVEN a user with Invalid email
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			
			NewUserBoundary newUser2 = new NewUserBoundary("bob@bob.com", RoleEnum.MINIAPP_USER, username, "avatar_url");
			UserBoundary user2 = this.userRestClient.post().body(newUser2).retrieve().body(UserBoundary.class);

			 Map<String, Object> objectDetails = new HashMap<>();
			 objectDetails.put("name", "John");
			ObjectBoundary newObj = new ObjectBoundary(new ObjectId(superapp,"123"),
					"test","test_alias",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails);

			
			ObjectBoundary savedobj = this.objRestClient.post().uri(uriBuilder -> uriBuilder
							.queryParam("userSuperapp", user.getUserId().getSuperapp())
							.queryParam("userEmail", user.getUserId().getEmail()).build())
					.body(newObj).retrieve().body(ObjectBoundary.class);

			
		    String uri = UriComponentsBuilder.fromUriString("/{superapp}/{id}")
		            .queryParam("userSuperapp", user2.getUserId().getSuperapp())
		            .queryParam("userEmail", user2.getUserId().getEmail())
		            .buildAndExpand(savedobj.getObjectId().getSuperapp(), savedobj.getObjectId().getId())
		            .toUriString();
		    
			savedobj.setAlias(username);
			assertThatThrownBy(() -> 
			this.objRestClient.put()
				.uri(uri)
				.body(savedobj)
				.retrieve().toBodilessEntity())
				.isInstanceOf(HttpStatusCodeException.class)
				.extracting("statusCode.value")
				.isEqualTo(401);
										
		}
		
		
		@Test
		public void testRetrieveObjectSuperApp() throws Exception {
			String username = "johndoe";

			// GIVEN a user with Invalid email
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			
			
			 Map<String, Object> objectDetails = new HashMap<>();
			 objectDetails.put("name", "John");
			ObjectBoundary newObj = new ObjectBoundary(new ObjectId(superapp,"123"),
					"test","test_alias",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails);

			// THEN storing the user should throw an exception
			ObjectBoundary savedobj =
				this.objRestClient.post().uri(uriBuilder -> uriBuilder
						.queryParam("userSuperapp", user.getUserId().getSuperapp())
						.queryParam("userEmail", user.getUserId().getEmail()).build())
				.body(newObj).retrieve().body(ObjectBoundary.class);
			
		    String uri = UriComponentsBuilder.fromUriString("/{superapp}/{id}")
		            .queryParam("userSuperapp", user.getUserId().getSuperapp())
		            .queryParam("userEmail", user.getUserId().getEmail())
		            .buildAndExpand(savedobj.getObjectId().getSuperapp(), savedobj.getObjectId().getId())
		            .toUriString();
		    
		    assertThat(this.objRestClient.get().uri(uri).retrieve().body(ObjectBoundary.class)).isEqualTo(savedobj);
		    
		}
		
		
		@Test
		public void testRetrieveActiveObjectMiniApp() throws Exception {
			String username = "johndoe";

			// GIVEN a user with Invalid email
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			
			
			 Map<String, Object> objectDetails = new HashMap<>();
			 objectDetails.put("name", "John");
			ObjectBoundary newObj = new ObjectBoundary(new ObjectId(superapp,"123"),
					"test","test_alias",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails);

			// THEN storing the user should throw an exception
			ObjectBoundary savedobj =
				this.objRestClient.post().uri(uriBuilder -> uriBuilder
						.queryParam("userSuperapp", user.getUserId().getSuperapp())
						.queryParam("userEmail", user.getUserId().getEmail()).build())
				.body(newObj).retrieve().body(ObjectBoundary.class);
			
			NewUserBoundary mininewUser = new NewUserBoundary("bob@bob.com", RoleEnum.MINIAPP_USER, username, "avatar_url");
			UserBoundary miniUser = this.userRestClient.post().body(mininewUser).retrieve().body(UserBoundary.class);
			
		    String uri = UriComponentsBuilder.fromUriString("/{superapp}/{id}")
		            .queryParam("userSuperapp", miniUser.getUserId().getSuperapp())
		            .queryParam("userEmail", miniUser.getUserId().getEmail())
		            .buildAndExpand(savedobj.getObjectId().getSuperapp(), savedobj.getObjectId().getId())
		            .toUriString();
		    
		    assertThat(this.objRestClient.get().uri(uri).retrieve().body(ObjectBoundary.class)).isEqualTo(savedobj);
		    
		}
		
		
		@Test
		public void testRetrieveNotActiveObjectMiniApp() throws Exception {
			String username = "johndoe";

			// GIVEN a user with Invalid email
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			
			
			 Map<String, Object> objectDetails = new HashMap<>();
			 objectDetails.put("name", "John");
			ObjectBoundary newObj = new ObjectBoundary(new ObjectId(superapp,"123"),
					"test","test_alias",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails);

			// THEN storing the user should throw an exception
			ObjectBoundary savedobj =
				this.objRestClient.post().uri(uriBuilder -> uriBuilder
						.queryParam("userSuperapp", user.getUserId().getSuperapp())
						.queryParam("userEmail", user.getUserId().getEmail()).build())
				.body(newObj).retrieve().body(ObjectBoundary.class);
			
			NewUserBoundary mininewUser = new NewUserBoundary("bob@bob.com", RoleEnum.MINIAPP_USER, username, "avatar_url");
			UserBoundary miniUser = this.userRestClient.post().body(mininewUser).retrieve().body(UserBoundary.class);
			
		    String uri = UriComponentsBuilder.fromUriString("/{superapp}/{id}")
		            .queryParam("userSuperapp", miniUser.getUserId().getSuperapp())
		            .queryParam("userEmail", miniUser.getUserId().getEmail())
		            .buildAndExpand(savedobj.getObjectId().getSuperapp(), savedobj.getObjectId().getId())
		            .toUriString();
		    
		    
		    assertThatThrownBy(() -> this.objRestClient.get().uri(uri).retrieve().toBodilessEntity()).isInstanceOf(HttpStatusCodeException.class)
		    .extracting("statusCode.value")
		    .isEqualTo(404);
		    
		}
		
		
		@Test
		public void testExportAllObjectsWithPagination(){

			String username = "johndoe";

			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			Map<String, Object> objectDetails = new HashMap<>();
			
			 objectDetails.put("name", "John");
		
			 ObjectBoundary[] newObjects = {
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							"test","test_alias",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
								"test2","test_alias2",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
								"test3","test_alias3",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails)
			};

			// Create users sequentially to avoid any parallel issues
			List<ObjectBoundary> savedObjects = Arrays.stream(newObjects).parallel()
					.map(obj -> this.objRestClient.post().uri(uriBuilder -> uriBuilder
							.queryParam("userSuperapp", user.getUserId().getSuperapp())
							.queryParam("userEmail", user.getUserId().getEmail()).build()).body(obj).retrieve().body(ObjectBoundary.class))
					.collect(Collectors.toList());

			// Define the request parameters
			int size = 10;
			int page = 0;


			// Use URI templates to add request parameters
			ObjectBoundary[] objs = this.objRestClient.get()
					.uri(uriBuilder -> uriBuilder.queryParam("userSuperapp", user.getUserId().getSuperapp())
							.queryParam("userEmail", user.getUserId().getEmail()).queryParam("size", size)
							.queryParam("page", page).build())
					.retrieve().body(ObjectBoundary[].class);

			// THEN the retrieved users should match the saved users
			assertThat(objs).hasSize(3); // 2 users + 1 admin
			assertThat(objs).containsExactlyInAnyOrderElementsOf(savedObjects);
		
		}
		
		
		@Test
		public void testExportAllObjectsWithPaginationMiniAppUser(){

			String username = "johndoe";

			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			Map<String, Object> objectDetails = new HashMap<>();
			
			 objectDetails.put("name", "John");
		
			 ObjectBoundary[] newObjects = {
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							"test","test_alias",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
								"test2","test_alias2",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
								"test3","test_alias3",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails)
			};

			// Create users sequentially to avoid any parallel issues
			List<ObjectBoundary> savedObjects = Arrays.stream(newObjects).parallel()
					.map(obj -> this.objRestClient.post().uri(uriBuilder -> uriBuilder
							.queryParam("userSuperapp", user.getUserId().getSuperapp())
							.queryParam("userEmail", user.getUserId().getEmail()).build()).body(obj).retrieve().body(ObjectBoundary.class))
					.collect(Collectors.toList());

			// Define the request parameters
			int size = 10;
			int page = 0;

			NewUserBoundary newMiniUser = new NewUserBoundary("bob@bob.com", RoleEnum.MINIAPP_USER, username, "avatar_url");
			UserBoundary miniUser = this.userRestClient.post().body(newMiniUser).retrieve().body(UserBoundary.class);
			// Use URI templates to add request parameters
			ObjectBoundary[] objs = this.objRestClient.get()
					.uri(uriBuilder -> uriBuilder.queryParam("userSuperapp", miniUser.getUserId().getSuperapp())
							.queryParam("userEmail", miniUser.getUserId().getEmail()).queryParam("size", size)
							.queryParam("page", page).build())
					.retrieve().body(ObjectBoundary[].class);

			// THEN the retrieved users should match the saved users
			assertThat(objs).hasSize(2); // 2 users + 1 admin
			assertThat(objs).containsExactly(savedObjects.get(1),savedObjects.get(2));
		
		}
		
		@Test
		public void testExportTypeObjectsWithPagination(){

			String username = "johndoe";
			String type = "test";
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			Map<String, Object> objectDetails = new HashMap<>();
			
			 objectDetails.put("name", "John");
		
			 ObjectBoundary[] newObjects = {
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 type,"test_alias",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
								"test2","test_alias2",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 type,"test_alias3",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails)
			};

			// Create users sequentially to avoid any parallel issues
			List<ObjectBoundary> savedObjects = Arrays.stream(newObjects).parallel()
					.map(obj -> this.objRestClient.post().uri(uriBuilder -> uriBuilder
							.queryParam("userSuperapp", user.getUserId().getSuperapp())
							.queryParam("userEmail", user.getUserId().getEmail()).build()).body(obj).retrieve().body(ObjectBoundary.class))
					.collect(Collectors.toList());

			// Define the request parameters
			int size = 10;
			int page = 0;


		    String uri = UriComponentsBuilder.fromUriString("/search/ByType/{type}")
		            .queryParam("userSuperapp", user.getUserId().getSuperapp())
		            .queryParam("userEmail", user.getUserId().getEmail())
		            .queryParam("size", size)
					.queryParam("page", page)
					.buildAndExpand(type)
		            .toUriString();
		    
			// Use URI templates to add request parameters
			ObjectBoundary[] objs = this.objRestClient.get()
					.uri(uri)
					.retrieve().body(ObjectBoundary[].class);

			// THEN the retrieved users should match the saved users
			assertThat(objs).hasSize(2); // 2 users + 1 admin
			assertThat(objs).containsExactlyInAnyOrder(savedObjects.get(0),savedObjects.get(2));
		
		}
		
		
		@Test
		public void testExportTypeObjectsWithPaginationMiniApp(){

			String username = "johndoe";
			String type = "test";
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			Map<String, Object> objectDetails = new HashMap<>();
			
			 objectDetails.put("name", "John");
		
			 ObjectBoundary[] newObjects = {
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 type,"test_alias",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
								"test2","test_alias2",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 type,"test_alias3",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails),
					
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 type,"test_alias4",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails)
			};

			// Create users sequentially to avoid any parallel issues
			List<ObjectBoundary> savedObjects = Arrays.stream(newObjects).parallel()
					.map(obj -> this.objRestClient.post().uri(uriBuilder -> uriBuilder
							.queryParam("userSuperapp", user.getUserId().getSuperapp())
							.queryParam("userEmail", user.getUserId().getEmail()).build()).body(obj).retrieve().body(ObjectBoundary.class))
					.collect(Collectors.toList());

			// Define the request parameters
			int size = 10;
			int page = 0;

			NewUserBoundary newMiniUser = new NewUserBoundary("bob@bob.com", RoleEnum.MINIAPP_USER, username, "avatar_url");
			UserBoundary miniUser = this.userRestClient.post().body(newMiniUser).retrieve().body(UserBoundary.class);

			String uri = UriComponentsBuilder.fromUriString("/search/ByType/{type}")
		            .queryParam("userSuperapp", miniUser.getUserId().getSuperapp())
		            .queryParam("userEmail", miniUser.getUserId().getEmail())
		            .queryParam("size", size)
					.queryParam("page", page)
					.buildAndExpand(type)
		            .toUriString();
		    
			// Use URI templates to add request parameters
			ObjectBoundary[] objs = this.objRestClient.get()
					.uri(uri)
					.retrieve().body(ObjectBoundary[].class);

			// THEN the retrieved users should match the saved users
			assertThat(objs).hasSize(2); // 2 users + 1 admin
			assertThat(objs).containsExactlyInAnyOrder(savedObjects.get(2),savedObjects.get(3));
		
		}
		
		
		@Test
		public void testExportAliasObjectsWithPagination(){

			String username = "johndoe";
			String Alias = "aliasliasalis";
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			Map<String, Object> objectDetails = new HashMap<>();
			
			 objectDetails.put("name", "John");
		
			 ObjectBoundary[] newObjects = {
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 "test",Alias,new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
								"test2","test_alias2",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 "test1",Alias,new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails)
			};

			// Create users sequentially to avoid any parallel issues
			List<ObjectBoundary> savedObjects = Arrays.stream(newObjects).parallel()
					.map(obj -> this.objRestClient.post().uri(uriBuilder -> uriBuilder
							.queryParam("userSuperapp", user.getUserId().getSuperapp())
							.queryParam("userEmail", user.getUserId().getEmail()).build()).body(obj).retrieve().body(ObjectBoundary.class))
					.collect(Collectors.toList());

			// Define the request parameters
			int size = 10;
			int page = 0;


		    String uri = UriComponentsBuilder.fromUriString("/search/ByAlias/{alias}")
		            .queryParam("userSuperapp", user.getUserId().getSuperapp())
		            .queryParam("userEmail", user.getUserId().getEmail())
		            .queryParam("size", size)
					.queryParam("page", page)
					.buildAndExpand(Alias)
		            .toUriString();
		    
			// Use URI templates to add request parameters
			ObjectBoundary[] objs = this.objRestClient.get()
					.uri(uri)
					.retrieve().body(ObjectBoundary[].class);

			// THEN the retrieved users should match the saved users
			assertThat(objs).hasSize(2); // 2 users + 1 admin
			assertThat(objs).containsExactlyInAnyOrder(savedObjects.get(0),savedObjects.get(2));
		
		}
		
		
		@Test
		public void testExportAliasObjectsWithPaginationMiniApp(){

			String username = "johndoe";
			String Alias = "bobobobob";
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			Map<String, Object> objectDetails = new HashMap<>();
			
			 objectDetails.put("name", "John");
		
			 ObjectBoundary[] newObjects = {
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 "test",Alias,new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
								"test2","test_alias2",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 "test3",Alias,new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails),
					
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 "test4",Alias,new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails)
			};

			// Create users sequentially to avoid any parallel issues
			List<ObjectBoundary> savedObjects = Arrays.stream(newObjects).parallel()
					.map(obj -> this.objRestClient.post().uri(uriBuilder -> uriBuilder
							.queryParam("userSuperapp", user.getUserId().getSuperapp())
							.queryParam("userEmail", user.getUserId().getEmail()).build()).body(obj).retrieve().body(ObjectBoundary.class))
					.collect(Collectors.toList());

			// Define the request parameters
			int size = 10;
			int page = 0;

			NewUserBoundary newMiniUser = new NewUserBoundary("bob@bob.com", RoleEnum.MINIAPP_USER, username, "avatar_url");
			UserBoundary miniUser = this.userRestClient.post().body(newMiniUser).retrieve().body(UserBoundary.class);

			String uri = UriComponentsBuilder.fromUriString("/search/ByAlias/{alias}")
		            .queryParam("userSuperapp", miniUser.getUserId().getSuperapp())
		            .queryParam("userEmail", miniUser.getUserId().getEmail())
		            .queryParam("size", size)
					.queryParam("page", page)
					.buildAndExpand(Alias)
		            .toUriString();
		    
			// Use URI templates to add request parameters
			ObjectBoundary[] objs = this.objRestClient.get()
					.uri(uri)
					.retrieve().body(ObjectBoundary[].class);

			// THEN the retrieved users should match the saved users
			assertThat(objs).hasSize(2); // 2 users + 1 admin
			assertThat(objs).containsExactlyInAnyOrder(savedObjects.get(2),savedObjects.get(3));
		
		}
		
		
		@Test
		public void testExportPatternObjectsWithPagination(){

			String username = "johndoe";
			String Pattern = "tst";
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			Map<String, Object> objectDetails = new HashMap<>();
			
			 objectDetails.put("name", "John");
		
			 ObjectBoundary[] newObjects = {
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 "test","tst",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
								"test2","t_st",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 "test1","tststs",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails)
			};

			// Create users sequentially to avoid any parallel issues
			List<ObjectBoundary> savedObjects = Arrays.stream(newObjects).parallel()
					.map(obj -> this.objRestClient.post().uri(uriBuilder -> uriBuilder
							.queryParam("userSuperapp", user.getUserId().getSuperapp())
							.queryParam("userEmail", user.getUserId().getEmail()).build()).body(obj).retrieve().body(ObjectBoundary.class))
					.collect(Collectors.toList());

			// Define the request parameters
			int size = 10;
			int page = 0;


		    String uri = UriComponentsBuilder.fromUriString("/search/ByAliasPattern/{pattern}")
		            .queryParam("userSuperapp", user.getUserId().getSuperapp())
		            .queryParam("userEmail", user.getUserId().getEmail())
		            .queryParam("size", size)
					.queryParam("page", page)
					.buildAndExpand(Pattern)
		            .toUriString();
		    
			// Use URI templates to add request parameters
			ObjectBoundary[] objs = this.objRestClient.get()
					.uri(uri)
					.retrieve().body(ObjectBoundary[].class);

			// THEN the retrieved users should match the saved users
			assertThat(objs).hasSize(2); // 2 users + 1 admin
			assertThat(objs).containsExactlyInAnyOrder(savedObjects.get(0),savedObjects.get(2));
		
		}
		
		
		@Test
		public void testExportPatternObjectsWithPaginationMiniApp(){

			String username = "johndoe";
			String Pattern = "pat";
			NewUserBoundary newUser = new NewUserBoundary("johndoe@johndoe.com", RoleEnum.SUPERAPP_USER, username, "avatar_url");
			UserBoundary user = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
			Map<String, Object> objectDetails = new HashMap<>();
			
			 objectDetails.put("name", "John");
		
			 ObjectBoundary[] newObjects = {
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 "test","pattern",new Location(0.0, 0.0),false,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
								"test2","pattern",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails),
					 
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 "test3","past",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails),
					
					 new ObjectBoundary(new ObjectId(superapp,"123"),
							 "test4","pats",new Location(0.0, 0.0),true,new CreatedBy(user.getUserId()),objectDetails)
			};

			// Create users sequentially to avoid any parallel issues
			List<ObjectBoundary> savedObjects = Arrays.stream(newObjects).parallel()
					.map(obj -> this.objRestClient.post().uri(uriBuilder -> uriBuilder
							.queryParam("userSuperapp", user.getUserId().getSuperapp())
							.queryParam("userEmail", user.getUserId().getEmail()).build()).body(obj).retrieve().body(ObjectBoundary.class))
					.collect(Collectors.toList());

			// Define the request parameters
			int size = 10;
			int page = 0;

			NewUserBoundary newMiniUser = new NewUserBoundary("bob@bob.com", RoleEnum.MINIAPP_USER, username, "avatar_url");
			UserBoundary miniUser = this.userRestClient.post().body(newMiniUser).retrieve().body(UserBoundary.class);

			String uri = UriComponentsBuilder.fromUriString("/search/ByAliasPattern/{pattern}")
		            .queryParam("userSuperapp", miniUser.getUserId().getSuperapp())
		            .queryParam("userEmail", miniUser.getUserId().getEmail())
		            .queryParam("size", size)
					.queryParam("page", page)
					.buildAndExpand(Pattern)
		            .toUriString();
		    
			// Use URI templates to add request parameters
			ObjectBoundary[] objs = this.objRestClient.get()
					.uri(uri)
					.retrieve().body(ObjectBoundary[].class);

			// THEN the retrieved users should match the saved users
			assertThat(objs).hasSize(2); // 2 users + 1 admin
			assertThat(objs).containsExactlyInAnyOrder(savedObjects.get(1),savedObjects.get(3));
		
		}
		
		
		
		
		
		
		
		
		
		@Value("${server.port:8080}")
		public void setPort(int port) {
			this.port = port;
		}

}

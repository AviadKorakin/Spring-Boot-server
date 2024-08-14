package RestR;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import jakarta.annotation.PostConstruct;
import restR.boundaries.NewUserBoundary;
import restR.boundaries.UserBoundary;
import restR.entities.RoleEnum;


@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class UserTests {
	private String userUrl;
	private String adminUrl;
	private int port;
	private RestClient userRestClient;
	private RestClient adminRestClient;
	private NewUserBoundary newAdmin;

	@PostConstruct
	public void init() {
	    // Print a message to indicate the beginning of the test
	    System.err.println("beginning test");
	    
	    // Set the base URL for the user-related REST client
	    this.userUrl = "http://localhost:" + port + "/superapp/users";
	    // Set the base URL for the admin-related REST client
	    this.adminUrl = "http://localhost:" + port +"/superapp/admin";
	    // Create and initialize the restClient for user operations
	    this.userRestClient = RestClient.create(userUrl);
	    
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
	    
	    // Create the admin user in the database by sending a POST request to the user endpoint
	    UserBoundary admin = this.userRestClient.post().body(newAdmin).retrieve().body(UserBoundary.class);
	    
	    // Delete all users in the database by sending a DELETE request to the admin endpoint
	    // The delete request uses query parameters to specify the admin's superapp and email
	    this.adminRestClient.delete()
	            .uri(uriBuilder -> uriBuilder
	                    .path("/users")
	                    .queryParam("userSuperapp", admin.getUserId().getSuperapp())
	                    .queryParam("userEmail", admin.getUserId().getEmail())
	                    .build())
	            .retrieve();
	}

	@Test
	public void testGetSpecificUser() throws Exception {
		String username = "johndoe";

		NewUserBoundary newUser = new NewUserBoundary("john.doe@example.com", RoleEnum.SUPERAPP_USER, username,
				"avatar_url");
		UserBoundary savedUser = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);

		// WHEN retrieving the specific user
		UserBoundary retrievedUser = this.userRestClient.get()
				.uri("/login/{superapp}/{email}", savedUser.getUserId().getSuperapp(), savedUser.getUserId().getEmail())
				.retrieve().body(UserBoundary.class);

		// THEN the retrieved user should match the saved user
		assertThat(retrievedUser).usingRecursiveComparison().isEqualTo(savedUser);
	}

	@Test
	public void testUpdateUsernameUser() throws Exception {

		String username = "johndoe";

		NewUserBoundary newUser = new NewUserBoundary("john.doe@example.com", RoleEnum.SUPERAPP_USER, username,
				"avatar_url");

		UserBoundary savedUser = this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);

		// WHEN updating the user
		savedUser.setUsername(username);
		this.userRestClient.put()
				.uri("/{superapp}/{userEmail}", savedUser.getUserId().getSuperapp(), savedUser.getUserId().getEmail())
				.body(savedUser).retrieve().body(Void.class);

		// THEN the updated user should match the changes
		UserBoundary updatedUser = this.userRestClient.get()
				.uri("/login/{superapp}/{email}", savedUser.getUserId().getSuperapp(), savedUser.getUserId().getEmail())
				.retrieve().body(UserBoundary.class);

		assertThat(updatedUser.getUsername()).isEqualTo(username);
	}

	@Test
	public void testStoreInvalidUser() throws Exception {
		String username = "johndoe";

		// GIVEN a user with Invalid email
		NewUserBoundary newUser = new NewUserBoundary("invalid-email", RoleEnum.SUPERAPP_USER, username, "avatar_url");

		// THEN storing the user should throw an exception
		assertThrows(HttpClientErrorException.BadRequest.class, () -> {
			this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);
		});
	}

	@Test
	public void testCreateUserWithInvalidRole() {
		// GIVEN an invalid user role
		NewUserBoundary invalidUser = new NewUserBoundary("invalid@example.com", null, "invaliduser", "avatar_url");

		// WHEN we attempt to create the user
		// THEN the server should respond with a bad request status
		assertThrows(HttpClientErrorException.BadRequest.class, () -> {
			this.userRestClient.post().body(invalidUser).retrieve().body(UserBoundary.class);
		});
	}

	@Test
	public void testCreateUserWithInvalidRoleasMap() throws Exception {
		// GIVEN an invalid user role
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("email", "aviad11@gmail.com");
		requestBody.put("role", "asdsad");
		requestBody.put("username", "qqq");
		requestBody.put("avatar", "adsqweqq");
		
		// WHEN we attempt to create the user
		// THEN the server should respond with a bad request status
		assertThrows(HttpClientErrorException.BadRequest.class, () -> {
			this.userRestClient.post().body(requestBody).retrieve().body(UserBoundary.class);
		});
	}

	@Test
	public void testCreateUserWithDuplicateEmail() {
		// GIVEN a user with a specific email
		NewUserBoundary newUser = new NewUserBoundary("duplicate@example.com", RoleEnum.SUPERAPP_USER, "user",
				"avatar_url");
		this.userRestClient.post().body(newUser).retrieve().body(UserBoundary.class);

		// WHEN creating another user with the same email
		NewUserBoundary duplicateUser = new NewUserBoundary("duplicate@example.com", RoleEnum.SUPERAPP_USER, "user2",
				"avatar_url2");

		// THEN the server should respond with a conflict status
		assertThrows(HttpClientErrorException.Conflict.class, () -> {
			this.userRestClient.post().body(duplicateUser).retrieve().body(UserBoundary.class);
		});
	}

	@Test
	public void testRetrieveNonExistentUser() {
		// GIVEN a non-existent user
		String nonExistentSuperapp = "nonexistent";
		String nonExistentEmail = "nonexistent@example.com";

		// WHEN retrieving the user
		// THEN the server should respond with a not found status
		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.userRestClient.get().uri("/login/{superapp}/{email}", nonExistentSuperapp, nonExistentEmail).retrieve()
					.body(UserBoundary.class);
		});
	}

	public void testCreateUserWithEmptyFields() {
		// GIVEN a user with empty fields
		NewUserBoundary newUser = new NewUserBoundary("", RoleEnum.SUPERAPP_USER, "", "");

		// WHEN creating the user
		// THEN the server should respond with a bad request status
		assertThrows(HttpClientErrorException.BadRequest.class, () -> {
			this.userRestClient.post().uri("/superapp/users").body(newUser).retrieve().body(UserBoundary.class);
		});
	}

	@Test
	public void testUpdateNonExistentUser() {
		// GIVEN a non-existent user
		String nonExistentSuperapp = "nonexistent";
		String nonExistentEmail = "nonexistent@example.com";
		UserBoundary update = new UserBoundary();
		update.setUsername("newUsername");

		// WHEN updating the user
		// THEN the server should respond with a not found status
		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.userRestClient.put().uri("/{superapp}/{userEmail}", nonExistentSuperapp, nonExistentEmail).body(update)
					.retrieve().body(Void.class);
		});
	}

	@Test
	public void testConcurrentUserCreation() {
		// GIVEN multiple users
		NewUserBoundary[] newUsers = {
				new NewUserBoundary("user1@example.com", RoleEnum.SUPERAPP_USER, "user1", "avatar_url1"),
				new NewUserBoundary("user2@example.com", RoleEnum.SUPERAPP_USER, "user2", "avatar_url2"), };

		// WHEN creating users concurrently
		UserBoundary[] savedUsers = Arrays.stream(newUsers).parallel()
				.map(user -> this.userRestClient.post().body(user).retrieve().body(UserBoundary.class))
				.toArray(UserBoundary[]::new);

		// THEN all users should be created successfully
		assertThat(savedUsers).hasSize(newUsers.length);
	}

	@Value("${server.port:8080}")
	public void setPort(int port) {
		this.port = port;
	}

}

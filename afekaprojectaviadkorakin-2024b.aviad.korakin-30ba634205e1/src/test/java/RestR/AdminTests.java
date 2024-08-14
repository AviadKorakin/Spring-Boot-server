package RestR;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

import jakarta.annotation.PostConstruct;
import restR.boundaries.NewUserBoundary;
import restR.boundaries.UserBoundary;
import restR.entities.RoleEnum;


@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class AdminTests {
	private String userUrl;
	private String adminUrl;
	private String objectUrl;
	private int port;
	private RestClient userRestClient;
	private RestClient adminRestClient;
	private RestClient objectRestClient;
	private NewUserBoundary newAdmin;

	@PostConstruct
	public void init() {
		// Print a message to indicate the beginning of the test
		System.err.println("beginning test");

		// Set the base URL for the user-related REST client
		this.userUrl = "http://localhost:" + port + "/superapp/users";
		// Set the base URL for the admin-related REST client
		this.adminUrl = "http://localhost:" + port + "/superapp/admin";
		// Set the base URL for the admin-related REST client
		this.objectUrl = "http://localhost:" + port + "/superapp/objects";
		// Create and initialize the restClient for user operations
		this.userRestClient = RestClient.create(userUrl);

		// Create and initialize the adminClient for admin operations
		this.adminRestClient = RestClient.create(adminUrl);
		// Create and initialize the objectClient for object operations
		this.objectRestClient = RestClient.create(objectUrl);
		// Create a new admin user boundary object with predefined properties
		newAdmin = new NewUserBoundary("admin@admin.admin", RoleEnum.ADMIN, "admin", "admin");
	}

	// This method is annotated with @BeforeEach, meaning it will be executed before
	// each test method
	// in the current test class. It's used to set up the test environment.
	@BeforeEach
	public void setup() {
		// Print a message to indicate the setup phase
		System.err.println("set up");

		// Create the admin user in the database by sending a POST request to the user
		// endpoint
		UserBoundary admin = this.userRestClient.post().body(newAdmin).retrieve().body(UserBoundary.class);

		// Delete all users in the database by sending a DELETE request to the admin
		// endpoint
		// The delete request uses query parameters to specify the admin's superapp and
		// email
		this.adminRestClient.delete()
				.uri(uriBuilder -> uriBuilder.path("/users").queryParam("userSuperapp", admin.getUserId().getSuperapp())
						.queryParam("userEmail", admin.getUserId().getEmail()).build())
				.retrieve();
	}

	@Test
	public void testExportAllUsersWithPagination() throws Exception {
		NewUserBoundary[] newUsers = {
				new NewUserBoundary("user1@example.com", RoleEnum.SUPERAPP_USER, "user1", "avatar_url1"),
				new NewUserBoundary("user2@example.com", RoleEnum.SUPERAPP_USER, "user2", "avatar_url2") };
		NewUserBoundary newAdmin = new NewUserBoundary("admin@example.com", RoleEnum.ADMIN, "admin",
				"avatar_url_admin");

		// Create users sequentially to avoid any parallel issues
		List<UserBoundary> savedUsers = Arrays.stream(newUsers).parallel()
				.map(user -> this.userRestClient.post().body(user).retrieve().body(UserBoundary.class))
				.collect(Collectors.toList());

		// Create the admin user
		UserBoundary admin = this.userRestClient.post().body(newAdmin).retrieve().body(UserBoundary.class);

		// Add the admin user to the list of saved users
		savedUsers.add(0, admin);

		// Define the request parameters
		int size = 10;
		int page = 0;

		// Use URI templates to add request parameters
		UserBoundary[] users = this.adminRestClient.get()
				.uri(uriBuilder -> uriBuilder.path("/users").queryParam("userSuperapp", admin.getUserId().getSuperapp())
						.queryParam("userEmail", admin.getUserId().getEmail()).queryParam("size", size)
						.queryParam("page", page).build())
				.retrieve().body(UserBoundary[].class);

		// THEN the retrieved users should match the saved users
		assertThat(users).hasSize(3); // 2 users + 1 admin
		assertThat(users).containsExactlyInAnyOrderElementsOf(savedUsers);
	}

	@Test
	public void testCreateMultipleUsersAndVerifyPagination() throws Exception {
		// GIVEN multiple users
		NewUserBoundary[] newUsers = {
				new NewUserBoundary("user1@example.com", RoleEnum.SUPERAPP_USER, "user1", "avatar_url1"),
				new NewUserBoundary("user2@example.com", RoleEnum.SUPERAPP_USER, "user2", "avatar_url2"),
				new NewUserBoundary("user3@example.com", RoleEnum.SUPERAPP_USER, "user3", "avatar_url3"),
				new NewUserBoundary("user4@example.com", RoleEnum.SUPERAPP_USER, "user4", "avatar_url4") };
		NewUserBoundary newUserAdmin = new NewUserBoundary("admin@example.com", RoleEnum.ADMIN, "admin",
				"avatar_url_admin");
		// WHEN creating users concurrently
		Arrays.stream(newUsers).parallel()
				.map(user -> this.userRestClient.post().body(user).retrieve().body(UserBoundary.class));

		UserBoundary admin = this.userRestClient.post().body(newUserAdmin).retrieve().body(UserBoundary.class);
		// WHEN retrieving users with pagination
		int size = 2;
		int page = 0;
		UserBoundary[] firstPageUsers = this.adminRestClient.get()
				.uri(uriBuilder -> uriBuilder.path("/users").queryParam("userSuperapp", admin.getUserId().getSuperapp())
						.queryParam("userEmail", admin.getUserId().getEmail()).queryParam("size", size)
						.queryParam("page", page).build())
				.retrieve().body(UserBoundary[].class);
		// THEN its should return page with 2 users.
		assertThat(firstPageUsers).hasSize(2);
	}

	@Test
	public void testDeleteAllUsers() throws Exception {
		NewUserBoundary[] newUsers = {
				new NewUserBoundary("user1@example.com", RoleEnum.SUPERAPP_USER, "user1", "avatar_url1"),
				new NewUserBoundary("user2@example.com", RoleEnum.SUPERAPP_USER, "user2", "avatar_url2") };
		NewUserBoundary newUserAdmin = new NewUserBoundary("admin@example.com", RoleEnum.ADMIN, "admin",
				"avatar_url_admin");
		
		
		Arrays.stream(newUsers).parallel()
		.map(user -> this.userRestClient.post().body(user).retrieve().body(UserBoundary.class));

		UserBoundary admin = this.userRestClient.post().body(newUserAdmin).retrieve().body(UserBoundary.class);

		// Use admin client to delete all
		 assertThatCode(() -> {
	            this.adminRestClient.delete()
	                .uri(uriBuilder -> uriBuilder.path("/superapp/admin/users")
	                        .queryParam("userSuperapp", admin.getUserId().getSuperapp())
	                        .queryParam("userEmail", admin.getUserId().getEmail()).build())
	                .retrieve();
	        }).doesNotThrowAnyException();
	}

	@Value("${server.port:8080}")
	public void setPort(int port) {
		this.port = port;
	}

}

package RestR;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.client.RestClient;
import jakarta.annotation.PostConstruct;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ApplicationTests {
	private String url;
	private int port;
	private RestClient restClient;
	

	@PostConstruct
	public void init() {
		System.err.println("beginning test");
		this.url = "http://localhost:" + port + "/demo";
		this.restClient = RestClient.create(url);
	}
	
//	@BeforeEach
	public void setup() {
		System.err.println("set up");
		// DELETE database
		this.restClient
			.delete()
			.retrieve();
	}
	
	@AfterEach
	public void tearDown() {
		System.err.println("tear down");
		// DELETE database
		this.restClient
			.delete()
			.retrieve();
	}
	
	@Test
	public void contextLoads() {
	}
	
	/*@Test
	public void testStoreMessageWithValueHelloToDatabase() throws Exception {
		// GIVEN the server is up
		//    AND the database is empty
		
		// WHEN I POST /demo {"message":"hello"}
		String message = "hello";
		DemoBoundary newDemo = new DemoBoundary(message);
		newDemo = this.restClient
			.post()
			.body(newDemo)
			.retrieve()
			.body(DemoBoundary.class);
		
		// THEN the message is store to the database
		//   AND the server responds with status 2xx
		assertThat(this.restClient
			.get()
			.uri("/{id}", newDemo.getId())
			.retrieve()
			.body(DemoBoundary.class)
			.getMessage())
			.isEqualTo(message);
	}

	@Test
	public void testStoreMessageWithValueHelloToDatabaseWithExistingInfo() throws Exception{
		// GIVEN the server is up
		//    AND the database contains another message with the message "hello"
		this.restClient
			.post()
			.body(Collections.singletonMap("message", "hello"))
			.retrieve()
			.body(DemoBoundary.class);
		
		
		// WHEN I POST /demo {"message":"hello"}
		DemoBoundary newObject = this.restClient
		.post()
		.body(Collections.singletonMap("message", "hello"))
		.retrieve()
		.body(DemoBoundary.class);
		
		// THEN the message is store to the database
		//   AND the server responds with status 2xx
		assertThat(this.restClient
			.get()
			.uri("/{id}", newObject.getId())
			.retrieve()
			.body(DemoBoundary.class))
			.usingRecursiveComparison()
			.isEqualTo(newObject);
	}
	
	@Test
	public void testDatabaseIsEmptyOnInitialization() throws Exception {
		// GIVEN the server is up
		
		// WHEN I GET /demo?page=0&size=5
		// THEN the server responds with empty array
		// AND the response status is 2xx 
		assertThat(this.restClient
			.get()
			.uri("?page=0&size={size}", 5)
			.retrieve()
			.body(DemoBoundary[].class))
			.isNotNull()
			.isEmpty();
		
	}
	*/
	
	@Value("${server.port:8080}")
	public void setPort(int port) {
		this.port = port;
	}
	

}
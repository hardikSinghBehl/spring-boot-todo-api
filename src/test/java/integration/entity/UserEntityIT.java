package integration.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import com.hardik.donatello.SpringBootTodoApiApplication;
import com.hardik.donatello.entity.User;
import com.hardik.donatello.repository.UserRepository;

import net.bytebuddy.utility.RandomString;

@SpringBootTest(classes = SpringBootTodoApiApplication.class)
public class UserEntityIT {

	private static final String FIRST_NAME = "Hardik";

	private static final String LAST_NAME = "Behl";

	private static final String EMAIL_ID = "hardikIsTheGreatest@gmail.com";

	private static final String PASSWORD = "Something-Strong-Password-123";

	public static PostgreSQLContainer container;

	@Autowired
	private UserRepository userRepository;

	private List<UUID> usersToBeDeleted;

	static {
		container = (PostgreSQLContainer) new PostgreSQLContainer().withUsername("donatello").withPassword("donatello")
				.withDatabaseName("donatello-test-db").withReuse(true);
		container.start();
	}

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", container::getJdbcUrl);
		registry.add("spring.datasource.password", container::getPassword);
		registry.add("spring.datasource.username", container::getUsername);
	}

	@BeforeEach
	void setUp() {
		usersToBeDeleted = new ArrayList<UUID>();
	}

	@AfterEach
	void tearDown() {
		usersToBeDeleted.forEach(userId -> userRepository.deleteById(userId));
	}

	@Test
	@DisplayName("New User Record Gets Stored To Database Successfully When All Values Are Provided")
	void userStoredToDatabaseSuccessfully() {
		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		assertNotNull(savedUser);
		assertNotNull(savedUser.getId());
		assertThat(savedUser.getId()).isInstanceOf(UUID.class);
		assertEquals(1, userRepository.count());

		final var retreivedUser = userRepository.findById(savedUser.getId()).get();

		assertEquals(EMAIL_ID, retreivedUser.getEmail());
		assertEquals(FIRST_NAME, retreivedUser.getFirstName());
		assertEquals(LAST_NAME, retreivedUser.getLastName());
		assertEquals(PASSWORD, retreivedUser.getPassword());

		usersToBeDeleted.add(retreivedUser.getId());

	}

	@Test
	@DisplayName("Exception Thrown When User With Existing Email Id Is Attempted To Be Stored")
	void exceptionThrownWhenUserWithExistingEmailIdIsAttemptedToBeStored() {
		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		final var userWithSameEmailId = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(RandomString.make(10));
		user.setLastName(RandomString.make(10));
		user.setPassword(RandomString.make(20));

		assertThrows(Exception.class, () -> userRepository.save(userWithSameEmailId));

		usersToBeDeleted.add(savedUser.getId());
	}

	@Nested
	@DisplayName("User Log In Attempt")
	class userLoginAttempt {

		@Test
		@DisplayName("When Correct Credentials Are Provided")
		void userLoginAttemptWithCorrectCredentials() {
			final var user = new User();
			user.setEmail(EMAIL_ID);
			user.setFirstName(FIRST_NAME);
			user.setLastName(LAST_NAME);
			user.setPassword(PASSWORD);
			final var savedUser = userRepository.save(user);

			final var retreivedUser = userRepository.findByEmailAndPassword(EMAIL_ID, PASSWORD);

			assertTrue(retreivedUser.isPresent());

			usersToBeDeleted.add(savedUser.getId());
		}

		@Test
		@DisplayName("When InCorrect Credentials Are Provided")
		void userLoginAttemptWithInCorrectCredentials() {
			final var user = new User();
			user.setEmail(EMAIL_ID);
			user.setFirstName(FIRST_NAME);
			user.setLastName(LAST_NAME);
			user.setPassword(PASSWORD);
			final var savedUser = userRepository.save(user);

			final var retreivedUser = userRepository.findByEmailAndPassword(EMAIL_ID, RandomString.make(10));

			assertTrue(retreivedUser.isEmpty());

			usersToBeDeleted.add(savedUser.getId());
		}
	}

	@Test
	@DisplayName("Fields Of A User Record gets Updated Successfully")
	void fieldsGetUpdatedSuccessfullyOnUserUpdation() {
		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		final var retreivedUser = userRepository.findById(savedUser.getId()).get();

		retreivedUser.setFirstName(FIRST_NAME + "CHANGE");
		retreivedUser.setLastName(LAST_NAME + "CHANGE");
		retreivedUser.setPassword(RandomString.make(25));

		final var updatedUser = userRepository.save(retreivedUser);

		assertNotEquals(savedUser.getFirstName(), updatedUser.getFirstName());
		assertNotEquals(savedUser.getLastName(), updatedUser.getLastName());
		assertNotEquals(savedUser.getPassword(), updatedUser.getPassword());

		usersToBeDeleted.add(retreivedUser.getId());
	}

	@Test
	@DisplayName("created_at, updated_at and ids are automatically assigned to the saved user record")
	void requiredFieldsAreAssignedToPersistedObjectAutomatically() {
		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		assertNotNull(savedUser);
		assertNotNull(savedUser.getId());
		assertThat(savedUser.getId()).isInstanceOf(UUID.class);
		assertNotNull(savedUser.getCreatedAt());
		assertThat(savedUser.getCreatedAt()).isInstanceOf(LocalDateTime.class);
		assertNotNull(savedUser.getUpdatedAt());
		assertThat(savedUser.getUpdatedAt()).isInstanceOf(LocalDateTime.class);

		usersToBeDeleted.add(savedUser.getId());
	}

	@Test
	@DisplayName("updated_at field gets updated automatically when user record is updated")
	void updatedAtIsUpdatedAutomaticallyOnUserUpdation() {
		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		final var initialUpdatedAtValue = savedUser.getUpdatedAt();

		final var retreivedUser = userRepository.findById(savedUser.getId()).get();
		retreivedUser.setPassword(RandomString.make());
		final var updatedUser = userRepository.save(retreivedUser);

		assertNotNull(savedUser);
		assertNotNull(updatedUser);
		assertNotEquals(initialUpdatedAtValue, updatedUser.getUpdatedAt());
		assertTrue(updatedUser.getUpdatedAt().isAfter(initialUpdatedAtValue));

		usersToBeDeleted.add(savedUser.getId());
	}

	@Test
	@DisplayName("created_at and id do not change after user record updation in database")
	void createdAtAndIdsNotReassignedOnUserRecordUpdation() {
		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		final var retreivedUser = userRepository.findById(savedUser.getId()).get();
		retreivedUser.setPassword(RandomString.make());
		final var updatedUser = userRepository.save(retreivedUser);

		assertNotNull(savedUser);
		assertNotNull(updatedUser);
		assertEquals(savedUser.getId(), updatedUser.getId());

		assertAll(() -> assertEquals(savedUser.getCreatedAt().getYear(), updatedUser.getCreatedAt().getYear()),
				() -> assertEquals(savedUser.getCreatedAt().getMonth(), updatedUser.getCreatedAt().getMonth()),
				() -> assertEquals(savedUser.getCreatedAt().getDayOfMonth(),
						updatedUser.getCreatedAt().getDayOfMonth()),
				() -> assertEquals(savedUser.getCreatedAt().getHour(), updatedUser.getCreatedAt().getHour()),
				() -> assertEquals(savedUser.getCreatedAt().getMinute(), updatedUser.getCreatedAt().getMinute()),
				() -> assertEquals(savedUser.getCreatedAt().getSecond(), updatedUser.getCreatedAt().getSecond()));

		usersToBeDeleted.add(savedUser.getId());
	}

	@Test
	@DisplayName("User Gets Deleted Successfully From Database")
	void userGetsDeletedSuccessfully() {
		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		assertEquals(1, userRepository.count());

		userRepository.deleteById(savedUser.getId());

		assertEquals(0, userRepository.count());

	}
}

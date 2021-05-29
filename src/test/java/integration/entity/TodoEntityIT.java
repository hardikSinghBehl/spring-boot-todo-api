package integration.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import com.hardik.donatello.SpringBootTodoApiApplication;
import com.hardik.donatello.entity.Todo;
import com.hardik.donatello.entity.User;
import com.hardik.donatello.repository.TodoRepository;
import com.hardik.donatello.repository.UserRepository;

import net.bytebuddy.utility.RandomString;

@SpringBootTest(classes = SpringBootTodoApiApplication.class)
public class TodoEntityIT {

	private static final LocalDate DUE_DATE = LocalDate.of(1999, 12, 25);

	private static final String TODO_DESCRIPTION = "Todo-Description";

	private static final String TODO_TITLE = "Todo-Title";

	private static final String FIRST_NAME = "Hardik";

	private static final String LAST_NAME = "Behl";

	private static final String EMAIL_ID = "hardikIsTheGreatest@gmail.com";

	private static final String PASSWORD = "Something-Strong-Password-123";

	public static PostgreSQLContainer container;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TodoRepository todoRepository;

	private List<UUID> usersToBeDeleted;

	private List<UUID> todosToBeDeleted;

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
		todosToBeDeleted = new ArrayList<UUID>();
	}

	@AfterEach
	void tearDown() {
		todosToBeDeleted.forEach(todoId -> todoRepository.deleteById(todoId));
		usersToBeDeleted.forEach(userId -> userRepository.deleteById(userId));
	}

	@Test
	@DisplayName("Todo Record is Added To The Database For A User")
	void todoPersistedSuccessfully() {
		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		final var todo = new Todo();
		todo.setActive(true);
		todo.setUserId(savedUser.getId());
		todo.setTitle(TODO_TITLE);
		todo.setDescription(TODO_DESCRIPTION);
		todo.setDueDate(DUE_DATE);
		final var savedTodo = todoRepository.save(todo);

		assertNotNull(savedUser);
		assertNotNull(savedTodo);
		assertEquals(savedUser.getId(), savedTodo.getUserId());
		assertEquals(TODO_TITLE, savedTodo.getTitle());
		assertEquals(TODO_DESCRIPTION, savedTodo.getDescription());
		assertEquals(DUE_DATE, savedTodo.getDueDate());

		usersToBeDeleted.add(savedUser.getId());
		todosToBeDeleted.add(savedTodo.getId());
	}

	@Test
	@DisplayName("All Todos Of A User Are Deleted When User Is Deleted")
	void todosForDeletingUserAreAlsoDeleted() {

		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		final var todo = new Todo();
		todo.setActive(true);
		todo.setUserId(savedUser.getId());
		todo.setTitle(TODO_TITLE);
		todo.setDescription(TODO_DESCRIPTION);
		todo.setDueDate(DUE_DATE);
		final var savedTodo = todoRepository.save(todo);

		userRepository.deleteById(savedUser.getId());
		assertTrue(userRepository.findById(savedUser.getId()).isEmpty());
		assertTrue(todoRepository.findById(savedTodo.getId()).isEmpty());

	}

	@Test
	@DisplayName("Todo Record Is Successfully Updated")
	void todoUpdationSuccess() {

		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		final var todo = new Todo();
		todo.setActive(true);
		todo.setUserId(savedUser.getId());
		todo.setTitle(TODO_TITLE);
		todo.setDescription(TODO_DESCRIPTION);
		todo.setDueDate(DUE_DATE);
		final var savedTodo = todoRepository.save(todo);

		final var retreivedUser = todoRepository.findById(savedTodo.getId()).get();
		retreivedUser.setDescription(TODO_DESCRIPTION + RandomString.make(50));
		retreivedUser.setDueDate(LocalDate.now().minusDays(5));
		final var updatedUser = todoRepository.save(retreivedUser);

		assertNotNull(updatedUser.getDescription());
		assertNotNull(updatedUser.getDueDate());
		assertNotEquals(savedTodo.getDescription(), updatedUser.getDescription());
		assertNotEquals(savedTodo.getDueDate(), updatedUser.getDueDate());

		usersToBeDeleted.add(savedUser.getId());
		todosToBeDeleted.add(savedTodo.getId());
	}

	@Test
	@DisplayName("Id, Created_at And Updated_at Fields Are Automatically Set When Todo Is Stored")
	void requiredFieldsAreSetWhenTodoIsPersisted() {

		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		final var todo = new Todo();
		todo.setActive(true);
		todo.setUserId(savedUser.getId());
		todo.setTitle(TODO_TITLE);
		todo.setDescription(TODO_DESCRIPTION);
		todo.setDueDate(DUE_DATE);
		final var savedTodo = todoRepository.save(todo);

		assertNotNull(savedTodo);
		assertNotNull(savedTodo.getId());
		assertThat(savedTodo.getId()).isInstanceOf(UUID.class);
		assertNotNull(savedTodo.getCreatedAt());
		assertThat(savedTodo.getCreatedAt()).isInstanceOf(LocalDateTime.class);
		assertNotNull(savedTodo.getUpdatedAt());
		assertThat(savedTodo.getUpdatedAt()).isInstanceOf(LocalDateTime.class);

		usersToBeDeleted.add(savedUser.getId());
		todosToBeDeleted.add(savedTodo.getId());

	}

	@Test
	@DisplayName("Updated_at Field gets Updated Automatically On Todo updation")
	void requiredFieldsAreUpdatedWhenTodoIsUpdated() {
		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		final var todo = new Todo();
		todo.setActive(true);
		todo.setUserId(savedUser.getId());
		todo.setTitle(TODO_TITLE);
		todo.setDescription(TODO_DESCRIPTION);
		todo.setDueDate(DUE_DATE);
		final var savedTodo = todoRepository.save(todo);

		final var retreivedTodo = todoRepository.findById(savedTodo.getId()).get();
		retreivedTodo.setDescription(TODO_DESCRIPTION + RandomString.make(50));
		final var updatedTodo = todoRepository.save(retreivedTodo);

		assertNotEquals(savedTodo.getUpdatedAt(), updatedTodo.getUpdatedAt());

		usersToBeDeleted.add(savedUser.getId());
		todosToBeDeleted.add(savedTodo.getId());
	}

	@Test
	@DisplayName("Todo Is Deleted Successfully")
	void todoDeletionSuccessfull() {
		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		final var todo = new Todo();
		todo.setActive(true);
		todo.setUserId(savedUser.getId());
		todo.setTitle(TODO_TITLE);
		todo.setDescription(TODO_DESCRIPTION);
		todo.setDueDate(DUE_DATE);
		final var savedTodo = todoRepository.save(todo);

		todoRepository.deleteById(savedTodo.getId());

		assertTrue(todoRepository.findById(savedTodo.getId()).isEmpty());
		assertEquals(0, userRepository.findById(savedUser.getId()).get().getTodos().size());

		usersToBeDeleted.add(savedUser.getId());
	}

	@Test
	@DisplayName("Todo Is Stored Corresponding to Mentioned User")
	void todoIsStoredForUserMentioned() {
		final var user = new User();
		user.setEmail(EMAIL_ID);
		user.setFirstName(FIRST_NAME);
		user.setLastName(LAST_NAME);
		user.setPassword(PASSWORD);
		final var savedUser = userRepository.save(user);

		final var todo = new Todo();
		todo.setActive(true);
		todo.setUserId(savedUser.getId());
		todo.setTitle(TODO_TITLE);
		todo.setDescription(TODO_DESCRIPTION);
		todo.setDueDate(DUE_DATE);
		final var savedTodo = todoRepository.save(todo);

		final var retreivedUser = userRepository.findById(savedUser.getId()).get();

		usersToBeDeleted.add(savedUser.getId());
		todosToBeDeleted.add(savedTodo.getId());

	}
}

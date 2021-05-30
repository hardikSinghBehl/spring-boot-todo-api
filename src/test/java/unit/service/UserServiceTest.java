package unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hardik.donatello.constant.ApiResponse;
import com.hardik.donatello.dto.request.UserCreationRequestDto;
import com.hardik.donatello.dto.request.UserDetailUpdationRequestDto;
import com.hardik.donatello.dto.request.UserPasswordUpdationRequestDto;
import com.hardik.donatello.dto.response.UserDetailDto;
import com.hardik.donatello.entity.User;
import com.hardik.donatello.exception.InvalidUserIdException;
import com.hardik.donatello.repository.UserRepository;
import com.hardik.donatello.security.utility.JwtUtils;
import com.hardik.donatello.service.UserService;
import com.hardik.donatello.utility.ResponseUtil;

import net.bytebuddy.utility.RandomString;

@DisplayName("User Service Layer Unit Test Cases")
public class UserServiceTest {

	private UserService userService;
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private JwtUtils jwtUtils;
	private ResponseUtil responseUtil;

	private static final UUID USER_ID = UUID.fromString("421df6a9-e302-4a5a-9196-1f2cecfddc9f");
	private static final LocalDateTime CREATED_AT = LocalDateTime.of(1999, 12, 25, 04, 40);
	private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2021, 05, 14, 13, 30);
	private static final String FIRST_NAME = "Hardik";
	private static final String LAST_NAME = "Behl";
	private static final String EMAIL_ID = "hardik.behl7444@gmail.com";
	private static final String PASSWORD = "abcdef123";

	@BeforeEach
	void setUp() {
		this.userRepository = mock(UserRepository.class);
		this.passwordEncoder = mock(PasswordEncoder.class);
		this.jwtUtils = mock(JwtUtils.class);
		this.responseUtil = new ResponseUtil();
		this.userService = new UserService(userRepository, passwordEncoder, jwtUtils, responseUtil);
	}

	@Nested
	@DisplayName("When Service is called to fetch logged-in users profile details")
	class FetchLoggedInUserDetailTest {

		@Test
		@DisplayName("When Correct User-id Is Provided")
		void retrieve_correctUserIdGiven_success() {
			// PREPARE
			final var user = mock(User.class);
			when(user.getCreatedAt()).thenReturn(CREATED_AT);
			when(user.getUpdatedAt()).thenReturn(UPDATED_AT);
			when(user.getFirstName()).thenReturn(FIRST_NAME);
			when(user.getLastName()).thenReturn(LAST_NAME);
			when(user.getEmail()).thenReturn(EMAIL_ID);
			when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

			// CALL
			final var response = userService.retrieve(USER_ID);

			// VERIFY
			assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
			assertNotNull(response.getBody());
			assertThat(response.getBody()).isInstanceOf(UserDetailDto.class);
			final var returnedUserDetails = response.getBody();
			assertAll(() -> assertEquals(FIRST_NAME, returnedUserDetails.getFirstName()),
					() -> assertEquals(LAST_NAME, returnedUserDetails.getLastName()),
					() -> assertEquals(EMAIL_ID, returnedUserDetails.getEmailId()),
					() -> assertEquals(CREATED_AT, returnedUserDetails.getCreatedAt()),
					() -> assertEquals(UPDATED_AT, returnedUserDetails.getUpdatedAt()));
			verify(userRepository).findById(USER_ID);
		}

		@Test
		@DisplayName("When Incorrect User-id Is Provided")
		void retrieve_inCorrectUserIdGiven_unauthorize() {
			// PREPARE
			when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

			// CALL
			assertThrows(InvalidUserIdException.class, () -> userService.retrieve(USER_ID));

			// VERIFY
			verify(userRepository).findById(USER_ID);
		}

	}

	@Nested
	@DisplayName("When Service to create user is called")
	class CreateUserTest {

		@Test
		@DisplayName("with duplicate email-id")
		void createUser_duplicateEmailIdProvided_unauthorize() {
			// PREPARE
			final var userCreationRequest = mock(UserCreationRequestDto.class);
			when(userCreationRequest.getEmailId()).thenReturn(EMAIL_ID);
			when(userCreationRequest.getFirstName()).thenReturn(FIRST_NAME);
			when(userCreationRequest.getLastName()).thenReturn(LAST_NAME);
			when(userCreationRequest.getPassword()).thenReturn(PASSWORD);
			when(userRepository.existsByEmail(EMAIL_ID)).thenReturn(true);

			// CALL
			final var response = userService.create(userCreationRequest);

			// VERIFY
			assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
			assertNotNull(response.getBody());
			assertThat(response.getBody().toString()).contains(ApiResponse.DUPLICATE_EMAIL_ID);
			assertThat(response.getBody().toString()).contains(ApiResponse.FAILURE_STATUS);
			verify(userRepository).existsByEmail(EMAIL_ID);
			verify(userCreationRequest, times(1)).getEmailId();
			verify(userCreationRequest, times(0)).getFirstName();
			verify(userCreationRequest, times(0)).getLastName();
			verify(userCreationRequest, times(0)).getPassword();
		}

		@Test
		@DisplayName("with unique email-id")
		void createUser_UniqueEmailIdProvided_success() {
			// PREPARE
			final var userCreationRequest = mock(UserCreationRequestDto.class);
			when(userCreationRequest.getEmailId()).thenReturn(EMAIL_ID);
			when(userCreationRequest.getFirstName()).thenReturn(FIRST_NAME);
			when(userCreationRequest.getLastName()).thenReturn(LAST_NAME);
			when(userCreationRequest.getPassword()).thenReturn(PASSWORD);
			when(userRepository.existsByEmail(EMAIL_ID)).thenReturn(false);
			when(userRepository.save(Mockito.any(User.class))).thenReturn(mock(User.class));

			// CALL
			final var response = userService.create(userCreationRequest);

			// VERIFY
			assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
			assertNotNull(response.getBody());
			assertThat(response.getBody().toString()).contains(ApiResponse.SUCCESSFULL_ACCOUNT_CREATION);
			assertThat(response.getBody().toString()).contains(ApiResponse.SUCCESS_STATUS);
			verify(userRepository).existsByEmail(EMAIL_ID);
			verify(userCreationRequest, times(2)).getEmailId();
			verify(userCreationRequest).getFirstName();
			verify(userCreationRequest).getLastName();
			verify(userCreationRequest).getPassword();
		}

	}

	@Nested
	@DisplayName("When Update User Profile Details Is Called")
	class UserProfileDetailUpdationTest {

		@Test
		@DisplayName("With Correct inputs")
		void updateUserDetails_correctValuesGiven_success() {
			// PREPARE
			final var user = mock(User.class);
			final var userDetailUpdationRequest = mock(UserDetailUpdationRequestDto.class);
			when(userDetailUpdationRequest.getFirstName()).thenReturn(RandomString.make(10));
			when(userDetailUpdationRequest.getLastName()).thenReturn(RandomString.make(6));
			when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
			when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

			// CALL
			final var response = userService.update(USER_ID, userDetailUpdationRequest);

			// VERIFY
			assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
			assertNotNull(response.getBody());
			assertThat(response.getBody().toString()).contains(ApiResponse.USER_DETAIL_UPDATION_SUCCESS);
			assertThat(response.getBody().toString()).contains(ApiResponse.SUCCESS_STATUS);
			verify(user).setLastName(Mockito.anyString());
			verify(user).setFirstName(Mockito.anyString());
			verify(userDetailUpdationRequest).getFirstName();
			verify(userDetailUpdationRequest).getLastName();
			verify(userRepository).save(user);
			verify(userRepository).findById(USER_ID);
		}

		@Test
		@DisplayName("When Incorrect User-id Is Provided")
		void updateUserDetail_inCorrectUserIdGiven_unauthorize() {
			// PREPARE
			when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

			// CALL
			assertThrows(InvalidUserIdException.class,
					() -> userService.update(USER_ID, mock(UserDetailUpdationRequestDto.class)));

			// VERIFY
			verify(userRepository).findById(USER_ID);
			verify(userRepository, times(0)).save(Mockito.any(User.class));
		}

	}

	@Nested
	@DisplayName("When User Password Updation Service Is Called")
	class UserPasswordUpdationTest {

		@Test
		@DisplayName("With Correct User-id And Old Password")
		void updatePassword_correctValuesGiven_unauthorize() {
			// PREPARE
			final var user = mock(User.class);
			final var userPasswordUpdationRequest = mock(UserPasswordUpdationRequestDto.class);
			when(userPasswordUpdationRequest.getOldPassword()).thenReturn(PASSWORD);
			when(userPasswordUpdationRequest.getOldPassword()).thenReturn(RandomString.make(8));
			when(user.getPassword()).thenReturn(PASSWORD);
			when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
			when(passwordEncoder.matches(userPasswordUpdationRequest.getOldPassword(), user.getPassword()))
					.thenReturn(true);
			when(userRepository.save(user)).thenReturn(user);

			// CALL
			final var response = userService.update(USER_ID, userPasswordUpdationRequest);

			// VERIFY
			assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
			assertNotNull(response.getBody());
			assertThat(response.getBody().toString()).contains(ApiResponse.USER_PASSWORD_UPDATION_SUCCESS);
			assertThat(response.getBody().toString()).contains(ApiResponse.SUCCESS_STATUS);
			verify(userRepository).findById(USER_ID);
			verify(userRepository).save(user);
		}

		@Test
		@DisplayName("With Incorrect Old Password")
		void updatePassword_cinorrectOldPasswordGiven_unauthorize() {
			// PREPARE
			final var user = mock(User.class);
			final var userPasswordUpdationRequest = mock(UserPasswordUpdationRequestDto.class);
			when(userPasswordUpdationRequest.getOldPassword()).thenReturn(PASSWORD);
			when(userPasswordUpdationRequest.getOldPassword()).thenReturn(RandomString.make(8));
			when(user.getPassword()).thenReturn(PASSWORD);
			when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
			when(passwordEncoder.matches(userPasswordUpdationRequest.getOldPassword(), user.getPassword()))
					.thenReturn(false);
			when(userRepository.save(user)).thenReturn(user);

			// CALL
			final var response = userService.update(USER_ID, userPasswordUpdationRequest);

			// VERIFY
			assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
			assertNotNull(response.getBody());
			assertThat(response.getBody().toString()).contains(ApiResponse.WRONG_PASSWORD);
			assertThat(response.getBody().toString()).contains(ApiResponse.FAILURE_STATUS);
			verify(userRepository).findById(USER_ID);
			verify(userRepository, times(0)).save(user);
		}

		@Test
		@DisplayName("With Incorrect User-id")
		void updatePassword_inCorrectValuesGiven_unauthorize() {
			// PREPARE
			when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

			// CALL
			assertThrows(InvalidUserIdException.class,
					() -> userService.update(USER_ID, mock(UserPasswordUpdationRequestDto.class)));

			// VERIFY
			verify(userRepository).findById(USER_ID);
			verify(userRepository, times(0)).save(Mockito.any(User.class));
		}

	}

}

package com.todo.service;

import com.todo.domain.User;
import com.todo.repository.userRepo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private InputValidation inputValidation;

    @InjectMocks
    private UserServiceImpl underTestUserService;

    @Test
    void createNewUserTest() throws Exception {
        //given
        User user = new User(
                "testUser",
                "testUser@gmail.com",
                "Test@1234",
                "Test@1234"
        );
        //when
        when(userRepository.existsUserByEmail(anyString())).thenReturn(false);
        underTestUserService.createNewUser(user);
        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveUser(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void createNewUserThrowTest() {
        //given
        User user = new User(
                "testUser",
                "testUser@gmail.com",
                "Test@1234",
                "Test@1234"
        );
        //when
        when(userRepository.existsUserByEmail(anyString())).thenReturn(true);
        //then
        assertThatThrownBy(() ->underTestUserService.createNewUser(user))
                .isInstanceOf(Exception.class)
                .hasMessageContaining(user.getEmail() + " is already registered!");
        verify(userRepository, never()).saveUser(user);
    }

    @Test
    void fetchUserLoginTest() throws Exception {
        //given
        User user = new User(
                "testUser@gmail.com",
                "Test@1234"
        );
        //when
        when(userRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(user);
        User resultUser = underTestUserService.fetchUserLogin(user);
        //then
        assertThat(user).isEqualTo(resultUser);
    }

    @Test
    void fetchUserLoginThrowTest() {
        //given
        User user = new User(
                "testUser@gmail.com",
                "Test@1234"
        );
        //when
        when(userRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(null);
        //then
        assertThatThrownBy(() ->underTestUserService.fetchUserLogin(user))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Invalid Email or Password !!!");
    }

    @Test
    void updateUserTest() throws Exception {
        //given
        Long id = 1L;
        User user = new User(
                "",
                "",
                "Test@1234",
                "Test@5678",
                "Test@5678"
        );
        User existingUser = new User(
                1L,
                "testUser",
                "testUser@gmail.com",
                "Test@1234"
        );
        //when
        when(userRepository.findUserById(anyLong())).thenReturn(existingUser);
        underTestUserService.updateUser(id, user);
        //then
        verify(userRepository).updateUser(user);
    }

    @Test
    void updateUserThrowIfNoPasswordTest() {
        //given
        Long id = 1L;
        User user = new User(
                "testUser",
                "testUser@gmail.com",
                "",
                "Test@5678",
                "Test@5678"
        );
        //when
        //then
        assertThatThrownBy(() ->underTestUserService.updateUser(id, user))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Enter password to Change Profile details");
        verify(userRepository, never()).updateUser(user);
    }

    @Test
    void updateUserThrowNoUserExistsTest() {
        //given
        Long id = 1L;
        User user = new User(
                "testUser",
                "testUser@gmail.com",
                "Test@1234",
                "Test@5678",
                "Test@5678"
        );
        //when
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        //then
        assertThatThrownBy(() ->underTestUserService.updateUser(id, user))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("user with id " + id + " does not exists");
        verify(userRepository, never()).updateUser(user);
    }

    @Test
    void updateUserThrowIncorrectPasswordTest() {
        //given
        Long id = 1L;
        User user = new User(
                "testUser",
                "testUser@gmail.com",
                "Test@0000",
                "Test@5678",
                "Test@5678"
        );
        User existingUser = new User(
                "testUser@gmail.com",
                "Test@1234"
        );
        //when
        when(userRepository.findUserById(anyLong())).thenReturn(existingUser);
        //then
        assertThatThrownBy(() ->underTestUserService.updateUser(id, user))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Incorrect Password!");
        verify(userRepository, never()).updateUser(user);
    }

    @Test
    void updateUserThrowEmailExistsTest() {
        //given
        Long id = 1L;
        String email = "testUser1@gmail.com";
        User user = new User(
                "testUser",
                email,
                "Test@5678",
                "Test@5678",
                "Test@5678"
        );
        User existingUser = new User(
                1L,
                "testUser",
                "testUser@gmail.com",
                "Test@1234"
        );
        //when
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(userRepository.existsUserByEmail(anyString())).thenReturn(true);
        //then
        assertThatThrownBy(() ->underTestUserService.updateUser(id, user))
                .isInstanceOf(Exception.class)
                .hasMessageContaining(email + " is already registered!");
        verify(userRepository, never()).updateUser(user);
    }

    @Test
    void updateUserThrowSameNewPasswordTest() {
        //given
        Long id = 1L;
        User user = new User(
                "testUser",
                "testUser@gmail.com",
                "Test@5678",
                "Test@5678",
                "Test@5678"
        );
        //when
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        //then
        assertThatThrownBy(() ->underTestUserService.updateUser(id, user))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("New password can not be same a old password!");
        verify(userRepository, never()).updateUser(user);
    }


    @Test
    void deleteUserTest() throws Exception {
        //given
        Long id = 1L;
        when(userRepository.existsUserById(anyLong())).thenReturn(true);
        //when
        underTestUserService.deleteUser(id);
        //then
        verify(userRepository).deleteUserById(id);
    }

    @Test
    void deleteUserThrowTest() {
        //given
        Long id = 1L;
        when(userRepository.existsUserById(anyLong())).thenReturn(false);
        //when
        //then
        assertThatThrownBy(() ->underTestUserService.deleteUser(id))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("user with id: " + id + " does not exists");
        verify(userRepository, never()).deleteUserById(id);
    }
}
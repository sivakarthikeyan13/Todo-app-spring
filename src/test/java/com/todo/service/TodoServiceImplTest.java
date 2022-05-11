package com.todo.service;

import com.todo.domain.TodoItem;
import com.todo.repository.todoRepo.TodoRepository;
import com.todo.repository.userRepo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private InputValidation inputValidation;

    @InjectMocks
    private TodoServiceImpl underTestTodoService;

    @Test
    void fetchAllTodoItemsTest() throws Exception {
        //given
        Long userId = 1L;
        //when
        when(userRepository.existsUserById(userId)).thenReturn(true);
        List<TodoItem> itemList = underTestTodoService.fetchAllTodoItems(userId);
        //then
        assertThat(itemList.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void fetchAllTodoItemsThrowNoUserTest() {
        //given
        Long userId = 1L;
        //when
        when(userRepository.existsUserById(userId)).thenReturn(false);
        //then
        assertThatThrownBy(() ->underTestTodoService.fetchAllTodoItems(userId))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("user with id: " + userId + " does not exists");
        verify(todoRepository, never()).findAllByUserIdOrderByDate(userId);
    }

    @Test
    void createNewTodoItemTest() throws Exception{
        //given
        TodoItem todoItem = new TodoItem(
                1L,
                "testTask",
                false,
                LocalDate.of(2022, 5, 3)
        );
        //when
        when(userRepository.existsUserById(anyLong())).thenReturn(true);
        underTestTodoService.createNewTodoItem(todoItem);
        //then
        ArgumentCaptor<TodoItem> todoItemArgumentCaptor = ArgumentCaptor.forClass(TodoItem.class);
        verify(todoRepository).saveTodoItem(todoItemArgumentCaptor.capture());
        TodoItem capturedTodoItem = todoItemArgumentCaptor.getValue();
        assertThat(capturedTodoItem).isEqualTo(todoItem);
    }

    @Test
    void createNewTodoItemThrowNoUserIdTest() {
        //given
        TodoItem todoItem = new TodoItem(
                null,
                "testTask",
                false,
                LocalDate.of(2022, 5, 3)
        );
        //when
        //then
        assertThatThrownBy(() ->underTestTodoService.createNewTodoItem(todoItem))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("enter user id");
        verify(todoRepository, never()).saveTodoItem(todoItem);
    }

    @Test
    void createNewTodoItemThrowUserExistsTest() {
        //given
        Long userId = 1L;
        TodoItem todoItem = new TodoItem(
                userId,
                "testTask",
                false,
                LocalDate.of(2022, 5, 3)
        );
        //when
        when(userRepository.existsUserById(anyLong())).thenReturn(false);
        //then
        assertThatThrownBy(() ->underTestTodoService.createNewTodoItem(todoItem))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("user with id: " + userId + " does not exists");
        verify(todoRepository, never()).saveTodoItem(todoItem);
    }

    @Test
    void updateTodoItemTest() throws Exception{
        //given
        Long id = 2L;
        TodoItem todoItem = new TodoItem(
                1L,
                "updatedTestTask",
                null,
                LocalDate.of(2022, 5, 3)
        );
        TodoItem existingTodoItem = new TodoItem(
                1L,
                "testTask",
                false,
                LocalDate.of(2022, 5, 3)
        );
        //when
        when(todoRepository.findItemById(anyLong())).thenReturn(existingTodoItem);
        underTestTodoService.updateTodoItem(id, todoItem);
        //then
        verify(todoRepository).updateItem(todoItem);
    }

    @Test
    void updateTodoItemNoNameAndDateTest() throws Exception{
        //given
        Long id = 2L;
        TodoItem todoItem = new TodoItem(
                1L,
                "",
                null,
                null
        );
        TodoItem existingTodoItem = new TodoItem(
                1L,
                "testTask",
                false,
                LocalDate.of(2022, 5, 3)
        );
        //when
        when(todoRepository.findItemById(anyLong())).thenReturn(existingTodoItem);
        underTestTodoService.updateTodoItem(id, todoItem);
        //then
        verify(todoRepository).updateItem(todoItem);
    }

    @Test
    void updateTodoItemThrowUserIdNullTest() {
        //given
        Long id = 2L;
        TodoItem todoItem = new TodoItem(
                null,
                "updatedTestTask",
                false,
                LocalDate.of(2022, 5, 3)
        );
        //when
        //then
        assertThatThrownBy(() ->underTestTodoService.updateTodoItem(id,todoItem))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("User id not found!");
        verify(todoRepository, never()).updateItem(todoItem);
    }

    @Test
    void updateTodoItemThrowItemNoExistsTest() {
        //given
        Long id = 2L;
        TodoItem todoItem = new TodoItem(
                1L,
                "updatedTestTask",
                false,
                LocalDate.of(2022, 5, 3)
        );
        //when
        when(todoRepository.findItemById(anyLong())).thenReturn(null);
        //then
        assertThatThrownBy(() ->underTestTodoService.updateTodoItem(id,todoItem))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Task with id: " + id + " does not exists");
        verify(todoRepository, never()).updateItem(todoItem);
    }

    @Test
    void updateTodoItemThrowUserIdNoMatchTest() {
        //given
        Long id = 2L;
        Long userId = 1L;
        TodoItem todoItem = new TodoItem(
                userId,
                "updatedTestTask",
                false,
                LocalDate.of(2022, 5, 3)
        );
        TodoItem existingTodoItem = new TodoItem(
                5L,
                "testTask",
                false,
                LocalDate.of(2022, 5, 3)
        );
        //when
        when(todoRepository.findItemById(anyLong())).thenReturn(existingTodoItem);
        //then
        assertThatThrownBy(() ->underTestTodoService.updateTodoItem(id,todoItem))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Task with id: " + id + " did not match with user id: " + userId );
        verify(todoRepository, never()).updateItem(todoItem);
    }

    @Test
    void deleteTodoItemTest() throws Exception {
        //given
        Long id = 1L;
        //when
        when(todoRepository.existsItemById(anyLong())).thenReturn(true);
        underTestTodoService.deleteTodoItem(id);
        //then
        verify(todoRepository).deleteItemById(id);
    }

    @Test
    void deleteTodoItemThrowNoItemTest() {
        //given
        Long id = 1L;
        //when
        when(todoRepository.existsItemById(anyLong())).thenReturn(false);
        //then
        assertThatThrownBy(() ->underTestTodoService.deleteTodoItem(id))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Task with id: " + id + " does not exists");
        verify(todoRepository, never()).deleteItemById(id);
    }
}
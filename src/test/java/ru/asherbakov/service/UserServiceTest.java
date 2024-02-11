package ru.asherbakov.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.asherbakov.models.Location;
import ru.asherbakov.models.StructuralDivision;
import ru.asherbakov.models.User;
import ru.asherbakov.repository.UserRepository;
import ru.asherbakov.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService = new UserServiceImpl();
    List<User> userList = new ArrayList<>();
    @BeforeEach
    void setUp() {
        userList.add(new User("TEST_USER1","PASS1","Имя","Фамилия","Отчество",new Location(),new StructuralDivision(),(byte)1));
        userList.add(new User("TEST_USER2","PASS2","Имя","Фамилия",null,null,new StructuralDivision(),(byte)1));
    }
    @Test
    public void whenWeGetAllUsersReturnCorrectAnswer() {
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        Iterable<User> result = userService.getAllUsers();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userList, result);
    }
    @Test
    public void whenWeGetUserFullnameWithMiddleNameReturnCorrectAnswer() {
        String result = userService.getUserFullName(userList.get(0));
        String temp = String.format("%s %s %s", userList.get(0).getLastName(), userList.get(0).getFirstName(), userList.get(0).getMiddleName());
        Assertions.assertEquals(temp, result);
    }
    @Test
    public void whenWeGetUserFullnameWithoutMiddleNameReturnCorrectAnswer() {
        String result = userService.getUserFullName(userList.get(1));
        String temp = String.format("%s %s", userList.get(1).getLastName(), userList.get(1).getFirstName());
        Assertions.assertEquals(temp, result);
    }
}

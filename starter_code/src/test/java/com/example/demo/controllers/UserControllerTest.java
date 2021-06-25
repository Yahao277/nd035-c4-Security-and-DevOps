package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

  private UserController userController;
  private UserRepository userRepository = mock(UserRepository.class);
  private CartRepository cartRepository = mock(CartRepository.class);
  private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

  @Before
  public void setUp() {
    userController = new UserController();
    TestUtils.injectObjects(userController, "userRepository", userRepository);
    TestUtils.injectObjects(userController, "cartRepository", cartRepository);
    TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

    User user = new User();
    user.setId(1L);
    user.setUsername("test");
    user.setPassword("password");
    when(userRepository.findByUsername("test")).thenReturn(user);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
  }

  @Test
  public void findById(){

    ResponseEntity<User> response = userController.findById(1L);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());

    User responseUser = response.getBody();

    assertEquals("test", responseUser.getUsername());
  }

  @Test
  public void findByName(){

    ResponseEntity<User> response = userController.findByUserName("test");
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());

    User responseUser = response.getBody();
    assertEquals("test", responseUser.getUsername());
  }

  @Test
  public void findByNameFail(){

    ResponseEntity<User> response = userController.findByUserName("testA");
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());

  }

  @Test
  public void createUser(){
    CreateUserRequest request = new CreateUserRequest();
    request.setUsername("create_test");
    request.setPassword("passwords");
    request.setConfirmPassword("passwords");

    ResponseEntity<User> response =  userController.createUser(request);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    User user = response.getBody();
    assertEquals("create_test",user.getUsername());
  }

  @Test
  public void createUserFail(){
    CreateUserRequest request = new CreateUserRequest();
    request.setUsername("create_test");
    request.setPassword("passwords");
    request.setConfirmPassword("password2");

    ResponseEntity<User> response =  userController.createUser(request);
    assertNotNull(response);
    assertEquals(400, response.getStatusCodeValue());
  }

}

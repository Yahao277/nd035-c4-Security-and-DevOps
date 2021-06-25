package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

  private OrderController orderController;
  private UserRepository userRepository = mock(UserRepository.class);
  private OrderRepository orderRepository = mock(OrderRepository.class);

  public User createUser(Long id){
    User user = new User();
    user.setId(id);
    user.setUsername("cart_test_user");
    user.setPassword("cart_test");
    return user;
  }

  public Item createItem(Long id){
    Item item = new Item();
    item.setId(id);
    item.setName("one_item");
    item.setPrice(BigDecimal.valueOf(90));
    item.setDescription("item_description");
    return item;
  }

  @Before
  public void setUp() {
    orderController = new OrderController();
    TestUtils.injectObjects(orderController, "userRepository", userRepository);
    TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

    Item item = this.createItem(1L);
    User user = this.createUser(1L);
    Cart cart = new Cart();
    cart.setUser(user);
    cart.addItem(item);
    user.setCart(cart);
    UserOrder userOrder = UserOrder.createFromCart(cart);

    when(userRepository.findByUsername("cart_test_user")).thenReturn(user);
    when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(userOrder));

  }

  @Test
  public void submit(){
    ResponseEntity<UserOrder> response = orderController.submit("cart_test_user");
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    UserOrder userOrder = response.getBody();
    assertEquals(1,userOrder.getItems().size());
    assertEquals("cart_test_user",userOrder.getUser().getUsername());
  }

  @Test
  public void submitFail(){
    ResponseEntity<UserOrder> response = orderController.submit("cart_test_userA");
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  public void getOrdersForUsers(){
    ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("cart_test_user");
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    List<UserOrder> userOrders = response.getBody();
    assertEquals(1,userOrders.size());

  }
}

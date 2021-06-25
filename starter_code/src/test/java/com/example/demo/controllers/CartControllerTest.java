package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.configuration.IMockitoConfiguration;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

  private CartController cartController;
  private UserRepository userRepository = mock(UserRepository.class);
  private CartRepository cartRepository = mock(CartRepository.class);
  private ItemRepository itemRepository = mock(ItemRepository.class);

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
  public void setUp(){
    cartController = new CartController();
    TestUtils.injectObjects(cartController,"userRepository",userRepository);
    TestUtils.injectObjects(cartController,"itemRepository",itemRepository);
    TestUtils.injectObjects(cartController,"cartRepository",cartRepository);

    Item item = this.createItem(1L);
    User user = this.createUser(1L);
    Cart cart = new Cart();
    cart.setUser(user);
    cart.addItem(item);
    user.setCart(cart);

    when(userRepository.findByUsername("cart_test_user")).thenReturn(user);
    when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
  }

  @Test
  public void addToCart(){
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("cart_test_user");
    request.setItemId(1L);
    request.setQuantity(1);

    ResponseEntity<Cart> response = cartController.addTocart(request);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    Cart cart = response.getBody();
    assertEquals(2,cart.getItems().size());
  }

  @Test
  public void addToCartFail(){
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("cart_test_user");
    request.setItemId(10L);
    request.setQuantity(1);

    ResponseEntity<Cart> response = cartController.addTocart(request);
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  public void removeFromCart(){
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("cart_test_user");
    request.setItemId(1L);
    request.setQuantity(1);

    ResponseEntity<Cart> response = cartController.removeFromcart(request);
    assertNotNull(response);
    assertEquals(200,response.getStatusCodeValue());
    Cart cart = response.getBody();
    assertEquals(0,cart.getItems().size());
  }

  @Test
  public void removeFromCartNotFound(){
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("cart_test_user");
    request.setItemId(10L);
    request.setQuantity(1);

    ResponseEntity<Cart> response = cartController.removeFromcart(request);
    assertNotNull(response);
    assertEquals(404,response.getStatusCodeValue());

  }

}

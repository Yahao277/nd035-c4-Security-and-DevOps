package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
  private ItemController itemController;
  private ItemRepository itemRepository = mock(ItemRepository.class);

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
    itemController = new ItemController();
    TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

    Item item = createItem(1L);

    when(itemRepository.findAll()).thenReturn(Arrays.asList(item));
    when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    when(itemRepository.findByName(item.getName())).thenReturn(Arrays.asList(item));
  }

  @Test
  public void getItems(){
    ResponseEntity<List<Item>> response = itemController.getItems();
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    List<Item> items = response.getBody();
    assertEquals(1,items.size());
  }

  @Test
  public void getItemById(){
    ResponseEntity<Item> response = itemController.getItemById(1L);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    Item item = response.getBody();
    assertEquals("one_item",item.getName());
  }

  @Test
  public void getItemByIdFails(){
    ResponseEntity<Item> response = itemController.getItemById(3L);
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  public void getItemByName(){
    ResponseEntity<List<Item>> response = itemController.getItemsByName("one_item");
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    List<Item> items = response.getBody();
    assertEquals(1,items.size());
  }

  @Test
  public void getItemByNameNotFound(){
    ResponseEntity<List<Item>> response = itemController.getItemsByName("one_itemA");
    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());

  }

}

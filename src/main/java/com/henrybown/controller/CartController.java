package com.henrybown.controller;

import com.henrybown.data.CartRepository;
import com.henrybown.model.ShoppingCartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.ArrayList;

@Controller
public class CartController {

    @Autowired
    private CartRepository cartRepository = new CartRepository();

    @RequestMapping("/create-table")
    public String createTable() throws SQLException {
        CartRepository.initializeTable();
        return "redirect:/";
    }

    @RequestMapping("/")
    public String loadCart(ModelMap modelMap) throws SQLException {
        ArrayList<ShoppingCartItem> items = CartRepository.databaseToObjects();
        modelMap.put("items", items);
        return "cart";
    }

    @PostMapping("/add-item")
    public String addItem(@RequestParam(name = "item") String item) throws SQLException {
        if (item.length() != 0) {
            CartRepository.addNewItemToCart(item);
        }
        return "redirect:/";
    }

    @RequestMapping("/add/{item}")
    public String increaseQuantity(@PathVariable("item") String item) throws SQLException {
        CartRepository.increaseQuantity(item);

        return "redirect:/";
    }

    @PostMapping("/remove/{item}")
    public String decreaseQuantity(@PathVariable("item") String item) throws SQLException {
        CartRepository.decreaseQuantity(item);
        return "redirect:/";
    }

}

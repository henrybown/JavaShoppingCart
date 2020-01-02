package com.henrybown.data;

import com.henrybown.model.ShoppingCartItem;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;

@Component
public class CartRepository {
    static final String GET_ITEMS = "SELECT * FROM cart";
    static ArrayList<ShoppingCartItem> shoppingCartItems = new ArrayList<>();
    static int timeout = 30;
    private static String dbUrl = "jdbc:sqlite:database.db";

    public static boolean initializeTable() throws SQLException {
        String create = "CREATE TABLE cart (ID INTEGER, ProductName TEXT NOT NULL, Quantity INTEGER, PRIMARY KEY(ID));";
        Connection connection = DriverManager.getConnection(dbUrl);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(timeout);
        statement.executeUpdate(create);
        getShoppingCartItems(statement);
        return true;
    }

    public static ArrayList<ShoppingCartItem> databaseToObjects() throws SQLException {


        Connection connection = DriverManager.getConnection(dbUrl);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        return getShoppingCartItems(statement);
    }

    private static ArrayList<ShoppingCartItem> getShoppingCartItems(Statement statement) throws SQLException {
        shoppingCartItems = new ArrayList<>();
        ResultSet rs = statement.executeQuery(GET_ITEMS);

        int n = 0;
        while (rs.next()) {
            String productName = rs.getString("ProductName");
            int quantity = rs.getInt("Quantity");

            shoppingCartItems.add(n, new ShoppingCartItem(productName, quantity));
            n++;
        }

        return shoppingCartItems;
    }

    public static void addNewItemToCart(String productName) throws SQLException {
        String insert = "INSERT INTO cart (ProductName, Quantity) VALUES('" + productName + "', 1);";
        Connection connection = DriverManager.getConnection(dbUrl);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(timeout);
        if (!increaseQuantity(productName)) {
            statement.executeUpdate(insert);
        }
        getShoppingCartItems(statement);
    }

    public static void removeItemFromCart(String productName) throws SQLException {

        String delete = "DELETE FROM cart WHERE ProductName = '" + productName + "';";
        Connection connection = DriverManager.getConnection(dbUrl);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(timeout);
        statement.executeUpdate(delete);

    }

    public static boolean increaseQuantity(String item) throws SQLException {
        Connection connection = DriverManager.getConnection(dbUrl);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(timeout);
        ResultSet rs = statement.executeQuery("SELECT * FROM cart WHERE ProductName = '" + item + "';");
        int n = 0;
        while (rs.next()) {
            n = rs.getInt("Quantity");
        }

        n++;
        statement.execute("UPDATE cart SET quantity = " + n + " WHERE ProductName = '" + item + "';");
        if (n != 1) {
            return true;
        } else {
            return false;
        }

    }


    public static void decreaseQuantity(String item) throws SQLException {
        Connection connection = DriverManager.getConnection(dbUrl);

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(timeout);
        ResultSet rs = statement.executeQuery("SELECT * FROM cart WHERE ProductName = '" + item + "';");
        int n = 0;
        while (rs.next()) {
            n = rs.getInt("Quantity");
        }

        if (n - 1 == 0) {
            removeItemFromCart(item);
        } else {
            n--;
            statement.execute("UPDATE cart SET quantity = " + n + " WHERE ProductName = '" + item + "';");
        }

    }
}

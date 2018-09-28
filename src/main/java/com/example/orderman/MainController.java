package com.example.orderman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private DataSource ds;

    private Connection con;
    private Statement st ;
    private ResultSet rs ;

    @GetMapping("/")
    public String orderlist(
            Map<String, Object> model
    ) {
        List<Order> allOrders;
        Order order;
        try {

            con = ds.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT *" +
                                    " FROM orders");

            allOrders = new ArrayList<Order>();
            while(rs.next()){
                order = new Order();
                order.id = rs.getInt("orderId");
                order.date = rs.getDate("orderDate");
                order.name = rs.getString("orderName");
                allOrders.add(order);
            }
            model.put("allOrders", allOrders);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "orderlist";
    }

    @GetMapping("/order")
    public String order(
            @RequestParam(name="id", required=true) String id,
            Map<String, Object> model
    ) {
        Order order;
        List<Position> allPositions;
        Position pos;
        try {

            con = ds.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT *" +
                                    " FROM orders" +
                                    " WHERE orderId = " + id);

            order = new Order();
            while(rs.next()){
                order.id = rs.getInt("orderId");
                order.date = rs.getDate("orderDate");
                order.name = rs.getString("orderName");
                break;
            }
            model.put("orderId", order.id);
            model.put("orderDate", order.date);
            model.put("orderName", order.name);

            con = ds.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT *" +
                                    " FROM q_positions" +
                                    " WHERE orderId = " + id);
            allPositions = new ArrayList<Position>();
            pos = new Position();
            while(rs.next()){
                pos = new Position();
                pos.orderId = rs.getInt("orderId");
                pos.name = rs.getString("itemName");
                pos.cost = rs.getDouble("itemCost");
                pos.isArchival = rs.getBoolean("itemIsArchival");
                pos.count = rs.getInt("posCount");
                pos.summ = rs.getDouble("posSumm");
                allPositions.add(pos);
            }
            model.put("allPositions", allPositions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "order";
    }

}
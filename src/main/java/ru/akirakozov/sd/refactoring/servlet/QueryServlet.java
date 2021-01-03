package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.html_answer.ResponseMaker;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        String responseBody = "";

        if ("max".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
                    StringBuilder temp = new StringBuilder();
                    while (rs.next()) {
                        String name = rs.getString("name");
                        int price = rs.getInt("price");
                        temp.append(name).append("\t").append(price).append("</br>");
                    }
                    responseBody = ResponseMaker.funcResponse("Product with max price: ", Boolean.TRUE, temp.toString());
                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
                    StringBuilder temp = new StringBuilder();
                    while (rs.next()) {
                        String name = rs.getString("name");
                        int price = rs.getInt("price");
                        temp.append(name).append("\t").append(price).append("</br>");
                    }
                    responseBody = ResponseMaker.funcResponse("Product with min price: ", Boolean.TRUE, temp.toString());
                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
                    String temp = "";
                    if (rs.next()) {
                        temp = ("" + rs.getInt(1));
                    }
                    responseBody = ResponseMaker.funcResponse("Summary price: \n", Boolean.FALSE, temp);
                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
                    String temp = "";
                    if (rs.next()) {
                        temp = ("" + rs.getInt(1));
                    }
                    responseBody = ResponseMaker.funcResponse("Number of products: \n", Boolean.FALSE, temp);

                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            responseBody = "Unknown command: " + command;
        }
        ResponseMaker.okResponse(response, responseBody);
    }

}

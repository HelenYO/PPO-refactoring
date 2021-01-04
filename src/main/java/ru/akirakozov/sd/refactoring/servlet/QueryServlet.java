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

    private String dbFuncSumCount(String command) {
        String responseBody = "";
        String header = "";
        String dbCommand = "";
        if (command.equals("sum")) {
            dbCommand = "SELECT SUM(price) FROM PRODUCT";
            header = "Summary price: \n";
        } else if (command.equals("count")) {
            dbCommand = "SELECT COUNT(*) FROM PRODUCT";
            header = "Number of products: \n";
        }
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(dbCommand);
                String temp = "";
                if (rs.next()) {
                    temp = ("" + rs.getInt(1));
                }
                responseBody = ResponseMaker.funcResponse(header, Boolean.FALSE, temp);
                rs.close();
                stmt.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }

    private String dbFuncMinMax(String command) {
        String dbCommand = "";
        if (command.equals("min")) {
            dbCommand = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
        } else if (command.equals("max")) {
            dbCommand = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
        }
        String responseBody = "";
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(dbCommand);
                StringBuilder temp = new StringBuilder();
                while (rs.next()) {
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    temp.append(name).append("\t").append(price).append("</br>");
                }
                String header = "Product with " + command + " price: ";
                responseBody = ResponseMaker.funcResponse(header, Boolean.TRUE, temp.toString());
                rs.close();
                stmt.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        String responseBody = "";

        if ("max".equals(command)) {
            responseBody = dbFuncMinMax("max");
        } else if ("min".equals(command)) {
            responseBody = dbFuncMinMax("min");
        } else if ("sum".equals(command)) {
            responseBody = dbFuncSumCount("sum");
        } else if ("count".equals(command)) {
            responseBody = dbFuncSumCount("count");
        } else {
            responseBody = "Unknown command: " + command;
        }
        ResponseMaker.okResponse(response, responseBody);
    }
}

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
public class GetProductsServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder body = new StringBuilder();
        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
                body.append("<html><body>\n");

                while (rs.next()) {
                    String  name = rs.getString("name");
                    int price  = rs.getInt("price");
                    body.append(name).append("\t").append(price).append("</br>\n");
                }
                body.append("</body></html>");

                rs.close();
                stmt.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ResponseMaker.okResponse(response, body.toString());
    }
}

package ru.akirakozov.sd.refactoring;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.mockito.Mockito.when;

public class AddProductTest {
    private AddProductServlet addProductServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DELETE FROM PRODUCT";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        addProductServlet = new AddProductServlet();
    }

    @Test
    public void addTest() throws IOException {

        when(request.getParameter("name")).thenReturn("Moon", "rice");
        when(request.getParameter("price")).thenReturn("100000000", "70");
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        addProductServlet.doGet(request, response);
        addProductServlet.doGet(request, response);

        String expected = "Moon - 100000000\nrice - 70\n";
        StringBuilder res = new StringBuilder();

        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
            while (rs.next()) {
                res.append(rs.getString("name")).append(" - ").append(rs.getString("price")).append("\n");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(expected, res.toString());
    }
}

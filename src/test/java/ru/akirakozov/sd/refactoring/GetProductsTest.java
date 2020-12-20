package ru.akirakozov.sd.refactoring;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.mockito.Mockito.when;

public class GetProductsTest {
    private GetProductsServlet getProductsServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DELETE FROM PRODUCT";
            String sql2 = "INSERT INTO PRODUCT " +
                    "(NAME, PRICE) VALUES " +
                    "('Moon', 100000000)," +
                    "('rice', 70)";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.executeUpdate(sql2);
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        getProductsServlet = new GetProductsServlet();
    }

    @Test
    public void addTest() throws IOException {
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        getProductsServlet.doGet(request, response);
        Assert.assertEquals("<html><body>\n" +
                        "Moon\t100000000</br>\n" +
                        "rice\t70</br>\n" +
                        "</body></html>\n"
                , writer.toString());
    }
}

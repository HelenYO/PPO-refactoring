package ru.akirakozov.sd.refactoring;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.mockito.Mockito.when;

public class QueryTest {
    private QueryServlet queryServlet;

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
        queryServlet = new QueryServlet();
    }

    private void assertCommandResult(String command, String expectedRes) throws IOException {
        when(request.getParameter("command")).thenReturn(command);
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        queryServlet.doGet(request, response);
        Assert.assertEquals(expectedRes, writer.toString());
    }

    private String makeHtml(String command) {
        String head, info;
        switch (command) {
            case "count":
                head = "Number of products: \n";
                info = "2\n";
                break;
            case "sum":
                head = "Summary price: \n";
                info = "100000070\n";
                break;
            case "max":
                head = "<h1>Product with max price: </h1>\n";
                info = "Moon\t100000000</br>\n";
                break;
            default:
                head = "<h1>Product with min price: </h1>\n";
                info = "rice\t70</br>\n";
                break;
        }

        return "<html><body>\n" + head + info + "</body></html>\n";
    }

    @Test
    public void countTest() throws IOException {
        String temp = makeHtml("count");
        assertCommandResult("count", temp);
    }

    @Test
    public void sumTest() throws IOException {
        String temp = makeHtml("sum");
        assertCommandResult("sum", temp);
    }

    @Test
    public void maxTest() throws IOException {
        String temp = makeHtml("max");
        assertCommandResult("max", temp);
    }

    @Test
    public void minTest() throws IOException {
        String temp = makeHtml("min");
        assertCommandResult("min", temp);
    }
}

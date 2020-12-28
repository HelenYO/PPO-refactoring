package ru.akirakozov.sd.refactoring.html_answer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseMaker {

    public static void okResponse(HttpServletResponse response, String responseBody) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(responseBody);
    }
}

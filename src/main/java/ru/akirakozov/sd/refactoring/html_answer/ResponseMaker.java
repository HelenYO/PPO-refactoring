package ru.akirakozov.sd.refactoring.html_answer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseMaker {

    public static String funcResponse(String header, Boolean isHeader, String info) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html><body>\n");
        if(isHeader) {
            stringBuilder.append("<h1>");
        }
        stringBuilder.append(header);
        if(isHeader) {
            stringBuilder.append("</h1>\n");
        }
        stringBuilder.append(info).append("\n</body></html>");
        return stringBuilder.toString();
    }

    public static void okResponse(HttpServletResponse response, String responseBody) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(responseBody);
    }
}

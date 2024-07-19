package InternshipProj.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc) throws IOException {
        // Set the response status code to 403 Forbidden
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // Set the content type to application/json
        response.setContentType("application/json");

        // Write the custom error message to the response body
        PrintWriter writer = response.getWriter();
        writer.write("{\"message\":\"You do not have permission to access this resource\"}");
        writer.flush();
    }
}

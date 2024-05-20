package openclassroom.p6.paymybuddy.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final String LOG_ID = "[AuthenticationFilter]";

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        logger.info("{} - request user principal: {}", LOG_ID, request.getUserPrincipal());
        logger.info("{} - request: {} {} {}", LOG_ID, request.getMethod(), request.getRequestURI(), request.getContentLength());

        filterChain.doFilter(servletRequest, servletResponse);
    }
}

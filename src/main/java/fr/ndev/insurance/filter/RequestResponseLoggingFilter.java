package fr.ndev.insurance.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        logger.info("Incoming request: {} {}", req.getMethod(), req.getRequestURI());

        chain.doFilter(request, response);

        logger.info("Outgoing response: {}", res.getContentType());
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
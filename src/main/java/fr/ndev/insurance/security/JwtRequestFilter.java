package fr.ndev.insurance.security;

import fr.ndev.insurance.service.AccessTokenCookieService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AccessTokenCookieService accessTokenCookieService;

    @Autowired
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, AccessTokenCookieService accessTokenCookieService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.accessTokenCookieService = accessTokenCookieService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String username = null;
        String jwt = null;

        Optional<Cookie> jwtCookie = accessTokenCookieService.getAccessTokenCookie(request);
        if (jwtCookie.isPresent()) {
            System.out.println("JWT cookie found");
            jwt = jwtCookie.get().getValue();
        } else {
            System.out.println("JWT cookie not found");
            final String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
            }
        }

        if (jwt != null) {
            try {
                username = jwtUtil.extractUsername(jwt);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    CustomUserDetails userDetails = (CustomUserDetails) this.userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                logger.error("Cannot set user authentication: {}", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
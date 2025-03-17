package fr.ndev.insurance.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Objects.requireNonNull;


@Service
public class AccessTokenCookieService {

    @Value("${jwt.expiration}")
    private Long tokenExpiration;

    @Value ("${jwt.refresh_expiration}")
    private Long refreshExpiration;

    @Value("${cookie.access.name}")
    private String cookieAccessName;

    @Value("${cookie.refresh.name}")
    private String cookieRefreshName;

    public Optional<Cookie> getAccessTokenCookie(final HttpServletRequest request) {
        return Arrays.stream(Optional.ofNullable(request.getCookies())
                        .orElse(new Cookie[]{}))
                .filter(cookie -> cookieAccessName.equals(cookie.getName()))
                .findAny();
    }

    public Cookie createAccessTokenCookie(final String cookieValue) {
        requireNonNull(cookieValue);

        final var cookie = new Cookie(cookieAccessName, cookieValue);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge((int) (tokenExpiration / 1000));
        cookie.setPath("/");

        return cookie;
    }

    public Cookie createBlankAccessTokenCookie() {
        final var cookie = new Cookie(cookieAccessName, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(1);
        cookie.setPath("/");

        return cookie;
    }

    public Cookie createRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie(cookieRefreshName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // for HTTPS
        cookie.setPath("/");
        cookie.setMaxAge((int) (refreshExpiration / 1000));
        return cookie;
    }

    public Optional<Cookie> getRefreshTokenCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookieRefreshName.equals(cookie.getName()))
                .findFirst();
    }

    public Cookie createBlankRefreshTokenCookie() {
        Cookie cookie = new Cookie(cookieRefreshName, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
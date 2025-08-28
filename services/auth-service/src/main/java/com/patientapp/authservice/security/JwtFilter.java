package com.patientapp.authservice.security;

import com.patientapp.authservice.modules.user.entity.User;
import com.patientapp.authservice.common.handler.exceptions.UnauthorizedException;
import com.patientapp.authservice.modules.user.service.interfaces.UserService;
import com.patientapp.authservice.common.utils.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CookieUtil cookieUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (isPublicPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = cookieUtil.getAccessTokenFromCookie(request);
        final String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);
        final String userEmail;

        if (refreshToken == null || refreshToken.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (accessToken == null || accessToken.isBlank()) {
            String email = jwtService.extractUsername(refreshToken);
            User user = userService.findByEmailOrThrow(email);

            if (!jwtService.isTokenValid(refreshToken, user)) {
                throw new UnauthorizedException("Token de refresco inválido.");
            }

            final String newAccessToken = jwtService.generateAccessToken(user);
            final ResponseCookie accessTokenCookie = cookieUtil.createAccessTokenCookie(newAccessToken);
            accessToken = newAccessToken;

            response.addHeader(SET_COOKIE, accessTokenCookie.toString());
        }

        userEmail = jwtService.extractUsername(accessToken);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(accessToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();

        // Permite POST a /auth/register y /auth/login sin token
        if (HttpMethod.POST.matches(method) &&
                (path.equals("/auth/register") || path.equals("/auth/login") || path.equals("/auth/activate-account") || path.equals("/auth/refresh"))) {
            return true;
        }

        // Permite GET a /csrf/token
        return HttpMethod.GET.matches(method) && path.equals("/csrf/token");
    }
}

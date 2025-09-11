package com.patientapp.authservice.security;

import com.patientapp.authservice.common.handler.exceptions.UnauthorizedException;
import com.patientapp.authservice.common.utils.CookieUtil;
import com.patientapp.authservice.modules.user.entity.User;
import com.patientapp.authservice.modules.user.service.interfaces.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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

        // Buscar token en header
        String accessToken = null;
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }

        final String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);
        final boolean fromHeader = accessToken != null; // Si viene del header

        // Si no viene del header, buscar cookie
        if (!fromHeader) {
            accessToken = cookieUtil.getAccessTokenFromCookie(request);
        }

        // Si no hay access token ni header ni cookie, no hay nada que hacer
        if ((accessToken == null || accessToken.isBlank()) && !fromHeader) {
            filterChain.doFilter(request, response);
            return;
        }

        // Validar token
        try {
            String userEmail = jwtService.extractUsername(accessToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if (!jwtService.isTokenValid(accessToken, userDetails)) {
                if (fromHeader) {
                    throw new UnauthorizedException("Token inválido");
                } else {
                    // Si es frontend, intentar refresh con refresh token
                    if (refreshToken != null && !refreshToken.isBlank()) {
                        userEmail = jwtService.extractUsername(refreshToken);
                        User user = userService.findByEmailOrThrow(userEmail);
                        if (jwtService.isTokenValid(refreshToken, user)) {
                            accessToken = jwtService.generateAccessToken(user);
                            ResponseCookie accessTokenCookie = cookieUtil.createAccessTokenCookie(accessToken);
                            response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
                            userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                        }
                    }
                }
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            if (fromHeader) {
                throw new UnauthorizedException("Token inválido");
            }
            // Si es frontend y algo falla, ignorar y dejar pasar para manejar como no autenticado
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

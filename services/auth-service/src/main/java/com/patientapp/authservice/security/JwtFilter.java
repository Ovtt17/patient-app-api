package com.patientapp.authservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Permitir paths públicos
        if (isPublicPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener token del header Authorization
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authHeader.substring(7);

        try {
            String userEmail = jwtService.extractUsername(accessToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if (!jwtService.isTokenValid(accessToken, userDetails)) {
                // Token inválido → no autenticado
                filterChain.doFilter(request, response);
                return;
            }

            // Establecer contexto de seguridad
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            accessToken,
                            userDetails.getAuthorities()
                    );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            // Cualquier error → dejar pasar como no autenticado
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();

        return HttpMethod.POST.matches(method) && (
                        path.equals("/auth/login") ||
                        path.equals("/auth/activate-account") ||
                        path.equals("/auth/refresh")
        ) || (HttpMethod.GET.matches(method) && path.equals("/csrf/token"));
    }
}

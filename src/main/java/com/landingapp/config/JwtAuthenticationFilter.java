package com.landingapp.config;

import com.landingapp.service.JwtService;
import com.landingapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT Authentication Filter для обработки JWT токенов в запросах
 * Извлекает токен из заголовка Authorization или из cookies
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTH_COOKIE_NAME = "auth_token";

    private final JwtService jwtService;
    private final UserService userService;

    // Используем конструктор вместо @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    /**
     * Основной метод фильтрации запросов
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // Пропускаем публичные endpoints
            if (isPublicEndpoint(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Извлекаем токен из запроса
            String token = extractTokenFromRequest(request);

            if (StringUtils.hasText(token) && jwtService.validateToken(token)) {
                // Извлекаем username из токена
                String username = jwtService.extractUsername(token);

                if (StringUtils.hasText(username) &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Загружаем UserDetails из базы данных
                    UserDetails userDetails = userService.loadUserByUsername(username);

                    // Создаем объект аутентификации
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // Устанавливаем детали запроса
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Устанавливаем аутентификацию в SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    logger.debug("Authenticated user: {}", username);
                }
            }
        } catch (UsernameNotFoundException e) {
            logger.warn("User not found for JWT token: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
            // Не прерываем цепочку фильтров при ошибках аутентификации
        }

        // Продолжаем цепочку фильтров
        filterChain.doFilter(request, response);
    }

    /**
     * Проверяет, является ли endpoint публичным (не требует аутентификации)
     */
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.startsWith("/api/auth/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-resources/") ||
                path.equals("/favicon.ico");
    }

    /**
     * Извлекает JWT токен из запроса
     * Проверяет сначала заголовок Authorization, затем cookies
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        // 1. Пробуем извлечь из заголовка Authorization
        String bearerToken = request.getHeader(AUTH_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            String token = bearerToken.substring(BEARER_PREFIX.length());
            logger.debug("Extracted JWT from Authorization header");
            return token;
        }

        // 2. Пробуем извлечь из cookies
        String tokenFromCookie = extractTokenFromCookies(request);
        if (StringUtils.hasText(tokenFromCookie)) {
            logger.debug("Extracted JWT from cookies");
            return tokenFromCookie;
        }

        logger.debug("No JWT token found in request");
        return null;
    }

    /**
     * Извлекает токен из cookies запроса
     */
    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AUTH_COOKIE_NAME.equals(cookie.getName()) &&
                        StringUtils.hasText(cookie.getValue())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Определяет, должен ли фильтр применяться к данному запросу
     * Можно переопределить для исключения определенных путей
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // Дополнительные пути, которые не требуют аутентификации
        return path.startsWith("/api/public/") ||
                path.equals("/error");
    }
}
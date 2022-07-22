package com.devit.devitgateway.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";
    @Value("${jwt.secret}")
    private String secret;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = null;

            log.info(String.valueOf(request.getHeaders().containsKey(HEADER_AUTHORIZATION)));
            // Request Header 에 token 이 존재하지 않을 때
            if(!request.getHeaders().containsKey(HEADER_AUTHORIZATION)){
                System.out.println("token 없음");
                return handleUnAuthorized(exchange); // 401 Error
            }

            // Request Header 에서 token 문자열 받아오기
            List<String> headerToken = request.getHeaders().get(HEADER_AUTHORIZATION);
            String tokenString = Objects.requireNonNull(headerToken).get(0);

            if (tokenString.startsWith(TOKEN_PREFIX)) {
                token = tokenString.substring(TOKEN_PREFIX.length());
            }
            log.info(token);

            // 토큰 검증
            Key key = Keys.hmacShaKeyFor(secret.getBytes());

            try {
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            } catch (SecurityException e) {
                log.info("Invalid JWT signature.");
                return handleUnAuthorized(exchange);
            } catch (MalformedJwtException e) {
                log.info("Invalid JWT token.");
                return handleUnAuthorized(exchange);
            } catch (ExpiredJwtException e) {
                log.info("Expired JWT token.");
                return handleUnAuthorized(exchange);
            } catch (UnsupportedJwtException e) {
                log.info("Unsupported JWT token.");
                return handleUnAuthorized(exchange);
            } catch (IllegalArgumentException e) {
                log.info("JWT token compact of handler are invalid.");
                return handleUnAuthorized(exchange);
            }

            return chain.filter(exchange); // 토큰이 일치할 때

        });
    }

    private Mono<Void> handleUnAuthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {

    }
}

package com.devit.devitgateway.security.config;

import com.devit.devitgateway.security.filter.JwtTokenAuthenticationFilter;
import com.devit.devitgateway.security.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class ReactiveSecurityConfig {

    private final ApplicationContext applicationContext;

    private static final String FRONTEND_LOCALHOST = "http://localhost:8080";
    private static final String FRONTEND_STAGING1 = "https://www.devit.shop";
    private static final String FRONTEND_STAGING2 = "https://devit.shop";


    /**
     * ServerHttpSecurity는 스프링 시큐리티의 HttpSecurity와 비슷한 웹플럭스용 클래스다.
     * 이 클래스를 이용하여 모든 요청에 대해 인증 여부 체크를 정의할 수 있다.
     * 이 클래스에 필터를 추가하여, 요청에 인증용 토큰이 존재할 경우 인증이 되도록 설정할 수 있다.
     *
     * SecurityWebFilterChain클래스를 생성하기 전에 DefaultMethodSecurityExpressionHandler클래스가 먼저 구성되어 있어야 한다.
     * <p>
     * authenticationEntryPoint: 애플리케이션이 인증을 요청할 때 해야 할 일들을 정의함.
     * accessDeniedHandler: 인증된 사용자가 필요한 권한을 가지고 있을 않을 때 헤야 할 일들을 정의함.
     *
     * @param http
     * @return
     */
    @Bean
    @DependsOn({"methodSecurityExpressionHandler"})
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         JwtTokenProvider jwtTokenProvider) {
        DefaultMethodSecurityExpressionHandler defaultWebSecurityExpressionHandler = this.applicationContext.getBean(DefaultMethodSecurityExpressionHandler.class);
        defaultWebSecurityExpressionHandler.setPermissionEvaluator(myPermissionEvaluator());
        return http
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint((exchange, ex) -> {
                            return Mono.fromRunnable(() -> {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            });
                        })
                        .accessDeniedHandler((exchange, denied) -> {
                            return Mono.fromRunnable(() -> {
                                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            });
                        }))
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers("/api/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(new JwtTokenAuthenticationFilter(jwtTokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }

    @Bean
    public PermissionEvaluator myPermissionEvaluator() {
        return new PermissionEvaluator() {
            @Override
            public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
                if(authentication.getAuthorities().stream()
                        .filter(grantedAuthority -> grantedAuthority.getAuthority().equals(targetDomainObject))
                        .count() > 0)
                    return true;
                return false;
            }

            @Override
            public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
                return false;
            }
        };
    }

    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.applyPermitDefaultValues();
        corsConfig.addAllowedMethod(HttpMethod.PUT);
        corsConfig.addAllowedMethod(HttpMethod.DELETE);
        corsConfig.addAllowedMethod(HttpMethod.OPTIONS);
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowedOrigins(Arrays.asList(FRONTEND_LOCALHOST, FRONTEND_STAGING1, FRONTEND_STAGING2));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }


}

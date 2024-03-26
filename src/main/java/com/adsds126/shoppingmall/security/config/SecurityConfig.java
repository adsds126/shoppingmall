package com.adsds126.shoppingmall.security.config;

//import com.adsds126.shoppingmall.security.Filter.JwtFilter;
//import com.adsds126.shoppingmall.security.handler.JwtAccessDeniedHandler;
//import com.adsds126.shoppingmall.security.handler.JwtAuthenticationEntryPoint;
//import com.adsds126.shoppingmall.security.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.function.Supplier;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
//    private final TokenProvider tokenProvider;
////    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
////    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//    @Bean
//    public JwtFilter jwtFilter() {
//        return new JwtFilter(tokenProvider);
//    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     *
     * FilterChain
     *
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS 설정 적용
                .authorizeHttpRequests(authorize -> authorize   // HTTP 요청에 대한 인증 및 권한 정의
                        .requestMatchers("/api/authenticate").permitAll()  // 로그인 API
                        .requestMatchers("/h2-console/**").permitAll()  // H2 콘솔 접근 허용
                        .requestMatchers("/api/v1/members/**").permitAll()  // 회원 관련 API
                        .requestMatchers("/resources/**").permitAll()  // 정적 리소스 접근 허용
                        .anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요

                );

        // JwtFilter를 Security Filter Chain에 추가하는 예시
        // 적절한 필터 위치에 따라 addFilterBefore() 또는 addFilterAfter() 메서드 사용

        return http.build();  // HttpSecurity 객체 빌드 (단 한 번만 호출)
    }
    /**
     * cors 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Content-Type", "Authorization", "X-XSRF-token"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    /**
     * 이 메서드는 정적 자원에 대해 보안을 적용하지 않도록 설정한다.
     * 정적 자원은 보통 HTML, CSS, JavaScript, 이미지 파일 등을 의미하며, 이들에 대해 보안을 적용하지 않음으로써 성능을 향상시킬 수 있다.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    /**
     * isAdmin 메소드는 Supplier<Authentication>와 RequestAuthorizationContext를 인자로 받아서 "ADMIN" 역할을 가진 사용자인지 확인한다.
     * 만약 사용자가 "ADMIN" 역할을 가지고 있다면, AuthorizationDecision 객체는 true를 반환하고, 그렇지 않다면 false를 반환한다.
     */
    private AuthorizationDecision isAdmin(
            Supplier<Authentication> authenticationSupplier,
            RequestAuthorizationContext requestAuthorizationContext
    ) {
        return new AuthorizationDecision(
                authenticationSupplier.get()
                        .getAuthorities()
                        .contains(new SimpleGrantedAuthority("ADMIN"))
        );
    }
//    @Bean
//    public SecurityFilterChain filterChain(
//            HttpSecurity httpSecurity,
//            JwtFilter jwtFilter) throws Exception {
//        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  //cors설정 적용
//                .authorizeHttpRequests(authorize -> authorize   //HTTP 요청에 대한 인증 및 권한을 정의
//                        .requestMatchers("/api/authenticate").permitAll()//로그인 api
//                        .requestMatchers("/h2-console/**").permitAll()
//                        .requestMatchers("/api/v1/members/**").permitAll()
//                        .requestMatchers("/resources/**").permitAll()
//                        .anyRequest().authenticated()
//                ).build();
//        return httpSecurity.build();
//    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }


}

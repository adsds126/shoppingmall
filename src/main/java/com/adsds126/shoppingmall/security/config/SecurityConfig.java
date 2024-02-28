package com.adsds126.shoppingmall.security.config;

import com.adsds126.shoppingmall.security.Filter.CustomAuthenticationFilter;
import com.adsds126.shoppingmall.security.Filter.JwtAuthorizationFilter;
import com.adsds126.shoppingmall.security.handler.CustomAuthFailureHandler;
import com.adsds126.shoppingmall.security.handler.CustomAuthSuccessHandler;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.function.Supplier;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
     * "JWT 토큰을 통하여서 사용자를 인증한다." -> 이 메서드는 JWT 인증 필터를 생성한다.
     * JWT 인증 필터는 요청 헤더의 JWT 토큰을 검증하고, 토큰이 유효하면 토큰에서 사용자의 정보와 권한을 추출하여 SecurityContext에 저장한다.
     */
//    @Bean
//    public JwtAuthorizationFilter jwtAuthorizationFilter(CustomUserDetailsService userDetailsService) {
//        return new JwtAuthorizationFilter(userDetailsService);
//    }
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
    /**
     * 1. 커스텀을 수행한 '인증' 필터로 접근 URL, 데이터 전달방식(form) 등 인증 과정 및 인증 후 처리에 대한 설정을 구성하는 메서드다.
     * 이 메서드는 사용자 정의 인증 필터를 생성한다. 이 필터는 로그인 요청을 처리하고, 인증 성공/실패 핸들러를 설정한다.
     *
     * @return CustomAuthenticationFilter
     */
//    @Bean
//    public CustomAuthenticationFilter customAuthenticationFilter(
//            AuthenticationManager authenticationManager,
//            CustomAuthSuccessHandler customAuthSuccessHandler,
//            CustomAuthFailureHandler customAuthFailureHandler
//    ) {
//        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
//        // "/user/login" 엔드포인트로 들어오는 요청을 CustomAuthenticationFilter에서 처리하도록 지정한다.
//        customAuthenticationFilter.setFilterProcessesUrl("/user/login");
//        customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthSuccessHandler);    // '인증' 성공 시 해당 핸들러로 처리를 전가한다.
//        customAuthenticationFilter.setAuthenticationFailureHandler(customAuthFailureHandler);    // '인증' 실패 시 해당 핸들러로 처리를 전가한다.
//        customAuthenticationFilter.afterPropertiesSet();
//        return customAuthenticationFilter;
//    }
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)//csrf 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  //cors설정 적용
                .authorizeHttpRequests(authorize -> authorize   //HTTP 요청에 대한 인증 및 권한을 정의
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/v1/members/**").permitAll()
                        .requestMatchers("/resources/**").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }




//    @Bean
//    public SecurityFilterChain filterChain(
//            HttpSecurity http
//            CustomAuthenticationFilter customAuthenticationFilter,
//            JwtAuthorizationFilter jwtAuthorizationFilter
//    ) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)  //csrf 비활성화
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  //cors설정 적용
//                .authorizeHttpRequests(authorize -> authorize   //HTTP 요청에 대한 인증 및 권한을 정의
//                        .requestMatchers("/resources/**").permitAll()
//                        .requestMatchers("/main/rootPage").permitAll()
//                        .requestMatchers("/api/v1/members/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore((Filter) jwtAuthorizationFilter, BasicAuthenticationFilter.class) //BasicAuthenticationFilter전에 실행되도록
//                //이 필터는 요청 헤더에서 JWT 토큰을 추출하고 해당 토큰의 유효성을 검증하는 역할.
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .formLogin(login -> login
//                        .loginPage("/login")
//                        .successHandler(new SimpleUrlAuthenticationSuccessHandler("/main/rootPage"))
//                        .permitAll()
//                )
//                .addFilterBefore((Filter) customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }


}

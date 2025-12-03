package com.example.park.common;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;// 引入 CORS 相关
import org.springframework.web.cors.CorsConfigurationSource; // 引入 CORS 相关
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // 引入 CORS 相关

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.park.advice.ObjectFieldHandle;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    //パスワード暗号化と検証
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //ユーザーログイン検証規則を設定する
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
            .csrf(csrf -> csrf.disable())//JWTには、cookieが必要ではないので、CSRF保護は禁じることができる
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//JWTつかうので、sessionも必要ではない
            .authorizeHttpRequests(auth -> auth
                // 🌟 解决 CORS 核心问题：允许所有 OPTIONS 请求通过
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/park/user/login","/park/user/register").permitAll()//ユーザーはログインページと新規ページがアクセスできるように設定する
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)//ユーザー名とパスワードの認証前にJWTの検証をする
            .build();
    }

    // 🌟 新增 CORS 配置 Bean，解决 @CrossOrigin 在 Security 环境下失效的问题
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许 Vue 前端地址
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        // 允许所有方法，包括 OPTIONS, POST, GET, etc.
        configuration.setAllowedMethods(Arrays.asList("*"));
        // 允许所有 Header，以便携带 Content-Type, Authorization 等
        configuration.setAllowedHeaders(List.of("*"));
        // 允许携带认证信息（JWT, Cookie等）
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 对所有路径生效
        return source;
    }

    //ユーザーログインリクエスト管理
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public MetaObjectHandler metaObjectHandler(){
        return new ObjectFieldHandle();
    }
}

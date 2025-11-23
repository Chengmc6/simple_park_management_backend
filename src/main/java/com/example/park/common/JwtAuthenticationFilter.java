package com.example.park.common;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// JWT 認証フィルタークラス（リクエストごとに一度だけ実行される）
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    // リクエスト処理の内部ロジック（フィルターの本体）
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // リクエストヘッダーから Authorization トークンを抽出        
        String token=JwtUtils.extractToken(request.getHeader("Authorization"));
        
        // トークンが存在し、有効期限が切れていない場合に認証処理を行う
        if(token!=null && JwtUtils.isTokenExpired(token)){
            Long userId=JwtUtils.getUserID(token);
            String username=JwtUtils.getUsername(token);
            Integer role=JwtUtils.getRole(token);

            // ユーザー詳細情報を構築（パスワードは null、権限は空リスト）
            CustomerUserDetails userDetails=new CustomerUserDetails(
                    userId,
                    username,
                    null,
                    role
            );

            // Spring Security の認証トークンを作成
            UsernamePasswordAuthenticationToken authenticationToken=
                    new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            
            // 認証情報をセキュリティコンテキストに設定
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 次のフィルターまたはコントローラーに処理を渡す
        filterChain.doFilter(request, response);
    }

}

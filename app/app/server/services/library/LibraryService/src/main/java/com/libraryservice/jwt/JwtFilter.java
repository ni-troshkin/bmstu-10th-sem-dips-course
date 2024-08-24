package com.libraryservice.jwt;

import com.auth0.jwk.JwkException;
//import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
//import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";
    private final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtProvider jwtProvider;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
//        System.out.println("FILTER");

        final String token = getTokenFromRequest((HttpServletRequest) request);
//        System.out.println(((HttpServletRequest) request).getParameter("translate"));
        if (token != null) {
            if (jwtProvider.validateToken(token)) {
                try {
                    final Claims claims = jwtProvider.getClaims(token);
                    final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims, token);
                    jwtInfoToken.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication((Authentication) jwtInfoToken);
                } catch (JwkException e) {
                    log.error("JWK exception", e);
                    //                if (response.isCommitted()) {
                    //                    response.reset();
                    //                }
//                    ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
//            System.out.println(claims.toString());

            }
//            else
//                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }


//        else {
//            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////            HttpServletResponse resp = (HttpServletResponse) response;
//            if (resp.isCommitted()) {
//                resp.reset();
//            }
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            resp.setContentType("text/plain");
//            PrintWriter writer = resp.getWriter();
//            writer.write("The token is not valid.");
//            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
//        }
        fc.doFilter(request, response);

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
//        System.out.println(bearer);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}
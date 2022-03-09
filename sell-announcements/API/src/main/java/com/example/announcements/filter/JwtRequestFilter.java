package com.example.announcements.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.announcements.models.User;
import com.example.announcements.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Value("${jwt.hmacSecret}")
    private String hmacSecret;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private JWTVerifier jwtVerifier = null;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        jwtVerifier = JWT.require(Algorithm.HMAC256(hmacSecret)).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorizationHeader = httpServletRequest.getHeader("Authorization");

            // sprawdzamy czy użytkownik wywołuje żądanie z tokenem JWT
            if (authorizationHeader == null || authorizationHeader.length() == 0) {
                logger.severe("No \"Authorization\" header.");
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            String[] authHeaderParts = authorizationHeader.split("\\s"); // to jest wyrażenie regularne, które oznacza rozbicie Stringa według białych znaków (np. spacja)
            // Authorization składa się z 2 części - typu autoryzacji i tokenu
            if (authHeaderParts.length < 2) {
                logger.severe("Invalid \"Authorization\" header - expected type of authorization and token value.");
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            String authType = authHeaderParts[0];
            /**
             * autoryzacja typu Bearer - czyli dostaliśmy jakiś token, który identyfikuje nas w systemie.
             * Jeśli nie jest typu bearer, to nie jest token typu JWT.
             */
            if (!authType.equals("Bearer")) {
                logger.severe("Not a \"Bearer\" authorization type - dropping auth.");
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            /**
             * Sprawdzamy token - sprawdzamy czy ma ID usera oraz czy nie wygasł, jeśli ma ustawioną datę wygaśnięcia.
             */
            String authValue = authHeaderParts[1];
            DecodedJWT jwtToken = jwtVerifier.verify(authValue);

            String userId = jwtToken.getSubject(); // subject w nomenklaturze JWT oznacza jego podmiot - czyli określa dla jakiego usera został wydany token
            if (userId == null) {
                logger.severe("No token subject.");
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            if (jwtToken.getExpiresAt() != null && jwtToken.getExpiresAt().before(new Date())) {
                logger.severe("JWT token is expired.");
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            Optional<User> userOptional = userRepository.findById(Integer.parseInt(jwtToken.getSubject()));
            if (!userOptional.isPresent()) {
                logger.severe("User not found.");
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            User user = userOptional.get();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRole()))
                    .collect(Collectors.toCollection(ArrayList::new))
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            SecurityContextHolder.getContext().setAuthentication(
                    authenticationToken
            );

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (SignatureVerificationException e) {
            logger.severe("Could not verify JWT token: " + e.getMessage());
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}

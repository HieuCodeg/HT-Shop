package com.hieucodeg.service.jwt;

import com.hieucodeg.model.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Component
@Service
public class JwtService {

    private static final String SECRET_KEY = "GoiTenToiNheBanThanHoiCoToiLuonCungChiaSotDeRoiTaLaiCoThemNiemTin";
    private static final long JWT_TOKEN_VALIDITY = 1000L * 60 * 60;
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class.getName());

    public String generateTokenLogin(Authentication authentication) {
        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {0} ", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {0}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {0}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {0}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {0}", e);
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}


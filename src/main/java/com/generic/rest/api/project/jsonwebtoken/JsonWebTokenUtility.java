package com.generic.rest.api.project.jsonwebtoken;

import com.generic.rest.api.project.enumeration.CustomerLevel;
import com.generic.rest.api.project.secuirty.JsonWebTokenAuthenticationProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class JsonWebTokenUtility {

    private static final Logger logger = LoggerFactory.getLogger(JsonWebTokenUtility.class);

    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    private static final JwtParser signedParser = new DefaultJwtParser();
    private static final JwtParser unsignedParser = new DefaultJwtParser();

    public JsonWebTokenUtility() {
    }


    public static String createJsonWebToken(AuthTokenDetailsDTO authTokenDetailsDTO) {
        logger.info("createJsonWebToken()...");
        return Jwts.builder()
                .setSubject(authTokenDetailsDTO.getUserId())
                .claim("environment", authTokenDetailsDTO.getEnvironment())
                .claim("username", authTokenDetailsDTO.getUsername())
                .claim("email", authTokenDetailsDTO.getEmail())
                .claim("customerLevel", authTokenDetailsDTO.getCustomerLevel())
                .claim("logIp", authTokenDetailsDTO.getLogIp())
                .claim("roles", authTokenDetailsDTO.getRoleNames())
                .claim("grantedAuthorities", authTokenDetailsDTO.getGrantedAuthorities())
                .claim("lastPasswordChangeDate", authTokenDetailsDTO.getLastPasswordChangeDate())
                .claim("userZoneId", authTokenDetailsDTO.getUserZoneId())
                .setExpiration(authTokenDetailsDTO.getExpirationDate())
                .signWith(signatureAlgorithm, getSecretKey(authTokenDetailsDTO.getEnvironment())).compact();
    }

    private static Key deserializeKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, signatureAlgorithm.getJcaName());
    }

    private static Key getSecretKey(String environment) {
        if(environment != null) {
            switch (environment) {
                case "test":
                case "local":
                case "dev":
                    return deserializeKey("45d04d07434cfdb44288b98cce426c324a08bf66c615423b532a5ff3c0530ae34c1449f9dba67c5116bdedea9b935cc1f57d1901595d54bade5437ec22d7502f");
                case "uat":
                    return deserializeKey("c3ca258fb744b219f93df0e7d7cbb59c565ea0471153b991b88d27d31dbde20e22627906c64d72e7928c9fc15f4426692f63376244a1f75070dbf432a1689952");
                case "prod":
                    return deserializeKey("5fb9029d83a4f7c7e2c8c993cb53070e442608d449856f5e53ff19203698da683ea7e56ea4b28ac646e715cd33971bb7f02d665acdada179dda7a45d3d57ec4d");
                default:
                    throw new IllegalStateException("Unexpected profile value in getSecretKey: " + environment);
            }
        }
        else {
            throw new IllegalStateException("Unexpected profile value in getSecretKey: null");
        }
    }

    public static AuthTokenDetailsDTO parseAndValidate(String token) {
        Claims claims = parseTokenIntoClaimsWithHandling(token);

        if (claims == null) {
            return null;
        }
        return createTokenDTOFromClaims(claims);
    }

    /**
     * For use when custom exception handling is needed
     *
     * @param token the token to be parsed
     * @return parsed auth token details
     * @throws Exception if anything goes wrong. Caller is responsible to handle any issues
     */
    public static AuthTokenDetailsDTO unsafeParseAndValidate(String token) throws Exception {
        Claims claims = parseTokenIntoClaims(token);

        if (claims == null) {
            return null;
        }

        return createTokenDTOFromClaims(claims);
    }

    private static Claims parseTokenIntoClaims(String token) throws Exception {
        return getClaimsWithoutKey(token);
    }

    private static Claims parseTokenIntoClaimsWithHandling(String token) {
        try {
            return parseTokenIntoClaims(token);
        } catch (ExpiredJwtException expiredJwtException) {
            logger.error("Failed to validate token. Token has expired.");
            return null;
        } catch (Exception ex) {
            logger.error("parseAndValidate() ... EXCEPTION" + ex.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", ex);
        }
    }

    private static AuthTokenDetailsDTO createTokenDTOFromClaims(Claims claims) {
        return new AuthTokenDetailsDTO.Builder()
                .withUserId(claims.getSubject())
                .withEnvironment((String) claims.get("environment"))
                .withUsername((String) claims.get("username"))
                .withEmail((String) claims.get("email"))
                .withCustomerId((String) claims.get("customerId"))
                .withCustomerLevel(CustomerLevel.valueOf((String) claims.get("customerLevel")))
                .withBranchId((String) claims.get("branchId"))
                .withLogIp((String) claims.get("logIp"))
                .withTenant((String) claims.get("tenant"))
                .withRoleNames((List<String>) claims.get("roles"))
                .withGrantedAuthorities((List<String>) claims.get("grantedAuthorities"))
                .withExpirationDate(claims.getExpiration())
                .withLastPasswordChangeDate(Objects.isNull(claims.get("lastPasswordChangeDate")) ? null : new Date((Long) claims.get("lastPasswordChangeDate")))
                .withUserZoneId((String) claims.get("userZoneId"))
                .build();
    }

    private static String serializeKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    private static Claims getClaimsWithoutKey(String token) {
        return Jwts.parser().setSigningKey(getSecretKey(JsonWebTokenAuthenticationProvider.getActiveProfile())).parseClaimsJws(token).getBody();
    }

    private static AuthTokenDetailsDTO getDetailsWithoutKey(String token) {
        return createTokenDTOFromClaims(getClaimsWithoutKey(token));
    }
}

package edu.douglaslima.accessmanagement.api.infra.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);
	
	@Value("${projeto.jwt.secret}")
	private String secret;
	
	@Value("${projeto.jwt.expiration}")
	private int expiration;
	
	/**
	 * Generates a JWT string.
	 * @param user an object of type {@code User} that contains user related information
	 * @return a JWT string
	 */
	public String generateToken(UserDetails user) {
		String token = Jwts.builder()
				.subject(user.getUsername())
				.issuer("access-management-api")
				.issuedAt(new Date())
				.expiration(new Date(new Date().getTime() + expiration))
				.signWith(signingKey())
				.compact();
		return token;
	}
	
	public Key signingKey() {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		return key;
	}
	
	/**
	 * Validates the token argument and return the {@code username} attribute, in case of the token being valid.
	 * @param token a JWT token compact string
	 * @return the {@code username} attribute
	 */
	public String validateToken(String token) {
		try {
			String username = Jwts.parser()
					.verifyWith((SecretKey) signingKey())
					.build()
					.parseSignedClaims(token)
					.getPayload()
					.getSubject();
			return username;
		} catch (IllegalArgumentException e) {
			LOGGER.error("The token is null or empty! " + e.getMessage());
		} catch(UnsupportedJwtException e) {
			LOGGER.error("The token does not match a JWT format. " + e.getMessage());
		} catch (JwtException e) {
			LOGGER.error("The token could not be validated or parsed as required. " + e.getMessage());
		}
		return null;
	}
	
}

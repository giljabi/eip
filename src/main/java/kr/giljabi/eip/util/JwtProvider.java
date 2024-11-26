package kr.giljabi.eip.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.giljabi.eip.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {
	@Value("${jwt.accessTokenSecret}")
	private String accessTokenSecret;// "4RT/2hAAJmC6i2FzmezBbaCVMD9FtsuExyjaVWJ5xRA=";

	@Value("${jwt.accessTokenExpiration}")
	private long accessTokenExpiration; //604800000;

	public String generateJwtToken(User userInfo) throws JsonProcessingException {
		SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecret));
		Date expiration = new Date((new Date()).getTime() + accessTokenExpiration);

		// 클레임 생성
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", userInfo.getUserId());

		ObjectMapper objectMapper = new ObjectMapper();
		String userInfoJson = objectMapper.writeValueAsString(claims);

		return Jwts.builder()
				.setSubject(userInfo.getUserId())
				.claim("userinfo", userInfoJson)
				.setIssuedAt(new Date())
				.setExpiration(expiration)
				.signWith(secretKey)
				.compact();
	}

	public Jws<Claims> getClaims(String token) {
		SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecret));
		Jws<Claims> claims = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);

		return claims;
	}

	public long getAccessTokenExpiration() {
		return accessTokenExpiration;
	}

	public boolean validateToken(String token) {
		SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecret));
		try {
			Jws<Claims> claimsJwts = Jwts.parserBuilder()
					.setSigningKey(secretKey)
					.build()
					.parseClaimsJws(token);

			Date expiration = claimsJwts.getBody().getExpiration();

			if (null != expiration) {
				return !expiration.before(new Date());
			}

			return true;
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}

		return false;
	}
}
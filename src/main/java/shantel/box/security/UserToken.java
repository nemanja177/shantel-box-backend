package shantel.box.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import shantel.box.model.Korisnik;

@Component
public class UserToken {
		@Value("box")
		private String APP_NAME;

		
		@Value("EQ8UuS3fVSKvxXDusPeA7w5ryy333zCe")
		public String SECRET;

		@Value("1200000")
		private int EXPIRES_IN;

		
		@Value("Authorization")
		private String AUTH_HEADER;

		
		private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

		
		public String generateToken(String username, String roles) {
			return Jwts.builder()
					.setIssuer(APP_NAME)
					.setSubject(username)
					.setIssuedAt(new Date())
					.setExpiration(generateExpirationDate())
					.claim("roles", roles)
					.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
		}

		private Date generateExpirationDate() {
			return new Date(new Date().getTime() + EXPIRES_IN);
		}

		public String refreshToken(String token) {
			String refreshedToken;
			try {
				final Claims claims = this.getAllClaimsFromToken(token);
				claims.setIssuedAt(new Date());
				refreshedToken = Jwts.builder()
						.setClaims(claims)
						.setExpiration(generateExpirationDate())
						.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
			} catch (Exception e) {
				refreshedToken = null;
			}
			return refreshedToken;
		}

		public boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
			final Date created = this.getIssuedAtDateFromToken(token);
			return (!(this.isCreatedBeforeLastPasswordReset(created, lastPasswordReset))
					&& (!(this.isTokenExpired(token))));
		}

		public Boolean validateToken(String token, UserDetails userDetails) {
			Korisnik user = (Korisnik) userDetails;
			final String username = getUsernameFromToken(token);
			final Date created = getIssuedAtDateFromToken(token);
			
			return (username != null && username.equals(userDetails.getUsername()));
		}

		public String getUsernameFromToken(String token) {
			String username;
			try {
				final Claims claims = this.getAllClaimsFromToken(token);
				username = claims.getSubject();
			} catch (Exception e) {
				username = null;
			}
			return username;
		}

		public Date getIssuedAtDateFromToken(String token) {
			Date issueAt;
			try {
				final Claims claims = this.getAllClaimsFromToken(token);
				issueAt = claims.getIssuedAt();
			} catch (Exception e) {
				issueAt = null;
			}
			return issueAt;
		}

		public String getAudienceFromToken(String token) {
			String audience;
			try {
				final Claims claims = this.getAllClaimsFromToken(token);
				audience = claims.getAudience();
			} catch (Exception e) {
				audience = null;
			}
			return audience;
		}

		public Date getExpirationDateFromToken(String token) {
			Date expiration;
			try {
				final Claims claims = this.getAllClaimsFromToken(token);
				expiration = claims.getExpiration();
			} catch (Exception e) {
				expiration = null;
			}
			return expiration;
		}

		public int getExpiredIn() {
			return EXPIRES_IN;
		}

		public String getToken(HttpServletRequest request) {
			String authHeader = getAuthHeaderFromHeader(request);

			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				return authHeader.substring(7);
			}

			return null;
		}

		public String getAuthHeaderFromHeader(HttpServletRequest request) {
			return request.getHeader(AUTH_HEADER);
		}
		
		private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
			return (lastPasswordReset != null && created.before(lastPasswordReset));
		}

		private Boolean isTokenExpired(String token) {
			final Date expiration = this.getExpirationDateFromToken(token);
			return expiration.before(new Date());
		}

		// Funkcija za citanje svih podataka iz JWT tokena
		private Claims getAllClaimsFromToken(String token) {
			Claims claims;
			try {
				claims = Jwts.parser()
						.setSigningKey(SECRET)
						.parseClaimsJws(token)
						.getBody();
			} catch (Exception e) {
				claims = null;
			}
			return claims;
		}
}

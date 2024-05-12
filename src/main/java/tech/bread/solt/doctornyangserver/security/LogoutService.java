package tech.bread.solt.doctornyangserver.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import tech.bread.solt.doctornyangserver.model.entity.Tokens;
import tech.bread.solt.doctornyangserver.repository.TokensRepo;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokensRepo tokensRepo;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        tokensRepo.findByToken(jwt).ifPresent(tokensRepo::delete);
    }
}

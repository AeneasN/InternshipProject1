package InternshipProj.api.spring_security;

import InternshipProj.api.ips.IPRepository;
import InternshipProj.api.ips.IPService;
import InternshipProj.api.users.UserIDRepository;
import InternshipProj.api.users.Userid;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PersSecurityFilter extends OncePerRequestFilter {
    private final UserIDRepository userIDRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String ipaddress = request.getRemoteAddr();
        try {
            String apikey = request.getHeader("api-key");

            if (apikey == null) {
                chain.doFilter(request, response);
                return;
            }

            long username = Long.parseLong(apikey);

            Userid userid = userIDRepository.findById(username)
                    .orElseThrow(() -> new ServletException("User not found"));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userid, null, List.of(new SimpleGrantedAuthority("USER")));

            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        } catch (Exception ex) {
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            }
        }
    }
}

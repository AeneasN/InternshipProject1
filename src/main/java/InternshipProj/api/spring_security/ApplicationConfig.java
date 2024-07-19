package InternshipProj.api.spring_security;

import InternshipProj.api.users.UserIDRepository;
import io.ipinfo.api.IPinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {
    @Autowired
    private UserIDRepository userIDRepository;
    private static final String ipinfoToken = "300a19e6ae2ffe";
    private static final String METEOMATICS_USERNAME = "stratify_nicholas_aeneas";
    private static final String METEOMATICS_PASSWORD = "0DusI8zD1i";


    @Bean
    public IPinfo iPinfo(){
        IPinfo iPinfo = new IPinfo.Builder()
                .setToken(ipinfoToken)
                .build();
        return iPinfo;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> (UserDetails) userIDRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);

        return authProvider;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(
                new BasicAuthenticationInterceptor(METEOMATICS_USERNAME, METEOMATICS_PASSWORD));
        return restTemplate;
    }
}

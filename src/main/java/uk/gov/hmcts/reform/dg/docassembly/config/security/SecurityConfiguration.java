package uk.gov.hmcts.reform.dg.docassembly.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import uk.gov.hmcts.reform.auth.checker.core.RequestAuthorizer;
import uk.gov.hmcts.reform.auth.checker.core.service.Service;
import uk.gov.hmcts.reform.auth.checker.spring.serviceonly.AuthCheckerServiceOnlyFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthCheckerServiceOnlyFilter authCheckerServiceOnlyFilter;

    public SecurityConfiguration(final RequestAuthorizer<Service> serviceRequestAuthorizer,
                                 final AuthenticationManager authenticationManager) {
        this.authCheckerServiceOnlyFilter = new AuthCheckerServiceOnlyFilter(serviceRequestAuthorizer);
        this.authCheckerServiceOnlyFilter.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/swagger-ui.html",
            "/webjars/springfox-swagger-ui/**",
            "/swagger-resources/**",
            "/v2/**",
            "/health",
            "/health/liveness",
            "/status/health",
            "/loggers/**",
            "/");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .addFilter(authCheckerServiceOnlyFilter)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated();
    }
}

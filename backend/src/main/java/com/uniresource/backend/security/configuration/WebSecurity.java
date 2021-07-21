package com.uniresource.backend.security.configuration;

import com.uniresource.backend.security.filter.JWTAuthenticationFilter;
import com.uniresource.backend.security.filter.JWTAuthorizationFilter;
import com.uniresource.backend.security.handler.UserLogoutSuccessHandler;
import com.uniresource.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
	private UserLogoutSuccessHandler userLogoutSuccessHandler;

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.cors()
        .and()
        .csrf().disable()
        .authorizeRequests()//.mvcMatchers(HttpMethod.GET, "/**").permitAll() // GET requests don't need auth
        .antMatchers(JWTConfig.antMatchers.split(","))
        .permitAll()
        .anyRequest()
        .authenticated()
        .and().logout().logoutUrl("/logout")
		.logoutSuccessHandler(userLogoutSuccessHandler) 
        .and()
        .addFilter(this.getJWTAuthenticationFilter())
        .addFilter(new JWTAuthorizationFilter(authenticationManager()))
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    public JWTAuthenticationFilter getJWTAuthenticationFilter() throws Exception{
        final JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/login");

        return filter;
    }
    
}

package com.example.multitenancy.multitenancy.infra.security;

import static com.example.multitenancy.multitenancy.infra.tenant.TenantContext.DEFAULT_TENANTID;
import static com.example.multitenancy.multitenancy.infra.tenant.TenantContext.KEY_TENANTID_SESSION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AutenticacaoFilter implements AuthenticationProvider, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1534330351565247392L;
	
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		logger.info("AUTENTICANDO USUARIO....");
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		UserDetails userDetails  = null;
		if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
			userDetails = new User(username, password, grantedAuthorities);
		}else {
			throw new UsernameNotFoundException("Usuário e senha em branco");
		}
			if("admin".equals(username) && "admin".equals(password) || "user".equals(username) && "user".equals(password)) {
				
				if(username.equals("user")) {
	                logger.info("alter to tenant:  tenant_2");
					alterTenantByHttpSession("tenant_2");
				}
				logger.info("AUTENTICADO!");
				
				return new UsernamePasswordAuthenticationToken(userDetails, password, grantedAuthorities);
		}else{
			throw new UsernameNotFoundException("Usuário ou senha não localizado!");
		}
		
	}

	
	public void alterTenantByHttpSession(String tenant)
    {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true); // true == allow create
             session.setAttribute(KEY_TENANTID_SESSION, tenant);
             logger.info(String.format("current tenant is: %s", session.getAttribute(KEY_TENANTID_SESSION)));
    }

	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}

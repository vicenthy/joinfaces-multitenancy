package com.example.multitenancy.multitenancy.infra.tenant.config;

import static com.example.multitenancy.multitenancy.infra.tenant.TenantContext.DEFAULT_TENANTID;
import static com.example.multitenancy.multitenancy.infra.tenant.TenantContext.KEY_TENANTID_SESSION;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7881761973590280332L;
	private Logger logger = LoggerFactory.getLogger(getClass());


	@Override
	public String resolveCurrentTenantIdentifier() {
		String tenantId = resolveTenantByHttpSession();
        logger.info("Tenant resolved: " + tenantId);
		return tenantId;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}
	
	public String resolveTenantByHttpSession()
	    {
	        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	        if(attr != null){
	            HttpSession session = attr.getRequest().getSession(false); // true == allow create
	            if(session != null){
	                String tenant = (String) session.getAttribute(KEY_TENANTID_SESSION);
	                if(tenant != null){
	        	        logger.info("Tenant resolved in session is: " + KEY_TENANTID_SESSION);
	                    return tenant;
	                }
	            }
	        }
	        //otherwise return default tenant
	        logger.info("Tenant resolved in session is: " + DEFAULT_TENANTID);
	        return DEFAULT_TENANTID;
	    }


}

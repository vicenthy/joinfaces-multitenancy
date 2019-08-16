package com.example.multitenancy.multitenancy.infra.tenant.config;

import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.multitenancy.multitenancy.infra.tenant.FlywayInitializer;

@Component
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	@Autowired
	private Map<String, DataSource> dataSourcesMtApp;
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 
	 */
	private static final long serialVersionUID = -7626307859296333207L;

	@Override
	protected DataSource selectAnyDataSource() {
		logger.info("CONNECT TENANT: {}", dataSourcesMtApp);
		return dataSourcesMtApp.values().stream().findFirst().get();
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		new FlywayInitializer().migrate(dataSourcesMtApp.get(tenantIdentifier));
		return dataSourcesMtApp.get(tenantIdentifier);
	}

	
	
}

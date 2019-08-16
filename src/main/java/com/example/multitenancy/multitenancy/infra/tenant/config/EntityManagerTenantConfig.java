package com.example.multitenancy.multitenancy.infra.tenant.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.multitenancy.multitenancy.domain.entity.Cliente;
import com.example.multitenancy.multitenancy.infra.tenant.config.MasterDatabaseConfigProperties.DataSourceProperties;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties({ MasterDatabaseConfigProperties.class, JpaProperties.class })
public class EntityManagerTenantConfig {

	
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private JpaProperties jpaProperties;
	@Autowired
	private MasterDatabaseConfigProperties multitenancyProperties;

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean
	public MultiTenantConnectionProvider multiTenantConnectionProvider() {
		return new MultiTenantConnectionProviderImpl();
	}

	@Bean
	public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
		return new CurrentTenantIdentifierResolverImpl();
	}

	@Bean
	public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
		return entityManagerFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean localEntityManager(
			MultiTenantConnectionProviderImpl multiTenantConnectionProvider,
			CurrentTenantIdentifierResolverImpl currentTenantIdentifierResolver) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		Map<String, Object> hibernateProps = new LinkedHashMap<>();
		hibernateProps.putAll(this.jpaProperties.getProperties());
		hibernateProps.put(Environment.HBM2DDL_AUTO, "update");
		hibernateProps.put(Environment.SHOW_SQL, "true");
		hibernateProps.put(Environment.FORMAT_SQL, "true");
		hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
		hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
		hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
		entityManagerFactoryBean.setPackagesToScan(Cliente.class.getPackage().getName());
		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		entityManagerFactoryBean.setJpaPropertyMap(hibernateProps);
		this.jpaProperties.getProperties().values().forEach(System.out::println);
		return entityManagerFactoryBean;
	}

	@Primary
	@Bean(name = "dataSourcesMtApp")
	public Map<String, DataSource> dataSourcesMtApp() {
		Map<String, DataSource> result = new HashMap<>();
		
		for (DataSourceProperties dsProperties : this.multitenancyProperties.getDatasources()) {
			DataSource dataSource = DataSourceBuilder.create()
					.url(dsProperties.getUrl())
					.username(dsProperties.getUsername())
					.password(dsProperties.getPassword())
					.driverClassName(dsProperties.getDriverClassName())
					.build();

			result.put(dsProperties.getTenantId(), dataSource);
		}
		return result;
	}
}

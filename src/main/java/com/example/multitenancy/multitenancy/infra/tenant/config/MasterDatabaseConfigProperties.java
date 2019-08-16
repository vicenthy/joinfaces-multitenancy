package com.example.multitenancy.multitenancy.infra.tenant.config;

import java.io.Serializable;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties("multitenancy.db")
public class MasterDatabaseConfigProperties implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1387150737979789128L;

	private List<DataSourceProperties> datasources;


	public static class DataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {

		private String tenantId;

		public String getTenantId() {
			return tenantId;
		}

		public void setTenantId(String tenantId) {
			this.tenantId = tenantId;
		}
	}

	

	public List<DataSourceProperties> getDatasources() {
		return datasources;
	}

	public void setDatasources(List<DataSourceProperties> datasources) {
		this.datasources = datasources;
	}
	
	
	@Override
	public String toString() {
		return "MasterDatabaseConfigProperties [dataSourcesProps=" + datasources + "]";
	}

}

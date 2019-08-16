package com.example.multitenancy.multitenancy.infra.tenant;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;

public class FlywayInitializer {
	public void migrate(DataSource dataSource) {
		ClassicConfiguration configuration = new ClassicConfiguration();
		configuration.setDataSource(dataSource);
		configuration.setBaselineOnMigrate(true);
		Flyway flyway = new Flyway(configuration);
		flyway.migrate();	
	}
}

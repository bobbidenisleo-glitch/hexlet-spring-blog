package io.hexlet.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @Profile("development")
    public DataSource devDataSource() {
        System.out.println("=== Development profile: H2 File Database ===");
        return DataSourceBuilder.create()
                .url("jdbc:h2:file:./devdb")
                .driverClassName("org.h2.Driver")
                .username("sa")
                .password("")
                .build();
    }

    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        System.out.println("=== Test profile: H2 In-Memory Database ===");
        return DataSourceBuilder.create()
                .url("jdbc:h2:mem:testdb")
                .driverClassName("org.h2.Driver")
                .username("sa")
                .password("")
                .build();
    }

    @Bean
    @Profile("production")
    public DataSource prodDataSource() {
        System.out.println("=== Production profile: PostgreSQL Database ===");
        return DataSourceBuilder.create()
                .url(System.getenv("DATABASE_URL"))
                .username(System.getenv("DATABASE_USERNAME"))
                .password(System.getenv("DATABASE_PASSWORD"))
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}

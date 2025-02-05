package com.test.task.comparusmultidbread.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Configuration
public class DatasourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "data-sources")
    public List<DataSourceProperties> dataSourcePropertiesList() {
        return new ArrayList<>();
    }

    @Bean
    public Map<DataSource, DataSourceProperties> dataSources(List<DataSourceProperties> propertiesList) {
        Map<DataSource, DataSourceProperties> dataSources = new HashMap<>();
        propertiesList.forEach(props -> {
            DataSource dataSource = DataSourceBuilder.create()
                .url(props.getUrl())
                .username(props.getUser())
                .password(props.getPassword())
                .build();
            dataSources.put(dataSource, props);
        });
        return dataSources;
    }

    @Bean
    @Primary
    public DataSource primaryDatasource(Map<DataSource, DataSourceProperties> dataSources) {
        return dataSources.entrySet().stream()
            .findFirst()
            .map(Entry::getKey)
            .orElseThrow(() -> new BeanInitializationException("Required at least one datasource."));
    }

    @Bean
    public Map<JdbcTemplate, DataSourceProperties> jdbcTemplates(Map<DataSource, DataSourceProperties> dataSources) {
        Map<JdbcTemplate, DataSourceProperties> templates = new HashMap<>();
        dataSources.forEach((dataSource, sourceProperties) -> {
            JdbcTemplate template = new JdbcTemplate(dataSource);
            templates.put(template, sourceProperties);
        });
        return templates;
    }
}

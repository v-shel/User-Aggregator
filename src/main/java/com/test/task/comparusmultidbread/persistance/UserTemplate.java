package com.test.task.comparusmultidbread.persistance;

import com.test.task.comparusmultidbread.config.DataSourceProperties;
import com.test.task.comparusmultidbread.model.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTemplate {

    private final Map<JdbcTemplate, DataSourceProperties> jdbcTemplates;

    public List<User> getUsersFromEachDatasource() {
        List<User> users = new ArrayList<>();
        jdbcTemplates.forEach((jdbcTemplate, properties) -> {
            Map<String, String> columnsMapping = properties.getMapping();

            String query = String.format("SELECT %s AS id, %s AS username, %s AS name, %s AS surname FROM %s",
                columnsMapping.get("id"),
                columnsMapping.get("username"),
                columnsMapping.get("name"),
                columnsMapping.get("surname"),
                properties.getTable());

            List<User> userList = jdbcTemplate.query(query, userRowMapper());
            users.addAll(userList);
            log.info("Got {} users from table: {}", userList.size(), properties.getTable());
        });
        return users;
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
            rs.getString("id"),
            rs.getString("username"),
            rs.getString("name"),
            rs.getString("surname")
        );
    }
}

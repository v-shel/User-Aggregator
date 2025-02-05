package com.test.task.comparusmultidbread;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.test.task.comparusmultidbread.config.DataSourceProperties;
import com.test.task.comparusmultidbread.controller.UserController;
import com.test.task.comparusmultidbread.model.entity.User;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class UserAggregatorApplicationTests {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:latest")
        .withDatabaseName("test_db1")
        .withUsername("user1")
        .withPassword("pass1")
        .withInitScripts("db/scripts/init-db.sql")
        .withExposedPorts(5432)
        .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
            new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(55432), new ExposedPort(5432)))
        ));

    @Autowired
    private UserController userController;
    @Autowired
    private Map<JdbcTemplate, DataSourceProperties> templates;

    @PostConstruct
    public void init() {
        POSTGRES.start();

        JdbcTemplate testDb2Template = templates.entrySet().stream()
            .filter(entry -> entry.getValue().getUrl().contains("test_db2"))
            .findFirst()
            .map(Entry::getKey)
            .orElseThrow(() -> new NoSuchElementException("No expected datasource."));

        testDb2Template.execute("""
            DROP TABLE IF EXISTS user_table;
            CREATE TABLE user_table (
                ldap_login varchar primary key not null,
                name       varchar,
                surname    varchar
            );
            INSERT INTO user_table (ldap_login, name, surname) VALUES ('ldap_login1', 'Jain', 'Melt');
            INSERT INTO user_table (ldap_login, name, surname) VALUES ('ldap_login2', 'Keith', 'Brooks');
        """);
    }

    @Test
    void testGetUsers() {
        List<User> users = userController.getUsers();
        assertThat(users).isNotNull().size().isEqualTo(4);
        assertThat(users)
            .extracting(User::getId)
            .allMatch(StringUtils::isNotBlank);

        assertThat(users)
            .extracting(User::getUsername)
            .containsExactlyInAnyOrderElementsOf(List.of("login1", "login2", "ldap_login1", "ldap_login2"));

        assertThat(users)
            .extracting(User::getName)
            .containsExactlyInAnyOrderElementsOf(List.of("John", "Ralph", "Jain", "Keith"));

        assertThat(users)
            .extracting(User::getSurname)
            .containsExactlyInAnyOrderElementsOf(List.of("Smith", "Cheers", "Melt", "Brooks"));
    }
}

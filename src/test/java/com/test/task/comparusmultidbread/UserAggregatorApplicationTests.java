package com.test.task.comparusmultidbread;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.test.task.comparusmultidbread.controller.UserController;
import com.test.task.comparusmultidbread.model.entity.User;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class UserAggregatorApplicationTests {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES1 = new PostgreSQLContainer<>("postgres:latest")
        .withDatabaseName("test_db1")
        .withUsername("user1")
        .withPassword("pass1")
        .withInitScripts("db/scripts/init-db1.sql")
        .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
            new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(55432), new ExposedPort(5432)))
        ));

    @Container
    private static final PostgreSQLContainer<?> POSTGRES2 = new PostgreSQLContainer<>("postgres:latest")
        .withDatabaseName("test_db2")
        .withUsername("user2")
        .withPassword("pass2")
        .withInitScripts("db/scripts/init-db2.sql")
        .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
            new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(55433), new ExposedPort(5432)))
        ));

    @Autowired
    private UserController userController;

    @PostConstruct
    public void init() {
        POSTGRES1.start();
        POSTGRES2.start();
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

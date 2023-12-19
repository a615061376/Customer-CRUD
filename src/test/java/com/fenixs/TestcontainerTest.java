package com.fenixs;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TestcontainerTest extends AbstractTestcontainer {

    @Test
    void canStartPostgreDB() {
        assertThat(container.isRunning()).isTrue();
        assertThat(container.isCreated()).isTrue();
    }

}

package com.fenixs.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        CustomerRowMapper rowMapper = new CustomerRowMapper();
        ResultSet rs = mock(ResultSet.class);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("name")).thenReturn("mock");
        when(rs.getString("email")).thenReturn("mock@gmail.com");
        when(rs.getInt("age")).thenReturn(50);

        CustomerEntity actual = rowMapper.mapRow(rs, 1);
        CustomerEntity expected = new CustomerEntity(1L, "mock","mock@gmail.com",50);

        assertThat(actual).isEqualTo(expected);
    }
}
package com.fenixs.customer;



import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper rowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }


    @Override
    public List<CustomerEntity> selectAllCustomers() {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<CustomerEntity> selectCustomerById(Long id) {
        var sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(CustomerEntity customerEntity) {
        var sql = """
                INSERT INTO customer(name, email, age)
                VALUES (?, ? ,?)
                """;
        int insertResult = jdbcTemplate.update(sql, customerEntity.getName(), customerEntity.getEmail(), customerEntity.getAge());
        if (insertResult == 1)
            System.out.println("Insert customer successfully.");
    }

    @Override
    public boolean existPersonWithId(Long id) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return result != null && result > 0;
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?
                """;
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return result != null && result > 0;
    }

    @Override
    public void updateCustomer(CustomerEntity updateObject) {
        Long id = updateObject.getId();
        String name = updateObject.getName();
        String email = updateObject.getEmail();
        Integer age = updateObject.getAge();

        if (name != null) {
            String sql = "UPDATE customer SET name = ? WHERE id = ?";
            int updateResult = jdbcTemplate.update(sql, name, id);
            if (updateResult == 1)
                System.out.println("update customer name successfully.");
        }

        if (email != null) {
            String sql = "UPDATE customer SET email = ? WHERE id = ?";
            int updateResult = jdbcTemplate.update(sql, email, id);
            if (updateResult == 1)
                System.out.println("update customer email successfully.");
        }

        if (age != null) {
            String sql = "UPDATE customer SET age = ? WHERE id = ?";
            int updateResult = jdbcTemplate.update(sql, age, id);
            if (updateResult == 1)
                System.out.println("update customer age successfully.");
        }
    }

    @Override
    public void deleteCustomerById(Long id) {
        var sql = """
                DELETE 
                FROM customer
                WHERE id = ?
                """;
        int deleteResult = jdbcTemplate.update(sql, id);
        if (deleteResult == 1)
            System.out.println("Delete customer successfully.");

    }
}

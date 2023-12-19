package com.fenixs.customer;

import com.fenixs.exception.DuplicateResourceException;
import com.fenixs.exception.RequestValidationException;
import com.fenixs.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void getCustomer() {
        Long id = 1L;
        CustomerEntity mock = new CustomerEntity(id, "faker", "faker@gamil.com", 50);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(mock));
        CustomerEntity actual = underTest.getCustomer(1L);
        assertThat(actual).isEqualTo(mock);
    }

    @Test
    void getCustomerFailWhenEmptyOptional() {
        Long id = 1L;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "customer with id [%s] not found.".formatted(id)
                );
    }

    @Test
    void addCustomer() {
        String email = "faker@gamil.com";
        when(customerDao.existPersonWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest mock = new CustomerRegistrationRequest("faker", email, 60);
        underTest.addCustomer(mock);
        ArgumentCaptor<CustomerEntity> customerEntityArgumentCaptor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerDao).insertCustomer(customerEntityArgumentCaptor.capture());
        CustomerEntity actual = customerEntityArgumentCaptor.getValue();
        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(mock.name());
        assertThat(actual.getEmail()).isEqualTo(mock.email());
        assertThat(actual.getAge()).isEqualTo(mock.age());
    }

    @Test
    void addCustomerFailWhenEmailExists() {
        String email = "faker@gamil.com";
        when(customerDao.existPersonWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest mock = new CustomerRegistrationRequest("faker", email, 60);
        assertThatThrownBy(() -> underTest.addCustomer(mock))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage(
                        "Email already taken"
                );
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void updateCustomerAllProperties() {
        Long id = 1L;
        CustomerEntity mock = new CustomerEntity(id, "faker", "faker@gamil.com", 50);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(mock));

        String newName = "Alex";
        String newEmail = "alex@gmail.com";
        int newAge = 66;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(newName, newEmail, newAge);
        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id, updateRequest);
        ArgumentCaptor<CustomerEntity> customerEntityArgumentCaptor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerDao).updateCustomer(customerEntityArgumentCaptor.capture());
        CustomerEntity actual = customerEntityArgumentCaptor.getValue();

        assertThat(actual.getName()).isEqualTo(updateRequest.name());
        assertThat(actual.getEmail()).isEqualTo(updateRequest.email());
        assertThat(actual.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void updateCustomerOnlyName() {
        Long id = 1L;
        CustomerEntity mock = new CustomerEntity(id, "faker", "faker@gamil.com", 50);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(mock));

        String newName = "Alex";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(newName, null, null);

        underTest.updateCustomer(id, updateRequest);
        ArgumentCaptor<CustomerEntity> customerEntityArgumentCaptor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerDao).updateCustomer(customerEntityArgumentCaptor.capture());
        CustomerEntity actual = customerEntityArgumentCaptor.getValue();

        assertThat(actual.getName()).isEqualTo(updateRequest.name());
        assertThat(actual.getEmail()).isEqualTo(mock.getEmail());
        assertThat(actual.getAge()).isEqualTo(mock.getAge());
    }

    @Test
    void updateCustomerOnlyEmail() {
        Long id = 1L;
        CustomerEntity mock = new CustomerEntity(id, "faker", "faker@gamil.com", 50);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(mock));

        String newEmail = "alex@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);
        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id, updateRequest);
        ArgumentCaptor<CustomerEntity> customerEntityArgumentCaptor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerDao).updateCustomer(customerEntityArgumentCaptor.capture());
        CustomerEntity actual = customerEntityArgumentCaptor.getValue();

        assertThat(actual.getName()).isEqualTo(mock.getName());
        assertThat(actual.getEmail()).isEqualTo(updateRequest.email());
        assertThat(actual.getAge()).isEqualTo(mock.getAge());
    }

    @Test
    void updateCustomerOnlyAge() {
        Long id = 1L;
        CustomerEntity mock = new CustomerEntity(id, "faker", "faker@gamil.com", 50);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(mock));

        int newAge = 66;
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, newAge);

        underTest.updateCustomer(id, updateRequest);
        ArgumentCaptor<CustomerEntity> customerEntityArgumentCaptor = ArgumentCaptor.forClass(CustomerEntity.class);
        verify(customerDao).updateCustomer(customerEntityArgumentCaptor.capture());
        CustomerEntity actual = customerEntityArgumentCaptor.getValue();

        assertThat(actual.getName()).isEqualTo(mock.getName());
        assertThat(actual.getEmail()).isEqualTo(mock.getEmail());
        assertThat(actual.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void updateCustomerFailWhenNoChange() {
        Long id = 1L;
        CustomerEntity mock = new CustomerEntity(id, "faker", "faker@gamil.com", 50);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(mock));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(mock.getName(), mock.getEmail(), mock.getAge());

        assertThatThrownBy(() ->  underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage(
                        "no data changes found!"
                );
        verify(customerDao, never()).updateCustomer(any());

    }

    @Test
    void updateCustomerFailWhenEmailTaken() {
        Long id = 1L;
        CustomerEntity mock = new CustomerEntity(id, "faker", "faker@gamil.com", 50);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(mock));

        String newEmail = "alex@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);
        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(true);

        assertThatThrownBy(() ->  underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage(
                        "Email already taken"
                );
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        Long id = 1L;
        when(customerDao.existPersonWithId(id)).thenReturn(true);
        underTest.deleteCustomerById(id);
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void deleteCustomerByIdFailWhenIdNotExists() {
        Long id = 1L;
        when(customerDao.existPersonWithId(id)).thenReturn(false);
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "customer with id [%s] not found.".formatted(id)
                );
        verify(customerDao, never()).deleteCustomerById(id);
    }
}
package com.example.demo;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.Customer;
import com.example.CustomerRepository;


@Testcontainers
@SpringBootTest
@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Container
    public static PostgreSQLContainer container = new PostgreSQLContainer()
    .withUsername("user")
    .withPassword("password")
    .withDatabaseName("test");

  @Autowired
  private CustomerRepository customerRepository;

  // requires Spring Boot >= 2.2.6
  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.password", container::getPassword);
    registry.add("spring.datasource.username", container::getUsername);
	registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
  }

  @Test
    void testSaveCustomer() {
        Customer customer = new Customer();
        customer.setName("Testcontainers");
        customer = customerRepository.save(customer);
        
        assertNotNull(customer.getCustumerid());
    }

    @Test
    void testFindCustomerById() {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer = customerRepository.save(customer);

        Optional<Customer> foundCustomer = customerRepository.findById(customer.getCustumerid());
        assertTrue(foundCustomer.isPresent());
        assertEquals("John Doe", foundCustomer.get().getName());
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = new Customer();
        customer.setName("Jane Doe");
        customer = customerRepository.save(customer);

        customer.setName("Jane Smith");
        customer = customerRepository.save(customer);

        Optional<Customer> updatedCustomer = customerRepository.findById(customer.getCustumerid());
        assertTrue(updatedCustomer.isPresent());
        assertEquals("Jane Smith", updatedCustomer.get().getName());
    }

    @Test
    void testDeleteCustomer() {
        Customer customer = new Customer();
        customer.setName("To Be Deleted");
        customer = customerRepository.save(customer);

        customerRepository.deleteById(customer.getCustumerid());
        Optional<Customer> deletedCustomer = customerRepository.findById(customer.getCustumerid());
        assertFalse(deletedCustomer.isPresent());
    }

}

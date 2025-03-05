package com.example.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * in a production application, you would likely more data access methods
 * specially methods that my have a complex query expressions attached
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByName(String name);

    @Override
    List<Employee> findAll();

    /**
     * Custom query to find employees whose email ends with the given domain.
     * Example usage:
     * findEmplyeedByOrganizationDomain("ua.pt")
     * would return all employees with emails like 'someone@ua.pt'.
     */
    @Query("SELECT e FROM Employee e WHERE e.email LIKE concat('%', :domain)")
    List<Employee> findEmplyeedByOrganizationDomain(@Param("domain") String domain);


}


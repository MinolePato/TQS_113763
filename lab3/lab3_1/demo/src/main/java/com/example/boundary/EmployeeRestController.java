package com.example.boundary;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.data.Employee;
import com.example.data.EmployeeDTO;
import com.example.service.EmployeeService;

/**
 * API endpoints. Try with Postman or curl
 * $curl -v http://localhost:8080/api/employees
 */
@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    /**
     * Using constructor Injection instead of @autowired
     * when using a constructor to set injected properties, you do not have to provide the autowire annotation
     * @param employeeService
     */
    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employees" )
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeDTO employee) {
        HttpStatus status = HttpStatus.CREATED;
        Employee saved = employeeService.save( employee.toEmployeeEntity() );
        return new ResponseEntity<>(saved, status);
    }


    @GetMapping(path="/employees" )
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

}



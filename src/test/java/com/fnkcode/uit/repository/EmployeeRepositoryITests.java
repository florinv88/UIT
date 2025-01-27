package com.fnkcode.uit.repository;

import com.fnkcode.uit.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryITests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp(){
        //given - precondition or setup
        this.employee = Employee.builder()
                .firstName("Florin")
                .lastName("Visan")
                .email("florinvisan022@gmail.com")
                .build();
    }


//    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("Test to get all employees operation")
    @Test
    public void givenEmployees_whenFindAll_thenReturnAllEmployees() {
        //given - precondition or setup
        employeeRepository.save(this.employee);
        Employee employee2 = Employee.builder()
                .firstName("Andrei")
                .lastName("Ionut")
                .email("florinvisan022@gmail.com")
                .build();

        employeeRepository.save(employee2);

        // when - action or behaviour
        List<Employee> employeeList = employeeRepository.findAll();

        //then verify the output
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("Test to get employee by their id")
    @Test
    public void givenEmployee_whenFindById_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(this.employee);

        // when - action or behaviour
        Optional<Employee> employee = employeeRepository.findById(this.employee.getId());

        //then verify the output
        assertThat(employee).isPresent();
    }

    @DisplayName("Test to get employee by their email")
    @Test
    public void givenEmployee_whenFindByEmail_thenReturnEmployeeObject() {
        //given
        employeeRepository.save(this.employee);
        // when - action or behaviour
        Optional<Employee> employee = employeeRepository.findByEmail(this.employee.getEmail());
        //then verify the output
        assertThat(employee).isPresent();
    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given
        employeeRepository.save(this.employee);
        // when - action or behaviour
        Employee savedEmployee = employeeRepository.findById(this.employee.getId()).get();
        savedEmployee.setEmail("florin022@gmail.com");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("florin022@gmail.com");
    }

    @DisplayName("Test for deleting operation")
    @Test
    public void givenEmployee_whenDelete_thenRemoveEmployee() {
        //given - precondition or setup
        employeeRepository.save(this.employee);
        // when - action or behaviour
        employeeRepository.delete(this.employee);
        Optional<Employee> emp = employeeRepository.findByEmail("florinvisan022@gmail.com");

        //then verify the output
        assertThat(emp).isNotPresent();
    }








}

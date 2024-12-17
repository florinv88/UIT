package com.fnkcode.uit.service;

import com.fnkcode.uit.entity.Employee;
import com.fnkcode.uit.exception.ResourceNotFoundException;
import com.fnkcode.uit.repository.EmployeeRepository;
import com.fnkcode.uit.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        // the old way before using @Mockito , @InjectMocks
        // employeeRepository = Mockito.mock(EmployeeRepository.class);
        // employeeService = new EmployeeServiceImpl(employeeRepository);

        this.employee = Employee.builder()
                .id(1L)
                .firstName("Florin")
                .lastName("Visan")
                .email("florinvisan022@gmail.com")
                .build();
    }

    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployee_whenSaveEmployee_thenReturnEmployeeObject() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee))
                .willReturn(employee);

        // when - action or behaviour
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));


        // when - action or behaviour
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,()->{
            employeeService.saveEmployee(employee);
        });

        //then
        verify(employeeRepository,never()).save(any(Employee.class));

    }

    @DisplayName("JUni test for get all employees")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeesList() {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("tony@gmail.com")
                .build();
        given(employeeRepository.findAll())
                .willReturn(List.of(employee,employee1));

        // when - action or behaviour
        List<Employee> allEmployees = employeeService.getAllEmployees();

        //then verify the output
        Assertions.assertThat(allEmployees).isNotEmpty();
        Assertions.assertThat(allEmployees.size()).isGreaterThan(1);
    }

    @DisplayName("JUni test for get all employees empty list")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        //given - precondition or setup
        given(employeeRepository.findAll())
                .willReturn(new ArrayList<>());

        // when - action or behaviour
        List<Employee> allEmployees = employeeService.getAllEmployees();

        //then verify the output
        Assertions.assertThat(allEmployees).isEmpty();
    }

    @DisplayName("JUNIT test for findById")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        //given - precondition or setup
        given(employeeRepository.findById(1L))
                .willReturn(Optional.of(employee));

        // when - action or behaviour
        Optional<Employee> employeeOptional = employeeService.getEmployeeById(1L);

        //then verify the output
        Assertions.assertThat(employeeOptional).isPresent();
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        //given - precondition or setup
        long empId=1L;
        willDoNothing().given(employeeRepository).deleteById(empId);

        // when - action or behaviour
        employeeService.deleteById(empId);

        //then verify the output
        verify(employeeRepository,times(1)).deleteById(empId);
    }





}

package com.fnkcode.uit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnkcode.uit.entity.Employee;
import com.fnkcode.uit.service.EmployeeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.*;
import  org.mockito.ArgumentMatchers;

@WebMvcTest
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setUp(){
        this.employee = Employee.builder()
                .id(1L)
                .firstName("Florin")
                .lastName("Visan")
                .email("florinvisan022@gmail.com")
                .build();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup
        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer(invocation->invocation.getArgument(0));

        // when - action or behaviour
        ResultActions result = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee))
        );

        //then verify the output
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())));
    }


    @Test
    @DisplayName("JUNIT test for get all employees endpoint")
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        //given - precondition or setup
        given(employeeService.getAllEmployees())
                .willReturn(List.of(employee));

        // when - action or behaviour
        ResultActions result = mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON));

        //then verify the output
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(Matchers.greaterThan(0))));
    }

    @Test
    @DisplayName("JUNIT test for get employee by id , positive scenario")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnOK() throws Exception {
        //given - precondition or setup
        given(employeeService.getEmployeeById(ArgumentMatchers.any(Long.class)))
                .willReturn(Optional.of(employee));

        // when - action or behaviour
        ResultActions result = mockMvc.perform(get("/api/employees/{id}",employee.getId())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(employee.getId().intValue())))
                .andExpect(jsonPath("$.firstName",is(employee.getFirstName())))
        ;
    }

    @Test
    @DisplayName("JUNIT test for get employee by id , negative scenario")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        //given - precondition or setup
        given(employeeService.getEmployeeById(ArgumentMatchers.any(Long.class)))
                .willReturn(Optional.empty());

        // when - action or behaviour
        ResultActions result = mockMvc.perform(get("/api/employees/{id}",employee.getId())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenEmployeeId_whenDeleteById_thenRetunOk() throws Exception {
        //given - precondition or setup
        willDoNothing().given(employeeService).deleteById(ArgumentMatchers.any(Long.class));

        // when - action or behaviour
        ResultActions result = mockMvc.perform(delete("/api/employees/{id}", employee.getId()));

        //then verify the output
        result.andDo(print())
                .andExpect(status().isAccepted());
    }





}

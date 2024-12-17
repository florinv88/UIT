package com.fnkcode.uit.service.impl;

import com.fnkcode.uit.entity.Employee;
import com.fnkcode.uit.exception.ResourceNotFoundException;
import com.fnkcode.uit.repository.EmployeeRepository;
import com.fnkcode.uit.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;


    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> employeeDb = employeeRepository.findByEmail(employee.getEmail());
        if (employeeDb.isPresent()){
            throw new ResourceNotFoundException("User already exists!");
        }

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}

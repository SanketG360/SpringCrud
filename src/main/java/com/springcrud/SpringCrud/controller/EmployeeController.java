package com.springcrud.SpringCrud.controller;


import com.springcrud.SpringCrud.dto.EmployeeDTO;
import com.springcrud.SpringCrud.exceptions.ResourceNotFoundException;
import com.springcrud.SpringCrud.services.EmployeeService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{empid}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long empid) {
        Optional<EmployeeDTO> employeeDTO = employeeService.getEmployeeById(empid);
        //return employeeDTO.map(employeeDTO1 -> ResponseEntity.ok(employeeDTO1)).orElse(ResponseEntity.notFound().build());
        //return employeeDTO.map(employeeDTO1 -> ResponseEntity.ok(employeeDTO1)).orElseThrow(()-> new NoSuchElementException("Employee was not found"));
        return employeeDTO.map(employeeDTO1 -> ResponseEntity.ok(employeeDTO1)).orElseThrow(()-> new ResourceNotFoundException("Employee was not found "+empid));

    }

    // IT IS ONLY WORKS BUT CURRENT CLASS EmployeeController
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<String> handleEmployeeNotFound(NoSuchElementException exception){
//            return new ResponseEntity<>("Employee not found",HttpStatus.NOT_FOUND);
//    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createNewEmp(@RequestBody  @Valid EmployeeDTO employeeDTO) {
        EmployeeDTO employeeDTO1 =  employeeService.createNewEmp(employeeDTO);
        return  new ResponseEntity<>(employeeDTO1, HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployeeById(@RequestBody  @Valid   EmployeeDTO employeeDTO,@PathVariable Long employeeId) {
        return ResponseEntity.ok(this.employeeService.updateEmployeeById(employeeDTO,employeeId));
    }

    @DeleteMapping(path = "/{employeeId}")
    public ResponseEntity<Boolean>  deleteEmployeeById(@PathVariable Long employeeId){
        boolean gotDeleted = this.employeeService.deleteEmployeeById(employeeId);
        if(gotDeleted) return ResponseEntity.ok(true);
        return ResponseEntity.notFound().build();
    }

    @PatchMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> updatePartialEmployeeById(@RequestBody Map<String,Object> updates, @PathVariable Long employeeId) {
        EmployeeDTO  employeeDTO = this.employeeService.updatePartialEmployeeById(updates,employeeId);
        if(employeeDTO== null) return  ResponseEntity.notFound().build();
        return ResponseEntity.ok(employeeDTO);
    }


}

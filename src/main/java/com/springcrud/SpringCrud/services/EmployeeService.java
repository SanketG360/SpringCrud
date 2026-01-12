package com.springcrud.SpringCrud.services;


import com.springcrud.SpringCrud.dto.EmployeeDTO;
import com.springcrud.SpringCrud.entities.EmployeeEntity;
import com.springcrud.SpringCrud.exceptions.ResourceNotFoundException;
import com.springcrud.SpringCrud.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private  final EmployeeRepository employeeRepository;
    private  final ModelMapper modelMapper;
    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper){
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public List<EmployeeDTO> getAllEmployees(){
        List<EmployeeEntity> employeeEntities =  employeeRepository.findAll();
        return  employeeEntities.stream().map(employeeEntity -> modelMapper.map(employeeEntity,EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<EmployeeDTO> getEmployeeById(Long id){
//        Optional<EmployeeEntity>  employeeEntity = employeeRepository.findById(id);
//        return employeeEntity.map(employeeEntity1 -> modelMapper.map(employeeEntity,EmployeeDTO.class));
          return employeeRepository.findById(id).map(employeeEntity -> modelMapper.map(employeeEntity,EmployeeDTO.class));
    }


    public EmployeeDTO createNewEmp(EmployeeDTO inputEmployee){
        EmployeeEntity toSaveEntity = modelMapper.map(inputEmployee,EmployeeEntity.class);
        EmployeeEntity savedEmployeeEntity  =  employeeRepository.save(toSaveEntity);
        return modelMapper.map(savedEmployeeEntity,EmployeeDTO.class);
    }

    public EmployeeDTO updateEmployeeById(EmployeeDTO employeeDTO , Long employeeId){
        IsExistsByEmployeeId(employeeId);
        EmployeeEntity employeeEntity = modelMapper.map(employeeDTO,EmployeeEntity.class);
        employeeEntity.setId(employeeId);
        EmployeeEntity saveEmployeeEntity = this.employeeRepository.save(employeeEntity);
        return modelMapper.map(saveEmployeeEntity,EmployeeDTO.class);
    }

    public void IsExistsByEmployeeId(Long employeeId){
        boolean exists = this.employeeRepository.existsById(employeeId);
        if(!exists) throw  new ResourceNotFoundException("Employee not found with id"+employeeId);
    }

    public boolean  deleteEmployeeById(Long employeeId){
        boolean exists = this.employeeRepository.existsById(employeeId);
        if(!exists)return false;
        this.employeeRepository.deleteById(employeeId);
        return true;
    }

    public EmployeeDTO updatePartialEmployeeById(Map<String,Object> updates , Long employeeId){
        IsExistsByEmployeeId(employeeId);
        EmployeeEntity employeeEntity = this.employeeRepository.findById(employeeId).get();
        updates.forEach((field,value)->{
            Field fieldTobeUpdated  = ReflectionUtils.findRequiredField(EmployeeEntity.class,field);
            fieldTobeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldTobeUpdated,employeeEntity,value);
        });

        return modelMapper.map(employeeRepository.save(employeeEntity),EmployeeDTO.class);

    }
}
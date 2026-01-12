package com.springcrud.SpringCrud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springcrud.SpringCrud.annotations.EmployeeRoleValidation;
import jakarta.validation.constraints.*;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private Long id;
    @NotBlank(message = "Required field is Employee : name")
    @Size(min=3,max=10,message="number of character in name shound in  the range  3 to 10")
    private String name;

    @NotBlank(message = "Email of emp cannot be blank")
    @Email(message = "email should be valid")
    private String email;

    @NotNull
    @Max(value = 80,message = "age cannot be  grater than 80")
    @Max(value = 18,message = "age cannot be  less than 18")
    private Integer age;

    //@Pattern(regexp = "^(ADMIN|USER)$",message = "role of employee can be user or admin")
    @EmployeeRoleValidation
    @NotBlank(message = "Role of employee cannot be blank")
    private  String role;

    @PastOrPresent(message = "date of joining field in emp cannot be in the future")
    private  LocalDate dateofJoining;

    @NotNull(message = "salary of employee should be not null")
    @Positive(message = "salary of employee should be positive")
    @Digits(integer = 6,fraction = 2,message = "salary can be in form xxxxx.xy")
    @DecimalMax(value ="10000.99")
    @DecimalMin(value = "100.50")
    private Double salary;

    @AssertTrue(message = "employee should be active")
    @JsonProperty("isActive")
    private  Boolean isActive;
}

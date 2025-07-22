package seu.lab6.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeModel {
    @NotEmpty(message = "The ID must be Not empty")
    @Size(min = 3, message = "ID : Length must be more than 2 characters")
    private String id;

    @NotEmpty(message = "The Name must be Not empty")
    @Size(min = 5, message = "Name : Length must be more than 4 characters")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name must contain only letters")
    private String name;

    //todo check
  @Email(message = "Enter correct email")
    private String email;

    @Pattern(regexp = "^05\\d{8}$", message = "PhoneNumber must start with '05' and 10 digits")
    private String phoneNumber;

    @NotNull(message = "The age must be not null")
    @Positive(message = "The age must be number")
    @Min(value = 26 , message = "The age Must be more than 25")
    private int age;

    @NotEmpty(message = "The position must be not empty")
    @Pattern(regexp = "^(supervisor|coordinator)$", message = "The position must be a supervisor or coordinator")
    private String position;

    private boolean onLeave = false;

    @NotNull(message = "The hireDate must be not empty")
    @PastOrPresent(message = "The hireDate should be a date in the present or the past")
    private LocalDate hireDate;

    @NotNull(message = "annualLeave must be not null")
    @Positive(message = "annualLeave must be a number ")
    private int annualLeave;
}

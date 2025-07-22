package seu.lab6.Controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import seu.lab6.Model.EmployeeModel;
import java.util.ArrayList;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    ArrayList<EmployeeModel> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.ok(employees);
    }


    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@Valid @RequestBody EmployeeModel employee, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
        }
        employees.add(employee);
        return ResponseEntity.status(200).body("Employee added successfully");
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") String id, @Valid@RequestBody EmployeeModel employee, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
        }
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(id)) {
                employees.set(i, employee);
                return ResponseEntity.status(200).body("Employee updated successfully");
            }
        }
        return ResponseEntity.status(400).body("Employee not found");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") String id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(id)) {
                employees.remove(i);
                return ResponseEntity.status(200).body("Employee deleted successfully");
            }
        }
        return ResponseEntity.status(400).body("Employee not found");
    }

    @GetMapping("/search-by-position/{position}")
    public ResponseEntity<?> getEmployeeByPosition(@PathVariable("position") String position) {
        if (!position.equalsIgnoreCase("supervisor") && !position.equalsIgnoreCase("coordinator")) {
            return ResponseEntity.status(400).body("position not correct , search for 'supervisor' or 'coordinator'");
        }
        ArrayList<EmployeeModel> employeesByPosition = new ArrayList<>();
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getPosition().equals(position)) {
                employeesByPosition.add(employees.get(i));
            }
        }
        if (!employeesByPosition.isEmpty()) {
            return ResponseEntity.status(200).body(employeesByPosition);
        }

        return ResponseEntity.status(400).body("No such employee");
    }


    @GetMapping("/search-employees-by-age-range/{min}/{max}")
    public ResponseEntity<?> searchEmployeeByAgeRange(@PathVariable("min") int min, @PathVariable("max") int max) {
        ArrayList<EmployeeModel> employeesByAgeRange = new ArrayList<>();
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getAge() >= min && employees.get(i).getAge() <= max) {
                employeesByAgeRange.add(employees.get(i));
            }
        }
        if (!employeesByAgeRange.isEmpty()) {
            return ResponseEntity.status(200).body(employeesByAgeRange);
        }
        return ResponseEntity.status(400).body("No employees with this age range");
    }

    @PutMapping("/apply-for-annual-leave/{id}/{leavedayes}")
    public ResponseEntity<?> applayForAnnualLeave(@PathVariable String id , @PathVariable int leavedayes) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(id)) {
                if (!employees.get(i).isOnLeave() && employees.get(i).getAnnualLeave() > 0 && employees.get(i).getAnnualLeave() >= leavedayes) {
                    employees.get(i).setOnLeave(true);
                    employees.get(i).setAnnualLeave(employees.get(i).getAnnualLeave() - leavedayes);
                    return ResponseEntity.status(200).body(employees.get(i).getName()+" : applied for annual leave : " + leavedayes + " days , and annual leave now is : " + employees.get(i).getAnnualLeave());
                }
                else {
                    return ResponseEntity.status(400).body("Employee with this id is on leave or don't have an annual leave ");
                }

            }
        }
        return ResponseEntity.status(400).body("The Employee not found");
    }


    @GetMapping("/get-Employees-with-no-annual-leave")
    public ResponseEntity<?> getAllEmployeesWithNoAnnualLeave() {
        ArrayList<EmployeeModel> employeesWithNoAnnualLeave = new ArrayList<>();
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getAnnualLeave() == 0) {
                employeesWithNoAnnualLeave.add(employees.get(i));
            }
        }
        if (!employeesWithNoAnnualLeave.isEmpty()) {
            return ResponseEntity.status(200).body(employeesWithNoAnnualLeave);
        }
        return ResponseEntity.status(400).body("No employee with no annual leave");
    }



    @PutMapping("/promote-employee/{supid}/{empid}")
    public ResponseEntity<?> promoteEmployee(@PathVariable String supid, @PathVariable String empid) {
        boolean isSupervisor = false;
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(supid) && employees.get(i).getPosition().equalsIgnoreCase("Supervisor")) {
                isSupervisor = true;
                break;
            }
        }
        if (!isSupervisor) {
            return ResponseEntity.status(400).body("Employee with this id is not a supervisor");
        }
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(empid)) {
                if (employees.get(i).getPosition().equalsIgnoreCase("coordinator") && !employees.get(i).isOnLeave() && employees.get(i).getAge() >= 30) {
                    employees.get(i).setPosition("Supervisor");
                    return ResponseEntity.status(200).body("Employee promoted successfully");
                }
                else {
                    return ResponseEntity.status(400).body("Employee not meet requirements to promote ,(already promoted, on leave, or age < 30)");
                }
            }
        }
        return ResponseEntity.status(400).body("Employee id to promote does not exist");
    }


}

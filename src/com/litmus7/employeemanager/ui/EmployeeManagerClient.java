package com.litmus7.employeemanager.ui;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.litmus7.employeemanager.constants.ApplicationStatusCodes;
import com.litmus7.employeemanager.controller.EmployeeManagerController;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dto.Response;

public class EmployeeManagerClient {
	public static void main(String[] args) {
	    EmployeeManagerController controller = new EmployeeManagerController();

	    try (Scanner scanner = new Scanner(System.in)) {
	        Response<Integer> response = controller.writeDataToDB("data/employees.csv");
	        if (response.getStatusCode() == 200) {
	            System.out.println("All records were inserted.");
	        } else if (response.getStatusCode() == 207) {
	            System.out.println("Message: " + response.getErrorMessage());
	            System.out.println("Inserted Count: " + response.getData());
	        } else {
	            System.out.println("Message: " + response.getErrorMessage());
	        }

	        //get all employees
	        Response<List<Employee>> employeeListResponse = controller.getAllEmployees();
	        if (employeeListResponse.getStatusCode() != 200) {
	            System.out.println("Message: " + employeeListResponse.getErrorMessage());
	        } else {
	            List<Employee> employees = employeeListResponse.getData();
	            for (Employee employee : employees) {
	                System.out.println(employee);
	            }
	        }

	        //get employee by id
	        System.out.print("Enter Employee ID to fetch: ");
	        int employeeId = scanner.nextInt();
	        scanner.nextLine(); // consume newline
	        Response<Employee> employeeResponse = controller.getEmployeeById(employeeId);

	        if (employeeResponse.getStatusCode() == 200) {
	            System.out.println("Employee details: " + employeeResponse.getData());
	        } else {
	            System.out.println("Error: " + employeeResponse.getErrorMessage());
	        }

	        // Update employee
	        System.out.print("Do you want to update any employee details? (yes/no): ");
	        String choice = scanner.nextLine();

	        if (choice.equalsIgnoreCase("yes")) {
	            System.out.print("Enter Employee ID to update: ");
	            int updateId = scanner.nextInt();
	            scanner.nextLine(); // consume newline

	            Response<Employee> existingEmpResponse = controller.getEmployeeById(updateId);

	            if (existingEmpResponse.getStatusCode() == 200) {
	                Employee empToUpdate = existingEmpResponse.getData();

	                System.out.print("Enter new First Name (" + empToUpdate.getFirstName() + "): ");
	                String firstName = scanner.nextLine();
	                if (!firstName.isBlank()) empToUpdate.setFirstName(firstName);

	                System.out.print("Enter new Last Name (" + empToUpdate.getLastName() + "): ");
	                String lastName = scanner.nextLine();
	                if (!lastName.isBlank()) empToUpdate.setLastName(lastName);

	                System.out.print("Enter new Email (" + empToUpdate.getEmail() + "): ");
	                String email = scanner.nextLine();
	                if (!email.isBlank()) empToUpdate.setEmail(email);

	                System.out.print("Enter new Phone (" + empToUpdate.getPhone() + "): ");
	                String phone = scanner.nextLine();
	                if (!phone.isBlank()) empToUpdate.setPhone(phone);

	                System.out.print("Enter new Department (" + empToUpdate.getDepartment() + "): ");
	                String dept = scanner.nextLine();
	                if (!dept.isBlank()) empToUpdate.setDepartment(dept);

	                System.out.print("Enter new Salary (" + empToUpdate.getSalary() + "): ");
	                String salaryStr = scanner.nextLine();
	                if (!salaryStr.isBlank()) empToUpdate.setSalary(Double.parseDouble(salaryStr));

	                System.out.print("Enter new Join Date (" + empToUpdate.getJoinDate() + ") [yyyy-mm-dd]: ");
	                String joinDate = scanner.nextLine();
	                if (!joinDate.isBlank()) empToUpdate.setJoinDate(java.time.LocalDate.parse(joinDate));

	                Response<Boolean> updateResponse = controller.updateEmployee(empToUpdate);
	                if (updateResponse.getStatusCode() == 200 && updateResponse.getData()) {
	                    System.out.println("Employee updated successfully!");
	                } else {
	                    System.out.println("Update failed: " + updateResponse.getErrorMessage());
	                }

	            } else {
	                System.out.println("Employee not found: " + existingEmpResponse.getErrorMessage());
	            }
	        }
	        
	        //delete employee by id
	        System.out.print("Enter Employee ID to delete: ");
	        int employeeId1 = scanner.nextInt();
	        scanner.nextLine(); 

	        Response<Boolean> employeeDeleteResponse = controller.deleteEmployeeById(employeeId1);

	        if (employeeDeleteResponse.getStatusCode() == 200) {
	            System.out.println("Success: " + employeeDeleteResponse.getErrorMessage());
	        } else {
	            System.out.println("Error: " + employeeDeleteResponse.getErrorMessage());
	        }
	        
            // Add new employee
            System.out.print("Do you want to add a new employee? (yes/no): ");
            String addChoice = scanner.nextLine();
            if (addChoice.equalsIgnoreCase("yes")) {
                try {
                    System.out.print("Enter Employee ID: ");
                    int id = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter First Name: ");
                    String firstName = scanner.nextLine();

                    System.out.print("Enter Last Name: ");
                    String lastName = scanner.nextLine();

                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();

                    System.out.print("Enter Phone: ");
                    String phone = scanner.nextLine();

                    System.out.print("Enter Department: ");
                    String department = scanner.nextLine();

                    System.out.print("Enter Salary: ");
                    double salary = Double.parseDouble(scanner.nextLine());

                    System.out.print("Enter Join Date (yyyy-mm-dd): ");
                    LocalDate joinDate = LocalDate.parse(scanner.nextLine());

                    Employee newEmp = new Employee(id, firstName, lastName, email, phone, department, salary, joinDate);
                    Response<Boolean> addResponse = controller.addEmployee(newEmp);

                    if (addResponse.getStatusCode() == 200 && addResponse.getData()) {
                        System.out.println("Employee added successfully.");
                    } else {
                        System.out.println("Failed to add employee: " + addResponse.getErrorMessage());
                    }

                } catch (Exception e) {
                    System.out.println("Error while adding employee: " + e.getMessage());
                }
            }

            List<Employee> employeeList = Arrays.asList(
            	    new Employee(107, "Arjyou", "Rao", "arjyou.rao@example.com", "5876543211", "IT", 78000.0, LocalDate.of(2025, 6, 1)),
            	    new Employee(108, "Sara", "Iyer", "sara.iyer@example.com", "9876543212", "Finance", 71000.0, LocalDate.of(2020, 3, 15))
            	);

            	Response<Integer> batchResponse = controller.addEmployeesInBatch(employeeList);

            	
            	if (batchResponse.getStatusCode() == ApplicationStatusCodes.SUCCESS) {
            	    System.out.println("All employees added successfully.");
            	} else if (batchResponse.getStatusCode() == ApplicationStatusCodes.PARTIAL_SUCCESS) {
            	    System.out.println("Some employees were added, but not all. ("+batchResponse.getData()+" out of "+employeeList.size()+" employees were only added"+")");
            	} else {
            	    System.out.println(batchResponse.getErrorMessage());
            	}
            	List<Integer> empIdsToTransfer = Arrays.asList(102, 105); 
            	String newDept = "HR";

            	Response<Boolean> transferResponse = controller.transferEmployeesToDepartment(empIdsToTransfer, newDept);

            	if (transferResponse.getStatusCode() == ApplicationStatusCodes.SUCCESS) {
            	    System.out.println("Tranferring employees to new department is Successfully completed");
            	} else {
            	    System.out.println("Failed Tranferring employees to new department");
            	}

            	System.out.println("Message: " + transferResponse.getErrorMessage());


	    } catch (Exception e) {
	        System.out.println("Unexpected error occurred: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
}

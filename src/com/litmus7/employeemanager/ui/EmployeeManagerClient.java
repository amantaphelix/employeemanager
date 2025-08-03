package com.litmus7.employeemanager.ui;
import java.util.List;

import com.litmus7.employeemanager.controller.EmployeeManagerController;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dto.Response;

public class EmployeeManagerClient {
    public static void main(String[] args) {
        EmployeeManagerController controller = new EmployeeManagerController(); 
        Response<Integer> response = controller.writeDataToDB("data/employees.csv");
        if (response.getStatusCode() == 200) {
            System.out.println("All records were inserted.");
        } else if (response.getStatusCode() == 207) {
            System.out.println("Message: " + response.getErrorMessage());
            System.out.println("Inserted Count: " + response.getData());
        } else {
            System.out.println("Message: " + response.getErrorMessage());
        }

        Response<List<Employee>> employeeListResponse = controller.getAllEmployees();
        if(employeeListResponse.getStatusCode()!=200) {
        	System.out.println("Message: " + employeeListResponse.getErrorMessage());
        }else {
        List<Employee> employees = employeeListResponse.getData();
            for (Employee emp : employees) {
                System.out.println(emp);
            }
        }
        
    }
}

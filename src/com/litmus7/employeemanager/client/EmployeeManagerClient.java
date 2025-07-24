package com.litmus7.employeemanager.client;
import java.util.List;

import com.litmus7.employeemanager.controller.EmployeeManagerController;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dto.Response;

public class EmployeeManagerClient {
    public static void main(String[] args) {
        EmployeeManagerController controller = new EmployeeManagerController(); 
        Response<String> response = controller.importCSV("src/com/litmus7/employeemanager/data/employees.csv");
        System.out.println("Status Code: " + response.getStatusCode());

        if (response.getErrorMessage() != null) {
            System.out.println("Message: " + response.getErrorMessage());
        } else {
            System.out.println("Message: " + response.getData());
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

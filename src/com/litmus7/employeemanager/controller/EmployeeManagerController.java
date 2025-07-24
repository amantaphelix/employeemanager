package com.litmus7.employeemanager.controller;

import com.litmus7.employeemanager.dto.Response;
import com.litmus7.employeemanager.service.EmployeeService;

import java.util.List;

import com.litmus7.employeemanager.constants.ApplicationStatusCodes;
import com.litmus7.employeemanager.dto.Employee;

public class EmployeeManagerController {
	private EmployeeService service = new EmployeeService();

	public Response<String> writeDataToDB(String filePath) {
	    boolean result = service.importEmployeesToDB(filePath);

	    if (result) {
	        return new Response<>(ApplicationStatusCodes.SUCCESS, null, "All records inserted successfully");
	    } else {
	        return new Response<>(ApplicationStatusCodes.FAILURE, "Some records failed to insert.", null);
	    }
	}
	
	public Response<List<Employee>> getAllEmployees() {
        List<Employee> employeeList = service.getAllEmployees();

        if (employeeList == null) {
            return new Response<>(500, "Failed to fetch data", null);
        }

        return new Response<>(200, "Data fetched successfully", employeeList);
    }

}
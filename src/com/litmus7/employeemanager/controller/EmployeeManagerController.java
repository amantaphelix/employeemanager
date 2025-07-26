package com.litmus7.employeemanager.controller;

import com.litmus7.employeemanager.dto.Response;
import com.litmus7.employeemanager.service.EmployeeService;

import java.util.List;

import com.litmus7.employeemanager.constants.ApplicationStatusCodes;
import com.litmus7.employeemanager.dto.Employee;

public class EmployeeManagerController {
	private EmployeeService service = new EmployeeService();

	public Response<Integer> writeDataToDB(String filePath) {
		if (filePath == null || filePath.trim().isEmpty()) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, "File path is missing.", null);
	    }

	    if (!filePath.toLowerCase().endsWith(".csv")) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, "Provided file is not a CSV.", null);
	    }
	    
	    int[] result = service.importEmployeesToDB(filePath);
	    int total = result[0];
	    int insertedCount = result[1];
	    if (insertedCount == 0) {
	        return new Response<>(500, "No records were inserted.", null);
	    } else if (insertedCount < total) {
	        return new Response<>(207, "Partial insert: " + insertedCount + " of " + total + " inserted.", insertedCount);
	    } else
	        return new Response<>(200, null, insertedCount); 
	    }

	
	public Response<List<Employee>> getAllEmployees() {
        List<Employee> employeeList = service.getAllEmployees();

        if (employeeList == null) {
            return new Response<>(500, "Failed to fetch data", null);
        }

        return new Response<>(200, "Data fetched successfully", employeeList);
    }

}
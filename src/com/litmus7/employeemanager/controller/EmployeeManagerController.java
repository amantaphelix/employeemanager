package com.litmus7.employeemanager.controller;

import com.litmus7.employeemanager.dto.Response;
import com.litmus7.employeemanager.exception.EmployeeDaoException;
import com.litmus7.employeemanager.exception.EmployeeServiceException;
import com.litmus7.employeemanager.service.EmployeeService;

import java.util.List;

import com.litmus7.employeemanager.constants.ApplicationStatusCodes;
import com.litmus7.employeemanager.constants.ApplicationMessages;
import com.litmus7.employeemanager.dto.Employee;

public class EmployeeManagerController {
	private EmployeeService employeeService = new EmployeeService();

	public Response<Integer> writeDataToDB(String filePath) {
	    if (filePath == null || filePath.trim().isEmpty()) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.FILE_PATH_MISSING);
	    }

	    if (!filePath.toLowerCase().endsWith(".csv")) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.NOT_A_CSV);
	    }

	    try {
	        int[] result = employeeService.importEmployeesToDB(filePath);
	        int total = result[0];
	        int insertedCount = result[1];

	        if (insertedCount == 0) {
	            return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.NO_RECORDS_INSERTED);
	        } else if (insertedCount < total) {
	            return new Response<>(ApplicationStatusCodes.PARTIAL_SUCCESS,
	                    ApplicationMessages.PARTIAL_INSERT + insertedCount + " of " + total + " inserted.");
	        } else {
	            return new Response<>(ApplicationStatusCodes.SUCCESS, null, insertedCount);
	        }

	    } catch (EmployeeServiceException e) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, "Service error: " + e.getMessage());
	    } catch (Exception e) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, "Unexpected error: " + e.getMessage());
	    }
	}


	
	public Response<List<Employee>> getAllEmployees() {
        List<Employee> employees;
		try {
			employees = employeeService.getAllEmployees();
			return new Response<>(200, ApplicationMessages.DATA_FETCH_SUCCESS, employees);
		} catch (EmployeeServiceException e) {
			return new Response<>(500, ApplicationMessages.DATA_FETCH_FAILED);
		}

    }

}
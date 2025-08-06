package com.litmus7.employeemanager.controller;

import com.litmus7.employeemanager.dto.Response;
import com.litmus7.employeemanager.exception.EmployeeDaoException;
import com.litmus7.employeemanager.exception.EmployeeServiceException;
import com.litmus7.employeemanager.service.EmployeeService;
import com.litmus7.employeemanager.util.ValidationUtils;

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
	
	public Response<Employee> getEmployeeById(int employeeId) {
	    if (employeeId <= 0) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.INVALID_EMPLOYEE_ID);
	    }

	    try {
	        Employee employee = employeeService.getEmployeeById(employeeId);
	        if (employee == null) {
	            return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.EMPLOYEE_NOT_FOUND);
	        }
	        return new Response<>(ApplicationStatusCodes.SUCCESS, ApplicationMessages.DATA_FETCH_SUCCESS, employee);
	    } catch (EmployeeServiceException e) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.SERVICE_ERROR_PREFIX + e.getMessage());
	    }
	}
	
	public Response<Boolean> updateEmployee(Employee employee) {
	    try {
	        boolean updated = employeeService.updateEmployee(employee);
	        if (updated) {
	            return new Response<>(ApplicationStatusCodes.SUCCESS, ApplicationMessages.EMPLOYEE_UPDATE_SUCCESS, true);
	        } else {
	            return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.EMPLOYEE_NOT_FOUND);
	        }
	    } catch (EmployeeServiceException e) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.SERVICE_ERROR_PREFIX + e.getMessage());
	    }
	}

	public Response<Boolean> deleteEmployeeById(int employeeId) {
	    if (employeeId <= 0) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.INVALID_EMPLOYEE_ID);
	    }

	    Response<Employee> employeeResponse = getEmployeeById(employeeId);
	    if (employeeResponse.getStatusCode() != ApplicationStatusCodes.SUCCESS) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.EMPLOYEE_NOT_FOUND);
	    }

	    try {
	        boolean deleted = employeeService.deleteEmployeeById(employeeId);
	        if (deleted) {
	            return new Response<>(ApplicationStatusCodes.SUCCESS, ApplicationMessages.EMPLOYEE_DELETE_SUCCESS, true);
	        } else {
	            return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.EMPLOYEE_DELETE_FAILED);
	        }
	    } catch (EmployeeServiceException e) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.SERVICE_ERROR_PREFIX + e.getMessage());
	    }
	}

	public Response<Boolean> addEmployee(Employee employee) {
	    if (employee == null || employee.getEmployeeId() <= 0) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.INVALID_EMPLOYEE_OBJECT);
	    }

	    try {
	        boolean added = employeeService.addEmployee(employee);
	        if (added) {
	            return new Response<>(ApplicationStatusCodes.SUCCESS, ApplicationMessages.EMPLOYEE_ADD_SUCCESS, true);
	        } else {
	            return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.EMPLOYEE_ADD_FAILED, false);
	        }
	    } catch (EmployeeServiceException e) {
	        return new Response<>(ApplicationStatusCodes.FAILURE, ApplicationMessages.SERVICE_ERROR_PREFIX + e.getMessage(), false);
	    }
	}

	
}
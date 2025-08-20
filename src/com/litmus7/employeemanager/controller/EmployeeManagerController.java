package com.litmus7.employeemanager.controller;

import com.litmus7.employeemanager.dto.Response;
import com.litmus7.employeemanager.exception.EmployeeServiceException;
import com.litmus7.employeemanager.service.EmployeeService;
import com.litmus7.employeemanager.util.AppErrorMessages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import com.litmus7.employeemanager.constants.ApplicationStatusCodes;
import com.litmus7.employeemanager.dto.Employee;

public class EmployeeManagerController {
	private EmployeeService employeeService = new EmployeeService();
	
	private static final Logger logger = LogManager.getLogger(EmployeeManagerController.class);

	
	public Response<Integer> writeDataToDB(String filePath) {
		logger.trace("Entering writeDataToDB with filePath: {}", filePath);

	    if (filePath == null || filePath.trim().isEmpty()) {
	    	logger.warn("File path is missing");
	    	return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-400.emptyPath"), null);
	    }

	    if (!filePath.toLowerCase().endsWith(".csv")) {
	    	logger.warn("Invalid file type: {}", filePath);
	    	return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-400.notCSV"), null);
	    }

	    try {
	        int[] result = employeeService.importEmployeesToDB(filePath);
	        int total = result[0];
	        int insertedCount = result[1];
	        logger.info("Import completed. Inserted {}/{} records", insertedCount, total);

	        if (insertedCount == 0) {
	        	return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-500.noRecordsInserted"));
	        } else if (insertedCount < total) {
	        	return new Response<>(ApplicationStatusCodes.PARTIAL_SUCCESS,
	                    AppErrorMessages.getErrorMessage("EMP-CTRL-207"), insertedCount);
	     
	        } else {
	            return new Response<>(ApplicationStatusCodes.SUCCESS, null, insertedCount);
	        }

	    } catch (EmployeeServiceException e) {
	    	logger.error("Service error while importing employees", e);
	    	return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage(e.getErrorCode()));
	    } catch (Exception e) {
	    	return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-500.general"));
	    }finally {
	        logger.trace("Exiting writeDataToDB");
	    }
	}


	
	public Response<List<Employee>> getAllEmployees() {
		logger.trace("Entering getAllEmployees");
       
		try {
			List<Employee> employees = employeeService.getAllEmployees();
			logger.info("Fetched {} employees", employees.size());
	        return new Response<>(ApplicationStatusCodes.SUCCESS, null, employees);
		} catch (EmployeeServiceException e) {
			logger.error("Failed to fetch all employees", e);
			return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage(e.getErrorCode()));
		}finally {
            logger.trace("Exiting getAllEmployees");
        }

    }
	
	public Response<Employee> getEmployeeById(int employeeId) {
		logger.trace("Entering getEmployeeById(employeeId={})", employeeId);

	    if (employeeId <= 0) {
	    	logger.warn("Invalid employee ID provided: {}", employeeId);
	    	return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-400.invalidId"));
	    }

	    try {
	        Employee employee = employeeService.getEmployeeById(employeeId);
	        if (employee == null) {
	        	logger.info("Employee with ID {} not found", employeeId);
	        	return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-404.employeeNotFound"));
	        }
	        logger.info("Employee with ID {} fetched successfully", employeeId);
	        return new Response<>(ApplicationStatusCodes.SUCCESS, null, employee);
	    } catch (EmployeeServiceException e) {
	    	logger.error("Error fetching employee with ID {}", employeeId, e);
	    	return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage(e.getErrorCode()));
	    }finally {
            logger.trace("Exiting getEmployeeById");
        }
	}
	
	public Response<Boolean> updateEmployee(Employee employee) {
		logger.trace("Entering updateEmployee(employeeId={})", 
                employee != null ? employee.getEmployeeId() : "null");
		if (employee == null) {
            return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-400.nullEmployee"), false);
        }
		try {
	        boolean updated = employeeService.updateEmployee(employee);
	        if (updated) {
	        	logger.info("Employee {} updated successfully", employee.getEmployeeId());
	        	return new Response<>(ApplicationStatusCodes.SUCCESS, null, true);
            } else {
                logger.warn("Employee {} not found for update", employee.getEmployeeId());
                return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-404.employeeNotFound"), false);
            }
        } catch (EmployeeServiceException e) {
            logger.error("Error updating employee {}", 
                         employee != null ? employee.getEmployeeId() : "null", e);
            return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage(e.getErrorCode()));
        } finally {
            logger.trace("Exiting updateEmployee");
        }
    }

    public Response<Boolean> deleteEmployeeById(int employeeId) {
        logger.trace("Entering deleteEmployeeById(employeeId={})", employeeId);

        if (employeeId <= 0) {
            logger.warn("Invalid employee ID for deletion: {}", employeeId);
            return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-400.invalidId"), false);
        }

        Response<Employee> employeeResponse = getEmployeeById(employeeId);
        if (employeeResponse.getStatusCode() != ApplicationStatusCodes.SUCCESS) {
            logger.warn("Employee {} not found, deletion aborted", employeeId);
            return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-404.employeeNotFound"));
        }

        try {
            boolean deleted = employeeService.deleteEmployeeById(employeeId);
            if (deleted) {
                logger.info("Employee {} deleted successfully", employeeId);
                return new Response<>(ApplicationStatusCodes.SUCCESS, null, true);
            } else {
                logger.warn("Failed to delete employee {}", employeeId);
                return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-SVC-404.deleteFailed"), false);
            }
        } catch (EmployeeServiceException e) {
            logger.error("Error deleting employee {}", employeeId, e);
            return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage(e.getErrorCode()), false);
        } finally {
            logger.trace("Exiting deleteEmployeeById");
        }
    }

    public Response<Boolean> addEmployee(Employee employee) {
        logger.trace("Entering addEmployee(employeeId={})", 
                     employee != null ? employee.getEmployeeId() : "null");

        if (employee == null || employee.getEmployeeId() <= 0) {
            logger.warn("Invalid employee object provided for addition");
            return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-400.nullEmployee"), false);
        }

        try {
            boolean added = employeeService.addEmployee(employee);
            if (added) {
                logger.info("Employee {} added successfully", employee.getEmployeeId());
                return new Response<>(ApplicationStatusCodes.SUCCESS, null, true);
            } else {
                logger.warn("Failed to add employee {}", employee.getEmployeeId());
                return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-500.general"), false);
            }
        } catch (EmployeeServiceException e) {
            logger.error("Error adding employee {}", employee.getEmployeeId(), e);
            return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage(e.getErrorCode()), false);
        } finally {
            logger.trace("Exiting addEmployee");
        }
    }

    public Response<Integer> addEmployeesInBatch(List<Employee> employeeList) {
        logger.trace("Entering addEmployeesInBatch(batchSize={})", 
                     employeeList != null ? employeeList.size() : "null");

        if (employeeList == null || employeeList.isEmpty()) {
            logger.warn("Empty or null employee list provided for batch addition");
            return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-400.emptyBatch"), 0);
        }

        try {
            int[] result = employeeService.addEmployeesInBatch(employeeList);
            int addedCount = Arrays.stream(result).sum();
            logger.info("Batch addition result: {}/{} employees added", addedCount, employeeList.size());

            if (addedCount == employeeList.size()) {
                return new Response<>(ApplicationStatusCodes.SUCCESS, null, addedCount);
            } else if (addedCount > 0) {
            	return new Response<>(ApplicationStatusCodes.PARTIAL_SUCCESS,
                        AppErrorMessages.getErrorMessage("EMP-CTRL-207"), addedCount);
            } else {
            	return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-500.noRecordsInserted"), 0);
            }
        } catch (EmployeeServiceException e) {
            logger.error("Error during batch employee addition", e);
            return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage(e.getErrorCode()), 0);
        } finally {
            logger.trace("Exiting addEmployeesInBatch");
        }
    }

    public Response<Integer> transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment) {
        logger.trace("Entering transferEmployeesToDepartment(ids={}, newDept={})", employeeIds, newDepartment);

        if (employeeIds == null || employeeIds.isEmpty() || newDepartment == null || newDepartment.isBlank()) {
            logger.warn("Invalid input for employee transfer: ids={}, newDept={}", employeeIds, newDepartment);
            return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage("EMP-CTRL-400.transferInvalid"));
        }

        try {
            int updatedCount = employeeService.transferEmployeesToDepartment(employeeIds, newDepartment);
            logger.info("{} employees transferred to {} department", updatedCount, newDepartment);
            return new Response<>(ApplicationStatusCodes.SUCCESS,null, updatedCount);
        } catch (EmployeeServiceException e) {
            logger.error("Error transferring employees to department {}", newDepartment, e);
            return new Response<>(ApplicationStatusCodes.FAILURE, AppErrorMessages.getErrorMessage(e.getErrorCode()));
        } finally {
            logger.trace("Exiting transferEmployeesToDepartment");
        }
    }
}
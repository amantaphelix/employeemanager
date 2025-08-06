package com.litmus7.employeemanager.service;

import java.io.IOException;
import java.time.LocalDate;

import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.litmus7.employeemanager.util.CSVReader;
import com.litmus7.employeemanager.util.ValidationUtils;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dto.Response;
import com.litmus7.employeemanager.constants.ApplicationStatusCodes;
import com.litmus7.employeemanager.dao.EmployeeDao;
import com.litmus7.employeemanager.exception.EmployeeDaoException;
import com.litmus7.employeemanager.exception.EmployeeServiceException;

public class EmployeeService {
    private EmployeeDao dao = new EmployeeDao();
    private static final Logger LOGGER = Logger.getLogger(EmployeeService.class.getName());

    static {
        try {
            LogManager.getLogManager().reset();


            FileHandler fileHandler = new FileHandler("logs/employee-import-errors.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);

        } catch (IOException e) {
            System.err.println("Failed to set up logger: " + e.getMessage());
        }
    }

    public int[] importEmployeesToDB(String filePath) throws EmployeeServiceException{
        List<String[]> data = null;
        int successCount = 0;

        try {
            data = CSVReader.readCSV(filePath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read CSV file: " + filePath, e);
            throw new EmployeeServiceException("Error reading CSV file: " + filePath, e);
        }

        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);

            if (row.length != 8) {
                LOGGER.warning("Row " + i + ": Invalid column count");
                continue;
            }

            try {
                int employeeId = Integer.parseInt(row[0].trim());
                String firstName = row[1].trim();
                String lastName = row[2].trim();
                String email = row[3].trim();
                String phone = row[4].trim();
                String department = row[5].trim();
                double salary = Double.parseDouble(row[6].trim());
                LocalDate joinDate = LocalDate.parse(row[7].trim());

                Employee employee = new Employee(employeeId, firstName, lastName, email, phone, department, salary, joinDate);

                if (!ValidationUtils.validateEmployee(employee)) {
                    LOGGER.warning("Row " + i + ": Validation failed for employee ID " + employeeId);
                    continue;
                }
                
                try {
                    if (dao.getEmployeeById(employeeId) != null) {
                        LOGGER.warning("Row " + i + ": Duplicate entry for Employee ID " + employeeId);
                        continue;
                    }
                } catch (EmployeeDaoException e) {
                    LOGGER.log(Level.SEVERE, "Row " + i + ": Failed to check existing employee in DB", e);
                    throw new EmployeeServiceException("Error checking for existing employee", e);
                }


                try {
                    if (dao.saveEmployee(employee)) {
                        successCount++;
                    }
                } catch (EmployeeDaoException e) {
                    throw new EmployeeServiceException("Service failed while saving employee", e);
                }

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Row " + i + ": Unexpected error", e);
            }
        }

        return new int[]{data.size() - 1, successCount};
    }

    public List<Employee> getAllEmployees() throws EmployeeServiceException {
        try {
			return dao.getAllEmployees();
		} catch (EmployeeDaoException e) {
			throw new EmployeeServiceException("Failed to fetch employee details",e);
		}
    }
    
    public Employee getEmployeeById(int employeeId) throws EmployeeServiceException {
        try {
            return dao.getEmployeeById(employeeId);
        } catch (EmployeeDaoException e) {
            throw new EmployeeServiceException("Failed to fetch employee by ID: " + employeeId, e);
        }
    }
    
    public boolean updateEmployee(Employee employee) throws EmployeeServiceException {
        try {
            if (!ValidationUtils.validateEmployee(employee)) {
                return false;
            }
            return dao.updateEmployee(employee);
        } catch (EmployeeDaoException e) {
            throw new EmployeeServiceException("Failed to update employee", e);
        }
    }
    
    public boolean deleteEmployeeById(int employeeId) throws EmployeeServiceException {
        try {
            return dao.deleteEmployeeById(employeeId);
        } catch (EmployeeDaoException e) {
            throw new EmployeeServiceException("Failed to delete employee", e);
        }
    }

    public boolean addEmployee(Employee employee) throws EmployeeServiceException {
        try {
        	if (!ValidationUtils.validateEmployee(employee)) {
        	    return false;
        	}

            return dao.addEmployee(employee);
        } catch (EmployeeDaoException e) {
            throw new EmployeeServiceException("Service error: Failed to add employee", e);
        }
    }

}

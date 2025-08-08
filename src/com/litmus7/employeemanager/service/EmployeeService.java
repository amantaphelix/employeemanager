package com.litmus7.employeemanager.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.litmus7.employeemanager.util.CSVReader;
import com.litmus7.employeemanager.util.ValidationUtils;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dao.EmployeeDao;
import com.litmus7.employeemanager.exception.EmployeeDaoException;
import com.litmus7.employeemanager.exception.EmployeeServiceException;

public class EmployeeService {
    private EmployeeDao dao = new EmployeeDao();
    private static final Logger logger = LogManager.getLogger(EmployeeService.class);

    public int[] importEmployeesToDB(String filePath) throws EmployeeServiceException {
        logger.trace("Entering importEmployeesToDB()");
        List<String[]> data = null;
        int successCount = 0;

        try {
            data = CSVReader.readCSV(filePath);
            logger.info("CSV file read successfully. Total rows: {}", data.size() - 1);
        } catch (IOException e) {
            logger.error("Failed to read CSV file: {}", filePath, e);
            throw new EmployeeServiceException("Error reading CSV file: " + filePath, e);
        }

        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);

            if (row.length != 8) {
                logger.warn("Row {} skipped: Invalid column count", i);
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
                    logger.warn("Row {} failed validation for Employee ID {}", i, employeeId);
                    continue;
                }

                try {
                    if (dao.getEmployeeById(employeeId) != null) {
                        logger.warn("Duplicate Employee ID {} at row {}", employeeId, i);
                        continue;
                    }
                } catch (EmployeeDaoException e) {
                    logger.error("Row {}: Failed to check existing employee in DB", e);
                    throw new EmployeeServiceException("Error checking for existing employee", e);
                }

                try {
                    if (dao.saveEmployee(employee)) {
                        logger.debug("Employee ID {} saved successfully", employeeId);
                        successCount++;
                    }
                } catch (EmployeeDaoException e) {
                    logger.error("Row {}: Failed to save employee in DB", e);
                    throw new EmployeeServiceException("Service failed while saving employee", e);
                }

            } catch (Exception e) {
                logger.error("Unexpected error while processing row {}", i, e);
            }
        }
        logger.trace("Exiting importEmployeesToDB()");
        return new int[]{data.size() - 1, successCount};
    }

    public List<Employee> getAllEmployees() throws EmployeeServiceException {
        logger.trace("Entering getAllEmployees()");
        try {
            List<Employee> result = dao.getAllEmployees();
            return result;
        } catch (EmployeeDaoException e) {
            logger.trace("Exiting getAllEmployees() with exception");
            throw new EmployeeServiceException("Failed to fetch employee details", e);
        }finally {
        	logger.trace("Exiting getAllEmployees()");
        }
    }

    public Employee getEmployeeById(int employeeId) throws EmployeeServiceException {
        logger.trace("Entering getEmployeeById()");
        try {
            Employee emp = dao.getEmployeeById(employeeId);
            return emp;
        } catch (EmployeeDaoException e) {
            logger.trace("Exiting getEmployeeById() with exception");
            throw new EmployeeServiceException("Failed to fetch employee by ID: " + employeeId, e);
        }finally {
        	 logger.trace("Exiting getEmployeeById()");
        }
    }

    public boolean updateEmployee(Employee employee) throws EmployeeServiceException {
        logger.trace("Entering updateEmployee()");
        try {
            if (!ValidationUtils.validateEmployee(employee)) {
                return false;
            }
            boolean updated = dao.updateEmployee(employee);
            logger.trace("Exiting updateEmployee()");
            return updated;
        } catch (EmployeeDaoException e) {
            logger.trace("Exiting updateEmployee() with exception");
            throw new EmployeeServiceException("Failed to update employee", e);
        }finally {
            logger.trace("Exiting updateEmployee()");}
    }

    public boolean deleteEmployeeById(int employeeId) throws EmployeeServiceException {
        logger.trace("Entering deleteEmployeeById()");
        try {
            boolean deleted = dao.deleteEmployeeById(employeeId);
            return deleted;
        } catch (EmployeeDaoException e) {
            logger.trace("Exiting deleteEmployeeById() with exception");
            throw new EmployeeServiceException("Failed to delete employee", e);
        }finally {
            logger.trace("Exiting deleteEmployeeById()");}
    }

    public boolean addEmployee(Employee employee) throws EmployeeServiceException {
        logger.trace("Entering addEmployee()");
        try {
            if (!ValidationUtils.validateEmployee(employee)) {
                return false;
            }
            boolean added = dao.addEmployee(employee);
            logger.trace("Exiting addEmployee()");
            return added;
        } catch (EmployeeDaoException e) {
            logger.trace("Exiting addEmployee() with exception");
            throw new EmployeeServiceException("Service error: Failed to add employee", e);
        }finally { 
        	logger.trace("Exiting addEmployee()");
        }
    }

    public int[] addEmployeesInBatch(List<Employee> employeeList) throws EmployeeServiceException {
        logger.trace("Entering addEmployeesInBatch()");
        List<Employee> validEmployees = new ArrayList<>();

        for (int i = 0; i < employeeList.size(); i++) {
            Employee employee = employeeList.get(i);

            if (!ValidationUtils.validateEmployee(employee)) {
                logger.warn("Batch Row {}: Validation failed for employee ID {}", (i + 1), employee.getEmployeeId());
                continue;
            }

            try {
                if (dao.getEmployeeById(employee.getEmployeeId()) != null || validEmployees.contains(employee)) {
                    logger.warn("Batch Row {}: Employee with ID {} already exists", (i + 1), employee.getEmployeeId());
                    continue;
                }
            } catch (EmployeeDaoException e) {
                logger.error("Batch Row {}: Failed to check existing employee in DB", (i + 1), e);
                throw new EmployeeServiceException("Error checking for existing employee ID: " + employee.getEmployeeId(), e);
            }

            validEmployees.add(employee);
        }

        try {
            int[] result = dao.addEmployeesInBatch(validEmployees);
            logger.trace("Exiting addEmployeesInBatch()");
            return result;
        } catch (EmployeeDaoException e) {
            logger.trace("Exiting addEmployeesInBatch() with exception");
            throw new EmployeeServiceException("Failed to add employees in batch" + e.getMessage(), e);
        }
    }

    public int transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment) throws EmployeeServiceException {
        logger.trace("Entering transferEmployeesToDepartment()");
        if (employeeIds == null || employeeIds.isEmpty()) {
            throw new EmployeeServiceException("Employee ID list is empty.");
        }
        if (newDepartment == null || newDepartment.isBlank()) {
            throw new EmployeeServiceException("New department name is invalid.");
        }
        try {
            int transferred = dao.transferEmployeesToDepartment(employeeIds, newDepartment);
            logger.trace("Exiting transferEmployeesToDepartment()");
            return transferred;
        } catch (EmployeeDaoException e) {
            logger.trace("Exiting transferEmployeesToDepartment() with exception");
            throw new EmployeeServiceException("Error During Department Transfers" + e.getMessage(), e);
        }
    }
}

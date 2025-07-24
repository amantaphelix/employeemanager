package com.litmus7.employeemanager.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.litmus7.employeemanager.util.CSVReader;
import com.litmus7.employeemanager.util.ValidationUtils;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dao.EmployeeDao;

public class EmployeeService {
	private EmployeeDao dao = new EmployeeDao();
	
	public boolean importEmployeesToDB(String filePath) {
	    List<String[]> data = null;
	    int successCount = 0;
	    List<String> errorMessages = new ArrayList<>();

	    try {
	        data = CSVReader.readCSV(filePath);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }

	    for (int i = 1; i < data.size(); i++) {
	        String[] row = data.get(i);

	        if (row.length != 8) {
	            errorMessages.add("Row " + i + ": Invalid column count");
	            continue;
	        }

	        try {
	            int employeeId = Integer.parseInt(row[0].trim());
	            String firstName = row[1].trim();
	            String lastName = row[2].trim();
	            String email = row[3].trim();
	            String phone = row[4].trim();
	            String department = row[5].trim();
	            String salaryStr = row[6].trim();
	            String joinDateStr = row[7].trim();

	            boolean valid = true;

	            if (!ValidationUtils.isValidName(firstName)) {
	                errorMessages.add("Row " + i + ": Invalid first name");
	                valid = false;
	            }

	            if (!ValidationUtils.isValidName(lastName)) {
	                errorMessages.add("Row " + i + ": Invalid last name");
	                valid = false;
	            }

	            if (!ValidationUtils.isValidEmail(email)) {
	                errorMessages.add("Row " + i + ": Invalid email");
	                valid = false;
	            }

	            if (!ValidationUtils.isValidPhone(phone)) {
	                errorMessages.add("Row " + i + ": Invalid phone");
	                valid = false;
	            }

	            if (!ValidationUtils.isValidDepartment(department)) {
	                errorMessages.add("Row " + i + ": Invalid department");
	                valid = false;
	            }

	            if (!ValidationUtils.isValidSalary(salaryStr)) {
	                errorMessages.add("Row " + i + ": Invalid salary");
	                valid = false;
	            }

	            if (!ValidationUtils.isValidJoinDate(joinDateStr)) {
	                errorMessages.add("Row " + i + ": Invalid join date");
	                valid = false;
	            }

	            if (!valid) continue;

	            double salary = Double.parseDouble(salaryStr);
	            LocalDate joinDate = LocalDate.parse(joinDateStr);

	            Employee employee = new Employee(employeeId, firstName, lastName, email, phone, department, salary, joinDate);
	            if (dao.employeeExists(employeeId)) {
	                errorMessages.add("Row " + i + ": Duplicate entry for Employee ID " + employeeId);
	                continue;
	            }
	            if (dao.saveEmployee(employee)) {
	                successCount++;
	            } else {
	                errorMessages.add("Row " + i + ": Failed to insert employee ID " + employeeId);
	            }

	        } catch (Exception e) {
	            errorMessages.add("Row " + i + ": Unexpected error - " + e.getMessage());
	        }
	    }

	   //printing errorMessages for debug
	    for (String err : errorMessages) {
	        System.err.println(err);
	    }

	    return successCount == data.size() - 1 && errorMessages.isEmpty();
	}
	
	public List<Employee> getAllEmployees() {
	    return dao.getAllEmployees();
	}

	
}

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
import com.litmus7.employeemanager.dao.EmployeeDao;

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

    public int[] importEmployeesToDB(String filePath) {
        List<String[]> data = null;
        int successCount = 0;

        try {
            data = CSVReader.readCSV(filePath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read CSV file: " + filePath, e);
            return new int[]{0, 0};
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

                if (dao.getEmployeeById(employeeId) != null) {
                    LOGGER.warning("Row " + i + ": Duplicate entry for Employee ID " + employeeId);
                    continue;
                }

                if (dao.saveEmployee(employee)) {
                    successCount++;
                } else {
                    LOGGER.warning("Row " + i + ": Failed to insert employee ID " + employeeId);
                }

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Row " + i + ": Unexpected error", e);
            }
        }

        return new int[]{data.size() - 1, successCount};
    }

    public List<Employee> getAllEmployees() {
        return dao.getAllEmployees();
    }
}

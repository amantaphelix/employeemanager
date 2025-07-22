package com.litmus7.employeemanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.ResultSet;

public class EmployeeManagerController {

    public List<String[]> readCSV(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty.");
        }
        if (!filePath.toLowerCase().endsWith(".csv")) {
            throw new IOException("Invalid file type. Only .csv files are allowed.");
        }

        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                data.add(row);
            }
        }
        return data;
    }

    public Response writeToDB(List<String[]> data, Connection conn) {
        String insertQuery = "INSERT INTO employee (emp_id, first_name, last_name, email, phone, department, salary, join_date) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int successCount = 0;
        List<String> errorMessages = new ArrayList<>();

        for (int i = 1; i < data.size(); i++) { 
            String[] row = data.get(i);

            if (row.length != 8) {
                errorMessages.add("Row " + i + ": Invalid column count");
                continue;
            }

            try {
                int empId = Integer.parseInt(row[0].trim());

                PreparedStatement checkStmt = conn.prepareStatement("SELECT emp_id FROM employee WHERE emp_id = ?");
                checkStmt.setInt(1, empId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                	errorMessages.add("Row " + i + ": Duplicate emp_id in file: " + empId);
                    continue;
                }

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

                try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                    stmt.setInt(1, empId);
                    stmt.setString(2, firstName);
                    stmt.setString(3, lastName);
                    stmt.setString(4, email);
                    stmt.setString(5, phone);
                    stmt.setString(6, department);
                    stmt.setDouble(7, salary);
                    stmt.setDate(8, Date.valueOf(joinDate));
                    stmt.executeUpdate();
                    successCount++;
                }

            }catch (Exception e) {
                errorMessages.add("Row " + i + ": Unexpected error - " + e.getMessage());
            }
        }

        if (successCount == 0) {
            return new Response(400, "All rows failed validation or insertion", successCount, errorMessages);
        } else if (!errorMessages.isEmpty()) {
        	int totalRows = data.size() - 1; 
            String message = String.format("%d out of %d rows inserted", successCount, totalRows);
            return new Response(206, message, successCount, errorMessages);
        } else {
            return new Response(200, "All rows inserted successfully", successCount, null);
        }
    }

    public Response writeDataToDB() {
        String filePath = "src/testemployees.csv";
        try (Connection conn = DBConnection.getConnection()) {
            List<String[]> data = readCSV(filePath);
            return writeToDB(data, conn);
        } catch (SQLException e) {
            return new Response(500, "Database connection error", 0, List.of(e.getMessage()));
        } catch (IOException e) {
            return new Response(400, "CSV reading error", 0, List.of(e.getMessage()));
        } catch (Exception e) {
            return new Response(500, "Unexpected error", 0, List.of(e.getMessage()));
        }
    }
}

package employeemanager;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class DBWriter {
    public static void writeToDB(List<String[]> data, Connection conn) {
        String insertQuery = "INSERT INTO employee (emp_id, first_name, last_name, email, phone, department, salary, join_date) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        for (int i = 1; i < data.size(); i++) { // skip header
            String[] row = data.get(i);

            try {
                if (row.length != 8) {
                    System.out.println("Invalid data length, skipping: " + String.join(",", row));
                    continue;
                }

                int empId = Integer.parseInt(row[0].trim());
                String firstName = row[1].trim();
                String lastName = row[2].trim();
                String email = row[3].trim();
                String phone = row[4].trim();
                String department = row[5].trim();
                double salary = Double.parseDouble(row[6].trim());
                LocalDate joinDate = LocalDate.parse(row[7].trim());

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
                    System.out.println("Inserted: " + empId);
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("Duplicate emp_id, skipping: " + row[0]);
            } catch (Exception e) {
                System.out.println("Failed to insert record: " + String.join(",", row));
                e.printStackTrace();
            }
        }
    }
}

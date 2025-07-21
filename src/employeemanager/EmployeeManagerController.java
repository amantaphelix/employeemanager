package employeemanager;

import java.io.IOException;
import java.util.*;
import java.sql.*;

public class EmployeeManagerController {
	public void writeDataToDB() {
		String filePath = "src/employees.csv";

		try (Connection conn = DBConnection.getConnection()) {
			System.out.println("Connected: " + !conn.isClosed());

			List<String[]> data = CSVReader.readCSV(filePath);
			DBWriter.writeToDB(data, conn);

		} catch (SQLException e) {
			System.out.println("Database connection error");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("CSV reading error");
			e.printStackTrace();
		}
	}
}

package com.litmus7.employeemanager.constants;

public class SQLConstants {
	public static final String INSERT_EMPLOYEE =
	"INSERT INTO employee (emp_id, first_name, last_name, email, phone, department, salary, join_date) " +
	"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	
	public static final String CHECK_EMPLOYEE_EXISTS = "SELECT emp_id FROM employee WHERE emp_id = ?";

	
	public static final String SELECT_ALL_EMPLOYEES="SELECT * FROM employee";
	
}
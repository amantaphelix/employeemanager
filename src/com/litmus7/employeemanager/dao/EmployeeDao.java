package com.litmus7.employeemanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.litmus7.employeemanager.constants.SQLConstants;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.util.DBConnection;
import com.litmus7.employeemanager.exception.EmployeeDaoException;

public class EmployeeDao {
	
    public boolean saveEmployee(Employee employee) throws EmployeeDaoException{
    	try (Connection conn = DBConnection.getConnection();
    	        PreparedStatement stmt = conn.prepareStatement(SQLConstants.INSERT_EMPLOYEE)) {

    	        stmt.setInt(1, employee.getEmployeeId());
    	        stmt.setString(2, employee.getFirstName());
    	        stmt.setString(3, employee.getLastName());
    	        stmt.setString(4, employee.getEmail());
    	        stmt.setString(5, employee.getPhone());
    	        stmt.setString(6, employee.getDepartment());
    	        stmt.setDouble(7, employee.getSalary());
    	        stmt.setDate(8, java.sql.Date.valueOf(employee.getJoinDate()));

    	        int rowsInserted = stmt.executeUpdate();
    	        return rowsInserted > 0;

    	    } catch (SQLException e) {
    	    	throw new EmployeeDaoException("Error inserting employee to DB", e);
    	    }
    }
    
    public List<Employee> getAllEmployees() throws EmployeeDaoException{
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLConstants.SELECT_ALL_EMPLOYEES);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getInt(SQLConstants.COLUMN_EMPLOYEE_ID));
                employee.setFirstName(rs.getString(SQLConstants.COLUMN_FIRST_NAME));
                employee.setLastName(rs.getString(SQLConstants.COLUMN_LAST_NAME));
                employee.setEmail(rs.getString(SQLConstants.COLUMN_EMAIL));
                employee.setPhone(rs.getString(SQLConstants.COLUMN_PHONE));
                employee.setDepartment(rs.getString(SQLConstants.COLUMN_DEPARTMENT));
                employee.setSalary(rs.getDouble(SQLConstants.COLUMN_SALARY));
                employee.setJoinDate(rs.getDate(SQLConstants.COLUMN_JOIN_DATE).toLocalDate());
                employees.add(employee);
            }
        } catch (SQLException e) {
        	throw new EmployeeDaoException("Error in retrieving employee details from DB", e);
        }

        return employees;
    }
    
    public Employee getEmployeeById(int employeeId) throws EmployeeDaoException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLConstants.SELECT_EMPLOYEE_BY_ID)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                 Employee employee = new Employee();
                employee.setEmployeeId(rs.getInt(SQLConstants.COLUMN_EMPLOYEE_ID));
                employee.setFirstName(rs.getString(SQLConstants.COLUMN_FIRST_NAME));
                employee.setLastName(rs.getString(SQLConstants.COLUMN_LAST_NAME));
                employee.setEmail(rs.getString(SQLConstants.COLUMN_EMAIL));
                employee.setPhone(rs.getString(SQLConstants.COLUMN_PHONE));
                employee.setDepartment(rs.getString(SQLConstants.COLUMN_DEPARTMENT));
                employee.setSalary(rs.getDouble(SQLConstants.COLUMN_SALARY));
                employee.setJoinDate(rs.getDate(SQLConstants.COLUMN_JOIN_DATE).toLocalDate());
                return employee;
            }
        } catch (SQLException e) {
        	throw new EmployeeDaoException("Error in retrieving employee details from DB", e);
        }
        return null;
    }

    public boolean updateEmployee(Employee employee) throws EmployeeDaoException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLConstants.UPDATE_EMPLOYEE)) {

            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getDepartment());
            stmt.setDouble(6, employee.getSalary());
            stmt.setDate(7, java.sql.Date.valueOf(employee.getJoinDate()));
            stmt.setInt(8, employee.getEmployeeId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new EmployeeDaoException("Error updating employee", e);
        }
    }

    public boolean deleteEmployeeById(int employeeId) throws EmployeeDaoException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLConstants.DELETE_EMPLOYEE_BY_ID)) {

            stmt.setInt(1, employeeId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
  
        } catch (SQLException e) {
            throw new EmployeeDaoException("Error deleting employee with ID " + employeeId + ": " + e.getMessage(), e);
        }

     }
    

    public boolean addEmployee(Employee employee) throws EmployeeDaoException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLConstants.INSERT_EMPLOYEE)) {

            preparedStatement.setInt(1, employee.getEmployeeId());
            preparedStatement.setString(2, employee.getFirstName());
            preparedStatement.setString(3, employee.getLastName());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setString(5, employee.getPhone());
            preparedStatement.setString(6, employee.getDepartment());
            preparedStatement.setDouble(7, employee.getSalary());
            preparedStatement.setDate(8, java.sql.Date.valueOf(employee.getJoinDate()));

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            throw new EmployeeDaoException("Error inserting employee with ID " + employee.getEmployeeId() + ": " + e.getMessage(), e);
        }
    }


}
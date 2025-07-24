package com.litmus7.employeemanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import com.litmus7.employeemanager.constants.SQLConstants;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.dto.Response;
import com.litmus7.employeemanager.util.DBConnection;

public class EmployeeDao {
	
    public boolean saveEmployee(Employee employee) throws SQLIntegrityConstraintViolationException {
    	try (Connection conn = DBConnection.getConnection();
    	        PreparedStatement stmt = conn.prepareStatement(SQLConstants.INSERT_EMPLOYEE)) {

    	        stmt.setInt(1, employee.getEmpId());
    	        stmt.setString(2, employee.getFirstName());
    	        stmt.setString(3, employee.getLastName());
    	        stmt.setString(4, employee.getEmail());
    	        stmt.setString(5, employee.getPhone());
    	        stmt.setString(6, employee.getDepartment());
    	        stmt.setDouble(7, employee.getSalary());
    	        stmt.setDate(8, java.sql.Date.valueOf(employee.getJoinDate()));

    	        int rowsInserted = stmt.executeUpdate();
    	        return rowsInserted > 0;

    	    }catch (SQLIntegrityConstraintViolationException e) {
    	        throw e;
    	    }  catch (SQLException e) {
    	        e.printStackTrace();
    	        return false;
    	    }
    }
    
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLConstants.SELECT_ALL_EMPLOYEES);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee emp = new Employee();
                emp.setEmpId(rs.getInt("emp_id"));
                emp.setFirstName(rs.getString("first_name"));
                emp.setLastName(rs.getString("last_name"));
                emp.setEmail(rs.getString("email"));
                emp.setPhone(rs.getString("phone"));
                emp.setDepartment(rs.getString("department"));
                emp.setSalary(rs.getDouble("salary"));
                emp.setJoinDate(rs.getDate("join_date").toLocalDate());

                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return employees;
    }
    
    public boolean employeeExists(int empId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLConstants.CHECK_EMPLOYEE_EXISTS)) {
            
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
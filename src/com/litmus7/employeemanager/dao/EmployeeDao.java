package com.litmus7.employeemanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.litmus7.employeemanager.constants.SQLConstants;
import com.litmus7.employeemanager.dto.Employee;
import com.litmus7.employeemanager.util.DBConnection;
import com.litmus7.employeemanager.exception.EmployeeDaoException;

public class EmployeeDao {
    private static final Logger logger = LogManager.getLogger(EmployeeDao.class);

    public boolean saveEmployee(Employee employee) throws EmployeeDaoException {
        logger.trace("Entering saveEmployee with ID: {}", employee.getEmployeeId());
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
            logger.debug("saveEmployee executed. Rows inserted: {}", rowsInserted);

            return rowsInserted > 0;
        } catch (SQLException e) {
            logger.error("SQL error inserting employee ID {}", employee.getEmployeeId(), e);
            throw new EmployeeDaoException("EMP-DAO-500.insert", e);
        } finally {
            logger.trace("Exiting saveEmployee for ID: {}", employee.getEmployeeId());
        }
    }

    public List<Employee> getAllEmployees() throws EmployeeDaoException {
        logger.trace("Entering getAllEmployees");
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
            logger.debug("getAllEmployees retrieved {} records", employees.size());
            return employees;
        } catch (SQLException e) {
            logger.error("Error retrieving employee list", e);
            throw new EmployeeDaoException("EMP-DAO-500.select", e);
        } finally {
            logger.trace("Exiting getAllEmployees");
        }
    }

    public Employee getEmployeeById(int employeeId) throws EmployeeDaoException {
        logger.trace("Entering getEmployeeById with ID: {}", employeeId);
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
                logger.debug("getEmployeeById found employee for ID {}", employeeId);
                return employee;
            }
            logger.debug("getEmployeeById found no employee for ID {}", employeeId);
            return null;
        } catch (SQLException e) {
            logger.error("Error retrieving employee with ID {}", employeeId, e);
            throw new EmployeeDaoException("EMP-DAO-500.select", e);
        } finally {
            logger.trace("Exiting getEmployeeById with ID: {}", employeeId);
        }
    }

    public boolean updateEmployee(Employee employee) throws EmployeeDaoException {
        logger.trace("Entering updateEmployee for ID: {}", employee.getEmployeeId());
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
            logger.debug("updateEmployee executed. Rows updated: {}", rowsUpdated);

            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.error("Error updating employee with ID {}", employee.getEmployeeId(), e);
            throw new EmployeeDaoException("EMP-DAO-500.update", e);
        } finally {
            logger.trace("Exiting updateEmployee for ID: {}", employee.getEmployeeId());
        }
    }

    public boolean deleteEmployeeById(int employeeId) throws EmployeeDaoException {
        logger.trace("Entering deleteEmployeeById for ID: {}", employeeId);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQLConstants.DELETE_EMPLOYEE_BY_ID)) {

            stmt.setInt(1, employeeId);
            int rowsDeleted = stmt.executeUpdate();
            logger.debug("deleteEmployeeById executed. Rows deleted: {}", rowsDeleted);

            return rowsDeleted > 0;
        } catch (SQLException e) {
            logger.error("Error deleting employee with ID {}", employeeId, e);
            throw new EmployeeDaoException("EMP-DAO-500.delete", e);
        } finally {
            logger.trace("Exiting deleteEmployeeById for ID: {}", employeeId);
        }
    }

    public boolean addEmployee(Employee employee) throws EmployeeDaoException {
        logger.trace("Entering addEmployee with ID: {}", employee.getEmployeeId());
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
            logger.debug("addEmployee executed. Rows inserted: {}", rowsInserted);

            return rowsInserted > 0;
        } catch (SQLException e) {
            throw new EmployeeDaoException("EMP-DAO-500.insert", e);
        } finally {
            logger.trace("Exiting addEmployee for ID: {}", employee.getEmployeeId());
        }
    }

    public int[] addEmployeesInBatch(List<Employee> employeeList) throws EmployeeDaoException {
        logger.trace("Entering addEmployeesInBatch. Batch size: {}", employeeList.size());
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SQLConstants.INSERT_EMPLOYEE)) {

            for (Employee employee : employeeList) {
                preparedStatement.setInt(1, employee.getEmployeeId());
                preparedStatement.setString(2, employee.getFirstName());
                preparedStatement.setString(3, employee.getLastName());
                preparedStatement.setString(4, employee.getEmail());
                preparedStatement.setString(5, employee.getPhone());
                preparedStatement.setString(6, employee.getDepartment());
                preparedStatement.setDouble(7, employee.getSalary());
                preparedStatement.setDate(8, java.sql.Date.valueOf(employee.getJoinDate()));
                preparedStatement.addBatch();
            }

            int[] result = preparedStatement.executeBatch();
            logger.debug("addEmployeesInBatch executed. Batch result length: {}", result.length);

            return result;
        } catch (SQLException e) {
            logger.error("Error inserting employees in batch", e);
            throw new EmployeeDaoException("EMP-DAO-500.batch", e);
        } finally {
            logger.trace("Exiting addEmployeesInBatch");
        }
    }

    public int transferEmployeesToDepartment(List<Integer> employeeIds, String newDepartment) throws EmployeeDaoException {
        logger.trace("Entering transferEmployeesToDepartment. New Department: {}, IDs: {}", newDepartment, employeeIds);
        int updatedCount = 0;
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(SQLConstants.UPDATE_DEPARTMENT)) {
                for (Integer employeeId : employeeIds) {
                    pstmt.setString(1, newDepartment);
                    pstmt.setInt(2, employeeId);
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected == 0) {
                        logger.warn("No employee found for ID {}", employeeId);
                        throw new EmployeeDaoException("EMP-DAO-404.employeeNotFound");
                    }
                    updatedCount++;
                }
                conn.commit();
                logger.debug("transferEmployeesToDepartment committed. Employees updated: {}", updatedCount);
            } catch (EmployeeDaoException e) {
                conn.rollback();
                logger.error("Transaction failed. Rolled back.", e);
                throw e;
            } catch (SQLException e) {
                conn.rollback();
                logger.error("Transaction failed for transferEmployeesToDepartment. Rolled back.", e);
                throw new EmployeeDaoException("EMP-DAO-500.transactionfailure", e);
            } finally {
                conn.setAutoCommit(true);
            }
            return updatedCount;
        } catch (SQLException e) {
            logger.error("Database error in transferEmployeesToDepartment", e);
            throw new EmployeeDaoException("EMP-DAO-500.general",e);
        } finally {
            logger.trace("Exiting transferEmployeesToDepartment");
        }
    }
}

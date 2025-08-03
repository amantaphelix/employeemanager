package com.litmus7.employeemanager.constants;

public class SQLConstants {
    public static final String TABLE_EMPLOYEE = "employee";
    public static final String COLUMN_EMPLOYEE_ID = "emp_id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_DEPARTMENT = "department";
    public static final String COLUMN_SALARY = "salary";
    public static final String COLUMN_JOIN_DATE = "join_date";

    public static final String INSERT_EMPLOYEE ="INSERT INTO " + TABLE_EMPLOYEE + " (" + COLUMN_EMPLOYEE_ID + ", " + COLUMN_FIRST_NAME + ", " +COLUMN_LAST_NAME + ", " + COLUMN_EMAIL + ", " + COLUMN_PHONE + ", " + COLUMN_DEPARTMENT + ", " +COLUMN_SALARY + ", " + COLUMN_JOIN_DATE + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String SELECT_EMPLOYEE_BY_ID ="SELECT * FROM " + TABLE_EMPLOYEE + " WHERE " + COLUMN_EMPLOYEE_ID + " = ?";

    public static final String SELECT_ALL_EMPLOYEES ="SELECT * FROM " + TABLE_EMPLOYEE;
}

package com.litmus7.employeemanager;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]{1,50}$");
    private static final Pattern DEPARTMENT_PATTERN = Pattern.compile("^[A-Za-z ]{1,50}$");
    private static final Pattern SALARY_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
    private static final Pattern JOIN_DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidName(String name) {
        return NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidDepartment(String department) {
        return DEPARTMENT_PATTERN.matcher(department).matches();
    }

    public static boolean isValidSalary(String salary) {
        return SALARY_PATTERN.matcher(salary).matches();
    }
    public static boolean isValidJoinDate(String date) {
        return JOIN_DATE_PATTERN.matcher(date).matches();
    }
}

package com.litmus7.employeemanager;

public class EmployeeManagerApp {

    public static void main(String[] args) {
        EmployeeManagerController controller = new EmployeeManagerController(); 
        Response response = controller.writeDataToDB();

        if (response.getStatusCode() == 200) {
            System.out.println("Success: " + response.getMessage());
            System.out.println("Total Inserted Records: " + response.getEmployeeCount());
        } else {
            System.out.println("Failed to insert all data.");
            System.out.println(response.getMessage());
            if (response.getErrors() != null && !response.getErrors().isEmpty()) {
                System.out.println("\nValidation/Insertion Errors:");
                for (String error : response.getErrors()) {
                    System.out.println(" - " + error);
                }
            }
        }

    }
}

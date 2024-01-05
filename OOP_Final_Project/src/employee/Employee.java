package employee;

import java.util.Scanner;
import query.Query;
import java.sql.ResultSet;

public class Employee {
    public String location;
    public String id;
    public Object tipe_resto;
    public Employee(Query query) {
        System.out.println("Welcome to Choparang Employee System");
        System.out.println();
        displayEmployee(query);

        try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
			    System.out.println("Enter your Employee ID:");
			    Integer employeeId = scanner.nextInt();
			    scanner.nextLine();

			    if (checkData(employeeId, query)) {
			        System.out.println("Employee ID entered successfully");
			        break;
			    }
			    System.out.println("Unknown Employee ID. Please re-enter.");
			}
		}
    }

    private void displayEmployee(Query query) {
        try {
            ResultSet resultSet = query.select("employees", "true");
            while (resultSet.next()) {
                System.out.printf("Employee ID: %s\n"
                        + "Name: %s \n"
                        + "Location: %s\n\n",
                        resultSet.getString("employee_id"),
                        resultSet.getString("name"),
                        resultSet.getString("location"));
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean checkData(Integer employeeId, Query query) {
        try {
            ResultSet resultSet = query.select("employees", "employee_id = " + employeeId);

            if (resultSet.next()) {
                resultSet.getString("name");
                this.location = resultSet.getString("location");

                if (isSpecialLocation()) {
                } else {
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isSpecialLocation() {
        return this.location.equals("Bandung") || this.location.equals("Jakarta") || this.location.equals("Bali");
    }
}

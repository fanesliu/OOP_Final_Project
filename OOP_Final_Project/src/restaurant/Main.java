package restaurant;
import java.util.*;

import employee.Employee;
import menu.Menu;
import query.Query;
import transaksi.NewTransaction;
import transaksi.UpdateTransaction;

import java.sql.*;


public class Main {
	Scanner scan = new Scanner(System.in);
	Menu menu = new Menu();
	NewTransaction new_transaction = new NewTransaction();
	UpdateTransaction update_transaction = new UpdateTransaction();
	
	public static void main(String[] args) {
		Query query = new Query();
		new Main(query);
	}
	
	public Main(Query query) {
        Employee employee = new Employee(query);

        while(true) {
        	System.out.println();
            System.out.println("-----RESTORAN PENDIRI BANGSA-----");
            System.out.println("1. Add Menu");
            System.out.println("2. Update Menu");
            System.out.println("3. Delete Menu");
            System.out.println("4. Add Transaction (new customer)");
            System.out.println("5. Update Transaction");
            System.out.println("6. Exit");
        	System.out.println("enter number from [1-6]");
            Integer inp = scan.nextInt(); scan.nextLine();
            switch(inp) {
                case 1:
                    menu.add_menu(query, employee);
                    break;
                case 2:
                    menu.update_menu(query, employee);
                    break;
                case 3:
                    menu.delete_menu(query, employee);
                    break;
                case 4:
                    new_transaction.make_reservation(query, employee);
                    break;
                case 5:
                    update_transaction.update(query, employee);
                    break;
                case 6:
                    System.out.println("~Matur nuwun~");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Pilihan tidak valid");
                    break;
            }
            
        }
        
    }
	
}
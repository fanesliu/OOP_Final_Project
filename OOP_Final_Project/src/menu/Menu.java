package menu;

import java.util.*;

import employee.Employee;
import query.Query;

import java.sql.*;


public class Menu {
	public int id;
	public String name;
	int price;
	public String location;
	
	Scanner scan = new Scanner(System.in);
	public HashMap<Integer, Integer> save_id = new HashMap<Integer, Integer>();
	Integer count = 1;
	MenuSpesial menu_special = new MenuSpesial();
	MenuLokal menu_local = new MenuLokal();
	
	public void pre() {
		save_id.clear();
		count = 1;
	}
	
	
	public void display_all_menu(Query query, Employee employee) {
		try {
			// print all menus available at employee's location
			ResultSet rs = query.select("menus", "location = 'All locations'");
			if(!rs.equals(null))System.out.printf("General menu :\n");
			while(rs.next()) {
				System.out.printf("No: %s \n"
						+ "Nama: %s \n"
	            		+ "Harga : %s\n\n",
	            				  count,
	            				  rs.getString("nama_makanan"),
	                              rs.getString("price"));
				save_id.put(count, Integer.parseInt(rs.getString("menu_id")));
				count++;
			}
			// print menu special
			if(employee.tipe_resto.equals("Special")) {
				save_id = menu_special.display_menu_location(query, employee, save_id, count);
			}
			// print menu local
			else {
				save_id = menu_local.display_menu_location(query, employee, save_id, count);
			}
			
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public boolean validate(Query query, Integer menu_id, Employee employee) {
		// check if this menu is being ordered
		Integer row = query.size(query.select("orders", "menu_id = " + menu_id)); 

		if(!row.equals(0)) {
			System.out.println("Update request rejected, the requested menu is being ordered\n");
			return false;
		}
		// check if the employee has the right to modify this menu
		ResultSet rs = query.select("menus", "menu_id = " + menu_id);
		String menu_loc;
		try {
			while(rs.next()) {
				menu_loc = rs.getString("location");
				if(!menu_loc.equals("All locations") && !menu_loc.equals(employee.location)) {
					System.out.println("You don't have the authority to modify this menu\n");
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public void update_menu(Query query, Employee employee) {
		pre();
		// Print all available menu
		System.out.printf("\n\nList of menu at " + employee.location + " branch restaurant :\n\n");
		display_all_menu(query, employee);
		
		List<String> columns = Arrays.asList("nama_makanan", "price");
		Integer menu_id;
		Integer update_op;
		String new_value;
		
		// pick menu_id untuk di update
		while(true) {
			System.out.printf("Enter menu id to update from [1 - %d] : ", save_id.size());
			menu_id = scan.nextInt(); scan.nextLine();
			if(1 <= menu_id && menu_id <= save_id.size()) break;
			System.out.println("Menu id doesn't exist! Please re-input");
		}
		
		// change menu id from display to real menu id
		menu_id = save_id.get(menu_id);
		// check menu id history transaction and employee location
		if(!validate(query, menu_id, employee)) return;
		
		// pick which column to update
		while(true){
			System.out.println("-----Update-----");
	        System.out.println("1. Menu Name");
	        System.out.println("2. Menu Price");
	        update_op = scan.nextInt(); scan.nextLine();
			if(1 == update_op || 2 == update_op) break;
			System.out.println("Unknown choice, please re-input");
		}
		
		// input value updatenya
		System.out.println("\nNew value :\n");
		new_value = scan.nextLine();
		
		// update
		if(update_op == 1) query.update("menus", columns.get(update_op-1), "'" + new_value + "'", "menu_id = " + menu_id);
		else query.update("menus", columns.get(update_op-1), new_value, "menu_id = " + menu_id);
	}
	
	public void delete_from_menus(Query query, Integer menu_id) {
		query.delete("menus", "menu_id", menu_id);
	}
	
	public void delete_menu(Query query, Employee employee) {
		pre();
		// Print all available menu
		System.out.printf("\n\nList of menu at " + employee.location + " branch restaurant :\n\n");
		display_all_menu(query, employee);
		
		Integer menu_id;
		// pick menu id
		while(true) {
			System.out.printf("Enter menu id to delete from [1 - %d]\n", save_id.size());
			menu_id = scan.nextInt(); scan.nextLine();
			if(1 <= menu_id && menu_id <= save_id.size())break;
			System.out.println("Menu id doesn't exist! Please re-input");
		}
		
		// convert menu_id from display to actual menu_id from table
		menu_id = save_id.get(menu_id);
		
		// check menu id history transaction and employee location
		if(!validate(query, menu_id, employee)) return;
		
		// delete from menu_special table (if the menu is special -> checked in menu_special class)
		menu_special.delete_from_thisMenu(query, menu_id);
		
		// delete from menu_local table (if the menu is special -> checked in menu_local class)
		menu_local.delete_from_thisMenu(query, menu_id);
		
		// delete from menus table
		delete_from_menus(query, menu_id);
		
	}
	
	public void add_to_menus(Query query, List<String> new_menu, Employee employee) {
		String columns = "(`nama_makanan`, `price`, `location`)";
		String values = "('" + new_menu.get(0) + "', " + new_menu.get(1) + ", '" + employee.location + "')";
		query.insert("menus", columns, values);
	}
	
	public void add_menu(Query query, Employee employee) {
		pre();
		List<String> question = Arrays.asList("nama_makanan", "price", "narasi", "origin");
		List<String> new_menu = Arrays.asList("", "", "", "");
		String menu_type;
		
		// Ask user
		System.out.println("-----Add Menu-----");
		while(true) {
			System.out.printf("New menu type (Regular, Special, or Local) : ");
			menu_type = scan.nextLine();
			if(menu_type.equals("Regular") || menu_type.equals(employee.tipe_resto)) {
				break;
			}
			System.out.println("You don't have the authority to make that kind of menu. Please re-input\n");
		}
		
		// Get input data
		for(int i=0; i<4; i++) {
			if(i == 2 && menu_type.equals("Regular")) break;
			if(i == 3 && menu_type.equals("Special")) break;
			
			System.out.printf("[%s] : \n", question.get(i));
			String inp = scan.nextLine();
			new_menu.set(i, inp);
		}
		
		// Insert into menus table
		add_to_menus(query, new_menu, employee);
		
		// If menu is special, insert into menu_special table
		if(menu_type.equals("Special")) {
			menu_special.add_to_thisMenu(query, new_menu);
		}
		
		// If menu is local, insert into local_special table
		if(menu_type.equals("Local")) {
			menu_local.add_to_thisMenu(query, new_menu);
		}
		
	}
	
}
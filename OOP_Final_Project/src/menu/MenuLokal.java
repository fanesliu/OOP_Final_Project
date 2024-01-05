package menu;
import java.util.*;

import employee.Employee;
import query.Query;

import java.sql.*;

public class MenuLokal implements TypeMenu{
	public HashMap<Integer, Integer> display_menu_location(Query query, Employee employee, HashMap<Integer, Integer> save_id, Integer count) {
		try {
			ResultSet rs = query.select("menus", "location = '" +  employee.location + "'");
			if(!rs.equals(null))System.out.printf("List " + employee.tipe_resto + " menu : \n");
			
			while(rs.next()) {
				// Print like regular menu
				System.out.printf("No: %s \n"
						+ "Nama: %s \n"
						+ "Harga : %s\n",
							  count,
            				  rs.getString("nama_makanan"),
            				  rs.getString("price"));
				
				// Join to menu_local table
				ResultSet menuRs = query.select("menu_local", "menu_id = " + rs.getInt("menu_id"));
				while(menuRs.next()) {
					System.out.printf("Narasi : %s\nOrigin : %s\n", menuRs.getString("narasi"), menuRs.getString("origin"));
				}
				System.out.println("");
				
				// Save each menu number COUNT to its corresponding menu ID
				save_id.put(count, rs.getInt("menu_id"));
				count++;
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return save_id;
	}
	
	public void delete_from_thisMenu(Query query, Integer menu_id) {
		ResultSet rs = query.select("menu_local", "menu_id = " + menu_id);
		if(rs.equals(null)) return;
		query.delete("menu_local", "menu_id", menu_id);
	}
	
	public void add_to_thisMenu(Query query, List<String> new_menu) {
		ResultSet rs = query.select("menus", "nama_makanan = '" + new_menu.get(0) + "'");
		Integer menu_id = null;
		try {
			while(rs.next()) {
				menu_id = rs.getInt("menu_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		String columns = "(`menu_id`, `narasi`, `origin`)";
		String values = "(" + menu_id + ", '" + new_menu.get(2) + "', '" + new_menu.get(3) + "')";
		query.insert("menu_local", columns, values);
	}
}
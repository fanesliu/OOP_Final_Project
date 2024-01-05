package menu;

import java.util.HashMap;
import java.util.List;

import employee.Employee;
import query.Query;

interface TypeMenu {
	public HashMap<Integer, Integer> display_menu_location(Query query, Employee employee, HashMap<Integer, Integer> save_id, Integer Count);
	public void delete_from_thisMenu(Query query, Integer menu_id);
	public void add_to_thisMenu(Query query, List<String> new_menu);
	
}
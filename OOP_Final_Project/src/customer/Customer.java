package customer;
import query.Query;

import java.sql.*;

public class Customer {
	public String name;
	public Integer id;
	
	public String fetchCustomerName(Query query, Integer customerId) {
    ResultSet resultSet = query.select("customers", "customer_id = " + customerId);
    try {
        while (resultSet.next()) {
            return resultSet.getString("name");
        }
    } catch (SQLException e) {
        // Handle the exception appropriately
        e.printStackTrace();
    }
    return "";
}

	public String getName(Query query, int int1) {
		return null;
	}

}
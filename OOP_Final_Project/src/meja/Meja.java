package meja;

import java.sql.ResultSet;
import java.sql.SQLException;

import query.Query;

public class Meja {
	public String typeMeja;
	public Integer jumlahTamu;
	public Meja(String typeMeja, Integer jumlahTamu) {
		super();
		this.typeMeja = typeMeja;
		this.jumlahTamu = jumlahTamu;
	}
	public boolean check_table(Query query) {
		ResultSet rs = query.select("tables", "table_type = '" + this.typeMeja +"'");
		boolean error = false;
		if(query.size(rs) == 0) {
			System.out.println("Unknown table type");
			error = true;
		}
		else {
			try {
				rs = query.select("tables", "table_type = '" + this.typeMeja +"'");
				while(rs.next()) {
					if(this.jumlahTamu > rs.getInt("maximum_people")) {
						System.out.println("The number of people exceeds table capacity (max. " + rs.getInt("maximum_people") + ")");
						error = true;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				error = true;
				e.printStackTrace();
			}
		}
		return error;
	}
	
	public void show_table(Query query, Integer transaction_id) {
		ResultSet rs = query.select("tables", "transaction_id = " + transaction_id);
		try {
			while(rs.next()) {
				System.out.println("Table type: " + this.typeMeja);
				System.out.println("Number of people: " + this.jumlahTamu);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

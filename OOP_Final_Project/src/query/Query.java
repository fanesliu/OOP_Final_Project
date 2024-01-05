package query;

import java.sql.*;

public class Query {
    private Connection conn;

    public Query() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant", "root", "");

            if (conn != null) {
                System.out.println("Connection established!");
            } else {
                System.out.println("Failed to connect");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet select(String table, String condition) {
        ResultSet rs = null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + table + " WHERE " + condition);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void update(String table, String column_value, String new_value, String condition) {
        try {
            String query = "UPDATE " + table + " SET " + column_value + " = ? WHERE " + condition;
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, new_value);

                int check = pst.executeUpdate();

                if (check != 0) {
                    System.out.println("Data successfully updated!");
                } else {
                    System.out.println("Update failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String table, String column_id, Integer id) {
        try {
            String query = "DELETE FROM " + table + " WHERE " + column_id + " = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, id);

                int check = pst.executeUpdate();

                if (check != 0) {
                    System.out.println("Data successfully deleted!");
                } else {
                    System.out.println("No data deleted");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String table, String column, String value) {
        try {
            String query = "INSERT INTO " + table + " " + column + " VALUES " + value;
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                int check = pst.executeUpdate();

                if (check != 0) {
                    System.out.println("Data successfully inserted!");
                } else {
                    System.out.println("Insert failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet getLastData(String table, String field) {
        ResultSet rs = null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + table + " ORDER BY " + field + " DESC LIMIT 1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public Integer size(ResultSet rs) {
        Integer ct = 0;
        try {
            while (rs.next()) {
                ct++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ct;
    }

    public ResultSet showTransaction(Integer targetId) {
        ResultSet rs = null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT nama_makanan, price, quantity, price*quantity AS `total_price` " +
                    " FROM orders AS o JOIN menus AS m ON m.menu_id = o.menu_id" +
                    " WHERE transaction_id = " + targetId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rs;
    }

    public ResultSet show_transaction(ResultSet rs, Integer finalId) {
        return null;
    }
}

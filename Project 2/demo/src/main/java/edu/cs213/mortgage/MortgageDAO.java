package edu.cs213.mortgage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MortgageDAO {
    public List<Mortgage> getFilteredMortgages(String query, List<Object> params) {
        List<Mortgage> mortgages = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Mortgage mortgage = new Mortgage(
                    rs.getInt("mortgage_id"),
                    rs.getDouble("loan_amount"),
                    rs.getDouble("interest_rate"),
                    rs.getString("purchaser_type"),
                    rs.getString("county"),
                    rs.getString("msamd")
                );
                mortgages.add(mortgage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mortgages;
    }
}

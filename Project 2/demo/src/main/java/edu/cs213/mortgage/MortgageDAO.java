package edu.cs213.mortgage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MortgageDAO {

    /**
     * Retrieves filtered mortgages from the database.
     * Uses dynamic query building and prepared statements.
     * 
     * @param filters Active filters provided by FilterManager.
     * @return List of matching Mortgage records.
     */
    public List<Mortgage> getFilteredMortgages(List<Filter> filters) {
        List<Mortgage> mortgages = new ArrayList<>();

        // Base SQL Query
        String query = """
            SELECT a.application_id, a.respondent_id, a.loan_type, a.loan_amount_000s, 
                   at.action_taken_name, a.action_taken, m.msamd_name, a.msamd, 
                   l.county_name, a.applicant_income_000s, a.rate_spread, a.purchaser_type, a.lien_status
            FROM application a
            JOIN action_taken at ON a.action_taken = at.action_taken
            JOIN location l ON a.location_id = l.location_id
            JOIN msamd m ON a.msamd = m.msamd
            WHERE a.action_taken = 1 AND 
            (a.purchaser_type = 0 OR a.purchaser_type = 1 OR a.purchaser_type = 2 OR a.purchaser_type = 3 OR a.purchaser_type = 4 OR a.purchaser_type = 8) 
        """;
        
        // Use FilterManager for Query Building
        FilterManager filterManager = new FilterManager(filters);
        String whereClause = filterManager.buildWhereClause();  
        List<Object> params = filterManager.collectParameters();

        try (Connection conn = DatabaseCon.connect();
             PreparedStatement stmt = conn.prepareStatement(query + whereClause)) {

            // Inject parameters into the PreparedStatement
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            // Execute Query and Map Results to Mortgage Objects
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Mortgage mortgage = new Mortgage(
                    rs.getInt("application_id"),
                    rs.getString("respondent_id"),
                    rs.getInt("loan_type"),
                    rs.getInt("loan_amount_000s"),
                    rs.getString("action_taken_name"),
                    rs.getInt("action_taken"),
                    rs.getString("msamd_name"),
                    rs.getInt("msamd"),
                    rs.getString("county_name"),
                    rs.getInt("applicant_income_000s"),
                    rs.getDouble("rate_spread"),
                    rs.getInt("purchaser_type"),
                    rs.getInt("lien_status") 
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mortgages;
    }
}

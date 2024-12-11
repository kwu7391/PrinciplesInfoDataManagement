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
                    a.action_taken, a.msamd, 
                    a.applicant_income_000s, a.rate_spread, a.purchaser_type, 
                   a.lien_status, a.property_type, a.loan_purpose
            FROM application a
        """;
        //            JOIN location l ON a.location_id = l.location_id
        //            JOIN action_taken at ON a.action_taken = at.action_taken
        //            JOIN msamd m ON a.msamd = m.msamd

        // Use FilterManager for Query Building
        FilterManager filterManager = new FilterManager(filters);
        String whereClause = filterManager.buildWhereClause();  
        List<Object> params = filterManager.collectParameters();

        try (Connection conn = DatabaseCon.connect();
             var stmt = conn.createStatement()) {

            // Inject parameters into the PreparedStatement
            //for (int i = 0; i < params.size(); i++) {
              //  stmt.setObject(i + 1, params.get(i));
            //}

            // Execute Query and Map Results to Mortgage Objects
            System.out.println("SELECT COUNT(*) FROM (" + query + " " + whereClause + ");");
            ResultSet rs = stmt.executeQuery(query + " " + whereClause);
            while (rs.next()) {
                Mortgage mortgage = new Mortgage(
                    rs.getInt("application_id"),
                    rs.getString("respondent_id"),
                    rs.getInt("loan_type"),
                    rs.getInt("loan_amount_000s"),
                    rs.getInt("action_taken"),
                    rs.getInt("msamd"),
                    //rs.getInt("county_code"),
                    rs.getInt("applicant_income_000s"),
                    rs.getDouble("rate_spread"),
                    rs.getInt("purchaser_type"),
                    rs.getInt("lien_status"),
                    rs.getInt("property_type"),
                    rs.getInt("loan_purpose")
                );
                mortgages.add(mortgage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mortgages;
    }
}
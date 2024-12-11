package edu.cs213.mortgage;

import java.util.List;

public class Filter {
    private final String name;                // Human-readable filter name
    private final String sqlCondition;        // SQL condition for the WHERE clause
    private final List<Object> parameters;    // Values used in the PreparedStatement
    private final int filterChoice;

    /**
     * Constructor to create a Filter instance.
     * @param name          Descriptive filter name
     * @param sqlCondition  Corresponding SQL WHERE clause condition
     * @param parameters    Filter parameters
     */
    public Filter(String name, String sqlCondition, List<Object> parameters, int filterChoice) {
        this.name = name;
        this.sqlCondition = sqlCondition;
        this.parameters = parameters;
        this.filterChoice = filterChoice;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getSqlCondition() {
        return sqlCondition;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public int getChoice() {
        return filterChoice;
    }

    public String toString()
    {
        return name;
    }
}

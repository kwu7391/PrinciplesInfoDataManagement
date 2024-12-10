package edu.cs213.mortgage;

import java.util.List;

public class Filter {
    private final String name;                // Human-readable filter name
    private final String sqlCondition;        // SQL condition for the WHERE clause
    private final List<Object> parameters;    // Values used in the PreparedStatement

    /**
     * Constructor to create a Filter instance.
     * @param name          Descriptive filter name
     * @param sqlCondition  Corresponding SQL WHERE clause condition
     * @param parameters    Filter parameters
     */
    public Filter(String name, String sqlCondition, List<Object> parameters) {
        this.name = name;
        this.sqlCondition = sqlCondition;
        this.parameters = parameters;
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

    public String toString()
    {
        return name;
    }
}

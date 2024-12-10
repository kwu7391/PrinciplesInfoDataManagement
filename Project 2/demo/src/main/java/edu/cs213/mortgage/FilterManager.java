package edu.cs213.mortgage;

import java.util.ArrayList;
import java.util.List;

public class FilterManager {
    private final List<Filter> filters;

    public FilterManager() {
        this.filters = new ArrayList<>();
    }

    public FilterManager(List<Filter> filters) {
        this.filters = new ArrayList<>(filters);
    }

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void removeFilter(Filter filter) {
        filters.remove(filter);
    }

    public void clearFilters() {
        filters.clear();
    }

    public List<Filter> getFilters() {
        return new ArrayList<>(filters);
    }

    /**
     * Builds the SQL WHERE clause from active filters.
     * Includes default action_taken and purchaser_type conditions.
     * @return A complete WHERE clause for the SQL query.
     */
    public String buildWhereClause() {
        if (filters.isEmpty()) {
            return "WHERE a.action_taken = 1 AND a.purchaser_type IN (0, 1, 2, 3, 4, 8)";
        }

        StringBuilder whereClause = new StringBuilder(
                "WHERE a.action_taken = 1 AND a.purchaser_type IN (0, 1, 2, 3, 4, 8)"
        );

        for (Filter filter : filters) {
            whereClause.append(" AND ").append(filter.getSqlCondition());
        }

        return whereClause.toString();
    }

    /**
     * Collects all filter parameters in order.
     * Used for safely injecting parameters into PreparedStatement.
     * @return A list of filter parameter values.
     */
    public List<Object> collectParameters() {
        List<Object> parameters = new ArrayList<>();
        for (Filter filter : filters) {
            parameters.addAll(filter.getParameters());
        }
        return parameters;
    }
}

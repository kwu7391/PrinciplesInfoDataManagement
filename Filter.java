package edu.cs213.mortgage;

public class Filter {
    private final String name;
    private final String sqlCondition;

    public Filter(String name, String sqlCondition) {
        this.name = name;
        this.sqlCondition = sqlCondition;
    }

    public String getName() { return name; }
    public String getSqlCondition() { return sqlCondition; }
}

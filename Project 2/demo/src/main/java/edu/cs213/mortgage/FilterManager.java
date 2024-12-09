package edu.cs213.mortgage;

import java.util.ArrayList;
import java.util.List;

public class FilterManager {
    private final List<Filter> filters = new ArrayList<>();

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void removeFilter(Filter filter) {
        filters.remove(filter);
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void clearFilters() {
        filters.clear();
    }
}

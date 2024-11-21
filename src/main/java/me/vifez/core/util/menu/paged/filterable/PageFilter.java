package me.vifez.core.util.menu.paged.filterable;

import java.util.function.Predicate;

public class PageFilter<T> {

    private String name;
    private boolean enabled;
    private Predicate<T> predicate;

    public PageFilter(String name, Predicate<T> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    public boolean test(T t) {
        return !enabled || predicate.test(t);
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
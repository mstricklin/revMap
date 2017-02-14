// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package edu.utexas.arlut.ciads.scratch.revMap00;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import lombok.Data;

@Data
public class Foo {
    final Long id;
    final String label;
    final String s1;
    private final Map<String, Object> properties = newHashMap();

    public static Foo copy(final Foo f) {
        Foo nf = new Foo(f);
        return nf;
    }
    public static Foo make(final String a, final String b) {
        return new Foo(sID++, a, b);
    }
    // =================================
    private Foo(final Long id_, final String label_, final String s1_) {
        id = id_;
        label = label_;
        s1 = s1_;
    }
    private Foo(final Foo f) {
        id = f.id;
        label = f.label;
        s1 = f.s1;
        properties.putAll(f.properties);
    }
    // =================================
    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key) {
        return (T)properties.get(key);
    }
    // =================================
    public Set<String> getPropertyKeys() {
        return properties.keySet();
    }
    // =================================
    public void setProperty(String key, Object value) {
        properties.put(key,  Property.make(key, value));
    }
    // =================================
    @SuppressWarnings("unchecked")
    public <T> T removeProperty(String key) {
        return (T)properties.remove(key);
    }
    // =================================
    public Long getId() {
        return id;
    }
    // =================================
    @Override
    public int hashCode() {
        return id.intValue();
    }
    // =================================
    @Override
    public boolean equals(Object other) {
        if ( this == other ) return true;
        if ( !(other instanceof Foo) ) return false;
        Foo f = (Foo)other;
        return Objects.equal(id, f.id);
    }
    // =================================

//    private Map<String, Property> properties = newHashMap();
    private static long sID = 1;
}

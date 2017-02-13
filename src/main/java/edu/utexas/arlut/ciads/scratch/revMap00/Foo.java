// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package edu.utexas.arlut.ciads.scratch.revMap00;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class Foo {
    final Long id;
    final String label;
    final String s1;

    public static Foo copy(final Foo f) {
        Foo nf = new Foo(f.getId(), f.label, f.s1);
        nf.properties = newHashMap(f.properties);
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
    // =================================
    public <T> T getProperty(String key) {
        return (T)properties.get(key);
    }
    // =================================
    public Set<String> getPropertyKeys() {
        return properties.keySet();
    }
    // =================================
    public void setProperty(String key, Object value) {
        properties.put(key,  new Property(key, value));
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

//    private Map<String, Property> properties = newHashMap();
    private Map<String, Object> properties = newHashMap();
    private static long sID = 1;
}

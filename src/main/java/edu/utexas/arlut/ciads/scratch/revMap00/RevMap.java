// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package edu.utexas.arlut.ciads.scratch.revMap00;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

import java.util.*;

import com.google.common.base.Optional;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RevMap extends AbstractMap<Long, Foo> {



    public void branch() {
        // begin transaction space
    }
    // =================================
    public void merge() {
        // lock
        for (Map.Entry<Long, Foo> e: modified.entrySet()) {
            m.put(e.getKey(), e.getValue());
        }
        for (Long key: removed.keySet()) {
            m.remove(key);
        }
        removed.clear();
        modified.clear();
    }
    // =================================
    public void rollback() {
        removed.clear();
        modified.clear();
    }

    // =================================
    @Override
    public void clear() {
        // add everything in the map to the 'removed' collection, pending a commit
        removed.putAll(m);
        modified.clear();
    }
    // =================================
    @Override
    public boolean containsKey(Object key) {
        if (removed.containsKey(key))
            return false;
        return (modified.containsKey(key) || m.containsKey(key));
    }
    // =================================
    @Override
    public boolean containsValue(Object value) {
        if (removed.containsValue(value))
            return false;
        return (modified.containsValue(value) || m.containsValue(value));
    }
    // =================================
    @Override
    public Set<Map.Entry<Long, Foo>> entrySet() {
        Set<Map.Entry<Long, Foo>> es = newHashSet(modified.entrySet());
        es.addAll( m.entrySet() );
        es.removeAll( removed.entrySet() );
        return es;
    }
    // =================================
    @Override
    public Foo get(Object key) {
        Foo f = modified.get(key);
        if (null == f)
            return m.get(key);
        return f;
    }
    // =================================
    @Override
    public boolean isEmpty() {
        // TODO: check m.isEmpty, and modified.isEmpty, after subtracting removed elemenets
        return m.isEmpty();
    }
    // =================================
    @Override
    public Set<Long> keySet() {
        Set<Long> keys = newHashSet( modified.keySet() );
        keys.addAll( m.keySet() );
        return Sets.difference( keys, removed.keySet() );
    }
    // =================================
    @Override
    public Foo put(@NonNull Long key, @NonNull Foo value) {
        return modified.put(key, value);
    }
    // =================================
    @Override
    public void putAll(Map<? extends Long, ? extends Foo> m) {
        modified.putAll(m);
    }
    // =================================
    public Foo remove(Long k) {
        if (removed.containsKey(k))
            return null;
        Foo v = m.get(k);
        if (null == v)
            v = modified.remove(k);
        if (null != v)
            removed.put(k, v);
        return v;
    }
    // =================================
    @Override
    public int size() {
        // TODO: (m.size U modified.size) - removed.size
        return m.size();
    }
    // =================================
    @Override
    public Collection<Foo> values() {
        Set<Foo> values = newHashSet( modified.values() );
        values.addAll( m.values() );
        values.removeAll(removed.values());
        return values;
    }
    // =================================
    public void touch(Long key) {
        if (modified.containsKey(key)) {
            Foo f = modified.get(key);
            f.setProperty("aaa", "bbb");
        } else if (m.containsKey(key)) {
            Foo f = Foo.copy( m.get(key) );
            f.setProperty("aaa", "bbb");
            modified.put(key, f);
        } else {
            // throw
        }
    }
    // =================================
    public void dump() {
        Map<Long, Optional<Foo>> mz = branchMap.get();

        log.info("Original ==============");
        for (Map.Entry<Long, Foo> e: m.entrySet())
            log.info("{} => {}", e.getKey(), e.getValue());
        log.info("Rev      ==============");
        for (Map.Entry<Long, Foo> e: modified.entrySet())
            log.info("{} => {}", e.getKey(), e.getValue());
        log.info("Removed   ==============");
        for (Map.Entry<Long, Foo> e: removed.entrySet())
            log.info("{} => {}", e.getKey(), e.getValue());
    }
    // =================================
    @Override
    public boolean equals(Object other) {
        if ( this == other ) return true;
        if ( !(other instanceof Foo) ) return false;
        RevMap rm = (RevMap)other;
        // TODO: ignore revision context?
        return Objects.equal(m, rm.m);
    }
    // =================================
    @Override
    public int hashCode() {
        return m.hashCode();
    }
    // =================================
    private Map<Long, Foo> m        = newHashMap();
    private Map<Long, Foo> modified = newHashMap();
    private Map<Long, Foo> added    = newHashMap();
    private Map<Long, Foo> removed  = newHashMap();

    private ThreadLocal<Map<Long, Optional<Foo>>> branchMap = new ThreadLocal<Map<Long, Optional<Foo>>>() {
        @Override protected Map<Long, Optional<Foo>> initialValue() {
            return newHashMap();
        }
    };
}

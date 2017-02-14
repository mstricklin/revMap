// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package edu.utexas.arlut.ciads.scratch.revMap00;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

@Slf4j
public  class Store<K, V> implements IStore<K, V> {

    public static class Revision<K,V> implements IRevision<K, V> {
        private Revision(Store store_) {
            this.name = "rev " + store_.toString();
            this.store = store_;
        }
        private Revision(String name, Store store_) {
            this.name = name;
            this.store = store_;
        }
        // =================================
        @Override
        public V put(K key, V value) {
            // TODO: check committed
            return modified.put(key, value);
        }
        // =================================
        @Override
        public void putAll(Map<K, V> m) {  }
        // =================================
        @Override
        public V get(K key) {
            if ( committed )
                return null;
            if (removed.contains(key))
                return null;
            V v = modified.get(key);
            if (null == v)
                return store.m.get(key);
            return v;
        }
        // =================================
        @Override
        public V remove(K key) {
            if (removed.contains(key))
                return null;
            V v = modified.remove(key);
            if (null == v)
                v = store.m.get(key);
            removed.add(key);
            return v;
        }
        // =================================
        @Override
        public Iterable<K> keys() {
            if ( committed )
                return Collections.emptyList();
            Set<K> k = Sets.newHashSet( store.m.keySet() );
            k.addAll( modified.keySet() );
            k.removeAll( removed );
            return Collections.unmodifiableSet(k);
        }
        // =================================
        @Override
        public Iterable<V> values() { return Collections.emptyList(); }
        // =================================
        @Override
        public boolean containsKey(K key) {
            if (removed.contains(key)) {
                return false;
            }
            return modified.containsKey(key) || store.m.containsKey(key);
        }
        // =================================
        @Override
        public int size() { return 0; }
        // =================================
        @Override
        public int revisionCnt() { return modified.size() + removed.size(); }
        // =================================
        @Override
        public void commit() { // throws, synchronize
            if (committed)
                return;
            store.m.putAll(modified);
            for (K k: removed)
                store.m.remove(k);
            modified.clear();
            removed.clear();
            committed = true;
        }
        // =================================
        @Override
        public void rollback() { // synchronize
            // mark this revision unusable somehow...
            modified.clear();
            removed.clear();
            committed = true;
        }
        // =================================
        @Override
        public String toString() {
            return name;
        }
        // =================================
        @Override
        public boolean equals(Object o) {
            // Just check against ourselves, b/c otherwise identical revisions
            // aren't the same revision
            return (this == o);
        }
        // =================================
        @Override
        public int hashCode() {
            return Objects.hashCode(removed, modified, store);
        }
        // =================================
        private boolean committed = false;
        private final String name;
        private final Store<K,V> store;
        private final Map<K, V> modified = newHashMap();
        private final Set<K> removed  = newHashSet();
    }
    // =================================
    @Override
    public V get(K key) {
        return m.get(key);
    }
    // =================================
    @Override
    public Iterable<K> keySet() {
        return Collections.unmodifiableSet(m.keySet());
    }
    // =================================
    @Override
    public Iterable<V> values() {
        return Collections.unmodifiableCollection(m.values());
    }
    // =================================
    @Override
    public boolean containsKey(K key) {
        return m.containsKey(key);
    }
    // =================================
    @Override
    public int size() {
        return m.size();
    }
    // =================================
    @Override
    public IRevision<K, V> revision() {
        return new Revision<>(this);
    }
    @Override
    public IRevision<K, V> revision(String name) {
        return new Revision<>(name, this);
    }
    // =================================

    // =================================
    private Map<K, V> m = newHashMap();
}

// CLASSIFICATION NOTICE: This file is UNCLASSIFIED
package edu.utexas.arlut.ciads.scratch.revMap00;

import java.util.Map;


public interface IStore<K, V> {

    V get(K key);
    Iterable<K> keySet();
    Iterable<V> values();
    boolean containsKey(K key);
    int size();

    IRevision<K,V> revision();
    IRevision<K, V> revision(String name);

    // =================================
    interface IRevision<K, V> {
        V put(K key, V value);
        void putAll(Map<K, V> m);
        V get(K key);
        V remove(K key);
        Iterable<K> keys();
        Iterable<V> values();
        boolean containsKey(K key);
        int size();
        int revisionCnt();

        void commit(); // throws
        void rollback();
    }
}

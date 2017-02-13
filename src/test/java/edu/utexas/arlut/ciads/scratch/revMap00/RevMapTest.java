package edu.utexas.arlut.ciads.scratch.revMap00;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.*;

@Slf4j
public class RevMapTest {
    @Before
    public void setUp() throws Exception {
        r = new RevMap();
    }
    // =================================
    @After
    public void tearDown() throws Exception {

    }
    // =================================
    @Test
    public void testInsert() {
        Set<Foo> expectedFoos = newHashSet();
        Set<Long> expectedIDs = newHashSet();
        for (int i=0; i<10; ++i) {
            Foo f0 = Foo.make("a", "a");
            r.put(f0.getId(), f0);
            expectedFoos.add(f0);
            expectedIDs.add(f0.getId());
        }
        assertEquals(expectedIDs, r.keySet());
        assertEquals(expectedFoos, r.values());
        r.merge();
        assertEquals(expectedIDs, r.keySet());
        assertEquals(expectedFoos, r.values());
    }
    // =================================
    @Test
    public void testInsertRollback() {
        Set<Foo> expectedFoos = newHashSet();
        Set<Long> expectedIDs = newHashSet();
        for (int i=0; i<10; ++i) {
            Foo f0 = Foo.make("a", "a");
            r.put(f0.getId(), f0);
            expectedFoos.add(f0);
            expectedIDs.add(f0.getId());
        }

        assertEquals(expectedIDs, r.keySet());
        assertEquals(expectedFoos, r.values());

        r.rollback();
        assertEquals(r.keySet(), Collections.emptySet());
        assertEquals(r.values(), Collections.emptySet());
    }
    // =================================
    @Test
    public void testInsertPartialRollback() {
        Set<Foo> expectedFoos = newHashSet();
        Set<Long> expectedIDs = newHashSet();

        Foo f0 = Foo.make("a", "a");
        r.put(f0.getId(), f0);
        expectedFoos.add(f0);
        expectedIDs.add(f0.getId());

        Foo f1 = Foo.make("a", "a");
        r.put(f1.getId(), f1);
        expectedFoos.add(f1);
        expectedIDs.add(f1.getId());

        assertEquals(newHashSet(f0.getId(), f1.getId()), r.keySet());
        assertEquals(newHashSet(f0, f1), r.values());

        log.info("pre-merge: {}", r.values());
        r.merge();

        Foo f2 = Foo.make("a", "a");
        r.put(f2.getId(), f2);

        assertEquals(newHashSet(f0.getId(), f1.getId(), f2.getId()), r.keySet());
        assertEquals(newHashSet(f0, f1, f2), r.values());

        log.info("partial-merge: {}", r.values());
        r.rollback();

        assertEquals(newHashSet(f0.getId(), f1.getId()), r.keySet());
        assertEquals(newHashSet(f0, f1), r.values());
        log.info("partial-rollback: {}", r.values());
    }
    // =================================
    @Test
    public void testContainsKey() {
        Foo f0 = Foo.make("a", "a");
        r.put(f0.getId(), f0);
        assertTrue(r.containsKey(f0.getId()));
        r.rollback();
        assertFalse(r.containsKey(f0.getId()));

        r.put(f0.getId(), f0);
        assertTrue(r.containsKey(f0.getId()));
        r.merge();
        assertTrue(r.containsKey(f0.getId()));
    }
    // =================================
    @Test
    public void testContainsValue() {
        Foo f0 = Foo.make("a", "a");
        r.put(f0.getId(), f0);
        assertTrue(r.containsValue(f0));
        r.rollback();
        assertFalse(r.containsValue(f0));

        r.put(f0.getId(), f0);
        assertTrue(r.containsValue(f0));
        r.merge();
        assertTrue(r.containsValue(f0));
    }
    // =================================
    @Test
    public void testRevisionPrimacy() {
        Foo f0 = Foo.make("a", "a");
        r.put(f0.getId(), f0);
        r.merge();
        log.info("revMap: {}", r.entrySet());
        log.info("revMap: {}", r.values());

        Foo f0a = Foo.copy(f0);
        f0a.setProperty("b", "b");
        r.put(f0a.getId(), f0a);
        log.info("revMap: {}", r.entrySet());
        log.info("revMap: {}", r.values());

    }
    // =================================
    @Test
    public void testFoo() {
        @Data
        class Bar {
            final Long id;
            final String s;
            @Override
            public int hashCode() {
                return id.intValue();
            }
            @Override
            public boolean equals(final Object rhs) {
                return id == ((Bar)rhs).id;
            }
        }

        Bar b0 = new Bar(1L, "aaa");
        Bar b1 = new Bar(1L, "bbb");
        log.info("Bar b0 {}", b0);
        log.info("Bar b1 {}", b1);
        Set<Bar> s = newHashSet(b0, b1);
        log.info("Set: {}", s);

        Set<Bar> s0 = newHashSet(b1);
        log.info("Set: {}", s0);
        s0.addAll(newHashSet(b0));
        log.info("Set: {}", s0);
    }
    // =================================
    private RevMap r;
}
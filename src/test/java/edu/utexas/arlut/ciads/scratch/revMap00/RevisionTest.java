package edu.utexas.arlut.ciads.scratch.revMap00;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.utexas.arlut.ciads.scratch.revMap00.IStore.IRevision;

import static com.google.common.collect.Iterables.isEmpty;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

@Slf4j
public class RevisionTest {
    // =================================
    @Test
    public void testPut() {
        Store.IRevision<Long, Foo> r0 = store.revision("fooRev");
        put(r0, 7L, "seven");

        // revision should have [7, "seven"]
        assertThat(r0.keys(), hasItem(7L));
        // mainline should *not* have [7, "seven"]
        assertThat(store.keySet(), not(hasItem(7L)));

        r0.commit();
        // revision should have nothing
        assertTrue(isEmpty(r0.keys()));
        // mainline should have [7, "seven"]
        assertThat(store.keySet(), hasItem(7L));


        Store.IRevision<Long, Foo> r1 = store.revision();
        put(r1, 8L, "eight");
        // revision should have [8, "eight"]
        assertThat(r1.keys(), hasItem(8L));
        // mainline should *not* have [8, "eight"]
        assertThat(store.keySet(), not(hasItem(8L)));

        r1.rollback();
        // revision should have nothing
        assertTrue(isEmpty(r1.keys()));
        // mainline should 7 from first commit
        assertThat(store.keySet(), hasItem(7L));

        // multiply put?
        // try put after commit?
    }
    // =================================
    @Test
    public void testGet() {
        Store.IRevision<Long, Foo> r0 = store.revision("fooRev");
        Foo f7 = put(r0, 7L, "seven");

        assertEquals(f7, r0.get(7L));
        assertNull(store.get(7L));

        r0.commit();
        assertNull(r0.get(7L));
        assertEquals(f7, store.get(7L));

        Store.IRevision<Long, Foo> r1 = store.revision();
        Foo f8 = put(r1, 8L, "eight");

        // revision should have 8
        assertEquals(f8, r1.get(8L));
        // mainline should *not* have 8
        assertNull(store.get(8L));

        r1.rollback();
        // revision should have nothing
        assertNull(r1.get(8L));
        // mainline should still have nothing
        assertNull(store.get(8L));
    }
    // =================================
    @Test
    public void testRemove() {

    }
    // =================================
    @Before
    public void setUp() throws Exception {
        store = new Store<>();
    }
    // =================================
    @After
    public void tearDown() throws Exception {
    }
    // =================================
    @Data
    static class Foo {
        final Long id;
        final String s;
    }
    // =================================
    private Foo put(IRevision<Long, Foo> r, Long id, String s) {
        Foo f = new Foo(id, s);
        r.put(id, f);
        return f;
    }
    // =================================
    static long sCnt = 1;
    Store<Long, Foo> store;
}
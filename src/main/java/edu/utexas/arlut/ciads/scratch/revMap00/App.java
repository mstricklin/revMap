package edu.utexas.arlut.ciads.scratch.revMap00;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main( String[] args ) {
        log.info("Hello world");

        RevMap r = new RevMap();

        Foo f0 = Foo.make("aaa", "aaa");
        r.put(f0.getId(), f0);
        r.dump();
        log.info("");
        r.merge();
        r.dump();

        log.info("");
        log.info("= add =======================================================");
        Foo f1 = Foo.make("ddd", "ddd");
        r.put(f1.getId(), f1);
        r.dump();
        log.info("");
        r.merge();
        r.dump();

        log.info("");
        log.info("= remove =======================================================");
        r.remove(1L);
        r.dump();
        log.info("");
        r.merge();
        r.dump();

        log.info("");
        log.info("= touch =======================================================");
        r.touch(2L);
        r.dump();
        log.info("");
        r.merge();
        r.dump();

        if (r != null)
            log.info("r is not null");


    }
    /*
     14:

     add vertex
     get vertex
     remove vertex
     get all vertices
     get vertex by attributes

     add edge
     get edge
     remove edge
     get all edges
     get edge by attributes

     get property
     get all properties
     set property
     remove property

     */
}

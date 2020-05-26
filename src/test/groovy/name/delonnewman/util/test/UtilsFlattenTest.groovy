package edu.cnm.util.test

import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail

class UtilsFlattenTest {

    @Test
    void testFlattenNestedMap() {
        def m = [a: 1, b: 2, c: 3, d: [e: [f: 5], g: 6], h: [i: 7]]
        
    }
}
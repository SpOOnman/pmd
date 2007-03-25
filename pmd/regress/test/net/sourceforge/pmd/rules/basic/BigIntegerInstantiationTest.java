/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package test.net.sourceforge.pmd.rules.basic;

import net.sourceforge.pmd.Rule;

import org.junit.Before;
import org.junit.Test;

import test.net.sourceforge.pmd.testframework.SimpleAggregatorTst;

public class BigIntegerInstantiationTest extends SimpleAggregatorTst {
    private Rule rule;

    @Before
    public void setUp() {
        rule = findRule("basic", "BigIntegerInstantiation");
    }

    @Test
    public void testAll() {
        runTests(rule);
    }        

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(BigIntegerInstantiationTest.class);
    }
}
package org.raml.v2.creators;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestRecursion extends CreatorTestBase{

    public static final String BASAL_RECURSION = "models/recurse.raml";

    @BeforeClass
    public static void beforeClass(){
        api = api( BASAL_RECURSION);
    }

    @Test
    public void testPerson(){
        final int times = 5;
        int t = 0;
        for ( int i =0 ; i < times; i++ ) {
            Map<String, Object> map = composite("Person");
            Map p = (Map)map.get("partner");
            if ( p != null ){
                t++;
            }
        }
        Assert.assertNotEquals(0, t);
        Assert.assertNotEquals(times, t);
    }

    @Test
    @Ignore
    public void testNodeRecursion(){
        Map<String,Object> map = composite("Node");
        Assert.assertTrue(map.get("children") instanceof List<?>);
        List<?> child = (List<?>)map.get("children");
        child.forEach((c) -> {
            Assert.assertTrue(c instanceof Map);
            Assert.assertTrue(((Map)c).get("children") instanceof List<?>);
        });
    }
}

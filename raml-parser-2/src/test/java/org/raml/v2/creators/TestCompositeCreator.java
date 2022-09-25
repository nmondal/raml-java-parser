package org.raml.v2.creators;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestCompositeCreator extends CreatorTestBase{

    @Test
    public void testAllPrimitive(){
        int count = 0;
        for ( int i =0; i < 10; i++ ){
            Optional<Map<String,Object>> person = TypeCreator.buildFrom(api, "Person");
            Assert.assertTrue(person.isPresent());
            Map<String,Object> map = person.get();
            Assert.assertNotNull( map.get("firstname"));
            Assert.assertNotNull( map.get("lastname"));
            if ( map.containsKey("title") ){
                count ++;
            }
        }
        Assert.assertNotEquals(0, count);
        Assert.assertNotEquals(10, count);
    }

    @Test
    public void testParentWithArrayProperty(){
        Optional<Map<String,Object>> mgr = TypeCreator.buildFrom(api, "Manager");
        Assert.assertTrue(mgr.isPresent());
        Map<String,Object> map = mgr.get();
        Assert.assertFalse(map.isEmpty());
    }
}

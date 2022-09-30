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
            Map<String,Object> map = composite("Person");
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
    public void testParentWithProperties(){

        Map<String,Object> map = composite("Manager");
        // is person??
        Assert.assertNotNull( map.get("firstname"));
        Assert.assertNotNull( map.get("lastname"));
        // has reports?
        List<Map> reports = (List<Map>)map.get("reports");
        Assert.assertNotNull(reports);
        // are reports persons?
        reports.forEach( (p) -> {
            Assert.assertNotNull( p.get("firstname"));
            Assert.assertNotNull( p.get("lastname"));
        });
        // now Admin
        map = composite("Admin");
        // is person??
        Assert.assertNotNull( map.get("firstname"));
        Assert.assertNotNull( map.get("lastname"));
        // has clearance ?
        Assert.assertNotNull( map.get("clearanceLevel"));

        // Now one with referenced property
        map = composite("AlertableAdmin");
        // is admin?
        Assert.assertNotNull( map.get("firstname"));
        Assert.assertNotNull( map.get("lastname"));
        Assert.assertNotNull( map.get("clearanceLevel"));
        // has phone?
        Assert.assertNotNull( map.get("phone"));
    }
}

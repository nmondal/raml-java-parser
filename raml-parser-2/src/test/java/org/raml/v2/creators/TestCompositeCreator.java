package org.raml.v2.creators;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TestCompositeCreator extends CreatorTestBase{

    @Test
    public void testAllPrimitive(){
        Optional<Map<String,Object>> person = TypeCreator.buildFrom(api, "Person");
        Assert.assertTrue(person.isPresent());
        Map<String,Object> map = person.get();
    }
}

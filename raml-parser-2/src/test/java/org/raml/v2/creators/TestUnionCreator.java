package org.raml.v2.creators;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;

public class TestUnionCreator extends CreatorTestBase{

    @Test
    public void testAllSamePrimitive(){
        // because of randomness, need to get at least one '@' and one not having '@'
        boolean email = false;
        boolean phone = false;
        for ( int i=0; i < 10; i++ ){
            Optional<String> contact = TypeCreator.buildFrom(api, "Contact");
            Assert.assertTrue(contact.isPresent());
            boolean hasAt = contact.get().contains("@");
            email = email || hasAt;
            phone = phone || !hasAt;
            if ( email && phone ) break;
        }
        Assert.assertTrue(email && phone);
    }

    @Test
    public void testDifferentPrimitive(){
        // because of randomness, need to get at least one '@' and one not having '@'
        boolean isByte = false;
        boolean isDouble = false;
        boolean isBool = false;
        boolean isString = false;
        for ( int i=0; i < 40; i++ ){
            Optional<Object> u = TypeCreator.buildFrom(api, "SomeUnion");
            Assert.assertTrue(u.isPresent());
            Object o = u.get();
            isBool = isBool || o instanceof Boolean;
            isByte = isByte || o instanceof Byte;
            isDouble = isDouble || o instanceof Double;
            isString = isString || o instanceof String;
        }
        Assert.assertTrue( isBool && isByte && isDouble && isString );
    }

}

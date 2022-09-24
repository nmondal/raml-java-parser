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
}

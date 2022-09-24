package org.raml.v2.creators;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public final class TestArrayCreators extends CreatorTestBase {

    @Test
    public void testNumericArray(){
        Optional<List<Byte>> ages = TypeCreator.buildFrom(api, "SampledAges");
        Assert.assertTrue(ages.isPresent());
        List<Byte> bytes = ages.get();
        Assert.assertTrue( bytes.size() > 0);
        bytes.forEach( (x) -> {
            Assert.assertTrue(x <= 80);
            Assert.assertTrue(x >= 18);
        });
    }

    @Test
    public void testStringArray(){
        Optional<List<String>> phones = TypeCreator.buildFrom(api, "SampledPhones");
        Assert.assertTrue(phones.isPresent());
        List<String> strings = phones.get();
        Assert.assertTrue( strings.size() >= 4);
    }

    @Test
    public void testDateOnlyArray(){
        Optional<List<String>> birthDays = TypeCreator.buildFrom(api, "SampledBirthDays");
        Assert.assertTrue(birthDays.isPresent());
        List<String> strings = birthDays.get();
        Assert.assertEquals( 5, strings.size() );
        strings.forEach( (x) -> Assert.assertEquals( 10, x.length()) );
    }
}

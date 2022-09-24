package org.raml.v2.creators;

import org.junit.Assert;
import org.junit.Test;
import java.util.Optional;

public final class TestPrimitiveCreators extends CreatorTestBase{

    @Test
    public void testString(){
        Optional<String> ph = TypeCreator.buildFrom(api, "Phone");
        Assert.assertTrue(ph.isPresent());
        String result = ph.get();
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testBoolean(){
        Optional<Boolean>  rel = TypeCreator.buildFrom(api, "RelationHuman");
        Assert.assertTrue(rel.isPresent());
    }
    @Test
    public void testInteger(){
        Optional<Number>  age = TypeCreator.buildFrom(api, "AgeHuman");
        Assert.assertTrue(age.isPresent());
        Assert.assertTrue( age.get().byteValue() >= 18);
        Assert.assertTrue( age.get().byteValue() <= 80);

        age = TypeCreator.buildFrom(api, "AgeBuilding");
        Assert.assertTrue(age.isPresent());
        Assert.assertTrue( age.get().shortValue() >= 0);
        Assert.assertTrue( age.get().shortValue() <= 20000);

        age = TypeCreator.buildFrom(api, "AgeFossil");
        Assert.assertTrue(age.isPresent());
        Assert.assertTrue( age.get().intValue() >= 0);
        Assert.assertTrue( age.get().intValue() <= 1000000);

        age = TypeCreator.buildFrom(api, "AgeExistence");
        Assert.assertTrue(age.isPresent());
        Assert.assertTrue( age.get().longValue() >= 0);
        Assert.assertTrue( age.get().longValue() <= 1000000000);
    }

    @Test
    public void testNumber(){
        Optional<Number>  fuz = TypeCreator.buildFrom(api, "Fuzziness");
        Assert.assertTrue(fuz.isPresent());

        Assert.assertTrue( fuz.get().doubleValue() >= 0.0001d);
        Assert.assertTrue( fuz.get().doubleValue() <= 1.0000d);

        fuz = TypeCreator.buildFrom(api, "FuzzyFloat");
        Assert.assertTrue(fuz.isPresent());
        Assert.assertTrue( fuz.get().doubleValue() >= 0.00f);
        Assert.assertTrue( fuz.get().doubleValue() <= 1.00f);

    }

    @Test
    public void testDates(){
        Optional<String> dt = TypeCreator.buildFrom(api, "BirthDay");
        Assert.assertTrue(dt.isPresent());
        Assert.assertEquals( 10, dt.get().length());

        dt = TypeCreator.buildFrom(api, "LunchTime");
        Assert.assertTrue(dt.isPresent());
        Assert.assertEquals( 8, dt.get().length());

        dt = TypeCreator.buildFrom(api, "BirthDateTime");
        Assert.assertTrue(dt.isPresent());
        Assert.assertEquals( 23, dt.get().length());
    }
}

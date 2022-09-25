package org.raml.v2.creators;

import org.junit.Assert;
import org.junit.Test;
import java.util.Optional;

public final class TestPrimitiveCreators extends CreatorTestBase{

    @Test
    public void testString(){
        Optional<String> ph = TypeCreator.buildFrom(api, "Phone");
        Assert.assertTrue(ph.isPresent());
        Assert.assertFalse(ph.get().isEmpty());

        Optional<String> email = TypeCreator.buildFrom(api, "Email");
        Assert.assertTrue(email.isPresent());
        Assert.assertTrue(email.get().length() <= 32 );
        Assert.assertTrue(email.get().length() >= 15 );

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

    @Test
    public void testEnums(){
        int cnt = 0;
        for ( int i =0; i < 10; i++ ) {
            Optional<String> cl = TypeCreator.buildFrom(api, "ClearanceLevel");
            Assert.assertTrue(cl.isPresent());
            Assert.assertFalse(cl.get().isEmpty());
            if ( cl.get().equals("high") ){
                cnt++;
            }
        }
        Assert.assertNotEquals(10, cnt);
        Assert.assertNotEquals(0, cnt);
        cnt = 0;
        for ( int i =0; i < 10; i++ ) {
            Optional<Integer> cl = TypeCreator.buildFrom(api, "Directions");
            Assert.assertTrue(cl.isPresent());
            if ( cl.get() == 0 ){
                cnt++;
            }
        }
        Assert.assertNotEquals(10, cnt);
        Assert.assertNotEquals(0, cnt);
    }

    @Test
    public void testRedirections(){
        // 1st level
        Optional<Boolean>  rel = TypeCreator.buildFrom(api, "R1");
        Assert.assertTrue(rel.isPresent());
        // 2nd level
        rel = TypeCreator.buildFrom(api, "R2");
        Assert.assertTrue(rel.isPresent());
    }
}

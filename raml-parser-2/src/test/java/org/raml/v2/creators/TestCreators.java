package org.raml.v2.creators;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.model.common.ValidationResult;
import org.raml.v2.api.model.v10.api.Api;

import java.io.File;
import java.util.Optional;

public class TestCreators {

    static Api api(String fileName){
        final File ramlFile = new File(fileName);
        Assert.assertTrue(ramlFile.exists());
        RamlModelResult ramlModelResult = new RamlModelBuilder().buildApi(ramlFile);
        if (ramlModelResult.hasErrors()) {
            for (ValidationResult validationResult : ramlModelResult.getValidationResults()) {
                System.err.println(validationResult.getMessage());
            }
        }
        Assert.assertFalse(ramlModelResult.hasErrors());
        Api api = ramlModelResult.getApiV10();
        Assert.assertNotNull(api);
        return api;
    }

    static Api api;

    @BeforeClass
    public static void beforeClass(){
        api = api( "models/types.raml");
    }

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

}

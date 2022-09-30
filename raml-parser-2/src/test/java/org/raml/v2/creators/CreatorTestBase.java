package org.raml.v2.creators;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.model.common.ValidationResult;
import org.raml.v2.api.model.v10.api.Api;

import java.io.File;
import java.util.Map;
import java.util.Optional;

public abstract class CreatorTestBase {
    public static Api api(String fileName){
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

    public static Map<String,Object> composite(String alias){
        Optional<Map<String,Object>> obj = TypeCreator.buildFrom(api, alias);
        Assert.assertTrue(obj.isPresent());
        Map<String,Object> map = obj.get();
        Assert.assertFalse(map.isEmpty());
        return map;
    }

    static Api api;

    @BeforeClass
    public static void beforeClass(){
        api = api( "models/types.raml");
    }

}

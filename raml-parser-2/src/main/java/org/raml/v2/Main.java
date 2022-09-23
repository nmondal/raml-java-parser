package org.raml.v2;

import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.model.common.ValidationResult;
import org.raml.v2.api.model.v10.api.Api;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        final File ramlFile = new File("raml-parser-2/models/types.raml");
        RamlModelResult ramlModelResult = new RamlModelBuilder().buildApi(ramlFile);
        if (ramlModelResult.hasErrors()) {
            for (ValidationResult validationResult : ramlModelResult.getValidationResults()) {
                System.out.println(validationResult.getMessage());
            }
        } else {
            Api api = ramlModelResult.getApiV10();
            List<TypeDeclaration> types = Objects.requireNonNull(api).types();
            for (TypeDeclaration td : types) {
                System.out.println(td.name());
            }
        }
    }
}
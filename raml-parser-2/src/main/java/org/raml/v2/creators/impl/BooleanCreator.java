package org.raml.v2.creators.impl;

import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;
import org.raml.v2.creators.TypeCreator;

public class BooleanCreator extends TypeCreator<Boolean> {

    public BooleanCreator(TypeDeclaration declaration) {
        super(declaration);
    }

    @Override
    public Boolean create() {
        return random.nextBoolean();
    }
}

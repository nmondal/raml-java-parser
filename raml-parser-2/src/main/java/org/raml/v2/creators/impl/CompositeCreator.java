package org.raml.v2.creators.impl;

import org.raml.v2.api.model.v10.datamodel.ObjectTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;
import org.raml.v2.creators.TypeCreator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CompositeCreator extends TypeCreator<Map<String,Object>> {

    Map<String,TypeCreator> propCreators = new HashMap<>();

    public ObjectTypeDeclaration typeDeclaration(){
        return (ObjectTypeDeclaration)declaration;
    }

    public CompositeCreator(TypeDeclaration declaration) {
        super(declaration);
        ObjectTypeDeclaration objectTypeDeclaration = typeDeclaration();
    }

    @Override
    public Map<String, Object> create() {
        if ( propCreators.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String,Object> map = new HashMap<>();
        return map;
    }
}

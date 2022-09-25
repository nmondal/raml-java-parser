package org.raml.v2.creators.impl;

import org.raml.v2.api.model.v10.datamodel.ObjectTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;
import org.raml.v2.creators.TypeCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CompositeCreator extends TypeCreator<Map<String,Object>> {

    static final Logger logger = LoggerFactory.getLogger(CompositeCreator.class);

    final List<TypeCreator<Object>> selfPropCreators;

    final List<TypeCreator<Object>> parentCreators;

    public ObjectTypeDeclaration typeDeclaration(){
        return (ObjectTypeDeclaration)declaration;
    }

    public CompositeCreator(TypeDeclaration declaration) {
        super(declaration);
        ObjectTypeDeclaration objectTypeDeclaration = typeDeclaration();
        selfPropCreators = objectTypeDeclaration.properties().stream().map( (x) -> typeCreatorFactory.creator(x)).toList();
        parentCreators = objectTypeDeclaration.parentTypes().stream().map( (x) -> typeCreatorFactory.creator(x)).toList();
    }

    @Override
    public Map<String, Object> create() {
        if ( selfPropCreators.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String,Object> map = new HashMap<>();
        // construct parents .... right now no collision check ???
        for ( TypeCreator<Object> tc : parentCreators){
            Object propVal = tc.create();
            if ( propVal instanceof Map<?,?>){
                Map<String,Object> pMap = (Map<String, Object>) propVal;
                map.putAll(pMap);
            } else {
                logger.error(String.format("%s did not produce object for %s", tc, this));
            }
        }
        // self - properties only for now...
        for ( TypeCreator<Object> tc : selfPropCreators){
            String propName = tc.declaration.name();
            if ( tc.shouldBuild() ){
                Object propVal = tc.create();
                map.put(propName,propVal);
            } else {
                logger.debug(String.format("%s was optional, ignoring", propName));
            }
        }
        return map;
    }
}

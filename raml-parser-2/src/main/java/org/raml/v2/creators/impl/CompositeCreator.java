package org.raml.v2.creators.impl;

import org.raml.v2.api.model.v10.datamodel.IntegerTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.ObjectTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;
import org.raml.v2.creators.TypeCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CompositeCreator extends TypeCreator<Map<String,Object>> {

    static final Logger logger = LoggerFactory.getLogger(CompositeCreator.class);

    public static final String REC_DEPTH = "::depth";
    final List<TypeCreator<Object>> selfPropCreators;

    final List<TypeCreator<Object>> parentCreators;

    public ObjectTypeDeclaration typeDeclaration(){
        return (ObjectTypeDeclaration)declaration;
    }

    private long stackSize = 0;

    final long maxStackDepth ;
    final long minStackDepth ;

    public CompositeCreator(TypeDeclaration declaration) {
        super(declaration);
        ObjectTypeDeclaration objectTypeDeclaration = typeDeclaration();
        List<TypeCreator<Object>> tmp = new ArrayList<>();
        IntegerTypeDeclaration recDepth = null;
        for ( TypeDeclaration td : objectTypeDeclaration.properties()){
            if ( td.name().equals( REC_DEPTH ) ){
                recDepth = (IntegerTypeDeclaration) td;
            } else {
                tmp.add( typeCreatorFactory.creator(td) );
            }
        }
        selfPropCreators = Collections.unmodifiableList(tmp);
        parentCreators = objectTypeDeclaration.parentTypes().stream().map( (x) -> typeCreatorFactory.creator(x)).toList();
        if ( recDepth == null ){
            minStackDepth = 0;
            maxStackDepth = Integer.MAX_VALUE;
        } else {
            minStackDepth = nullOrElse(recDepth::minimum, 0).intValue();
            maxStackDepth = nullOrElse(recDepth::maximum, 0).intValue();
        }
    }

    @Override
    public boolean maxRecursionReached(){
        return maxStackDepth <= stackSize;
    }

    @Override
    public Map<String, Object> create() {
        if ( selfPropCreators.isEmpty()) {
            // we do not need to do anything including stack - because empty set is not self referential
            return Collections.emptyMap();
        }
        if ( maxRecursionReached() ){
            logger.info("Max Recursion Depth Reached for Object Type : " + this);
            return Collections.emptyMap();
        }
        stackSize++;
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
                if ( tc.maxRecursionReached() ){
                    logger.info("Max Recursion Depth Reached for Object Type : " + tc);
                    continue;
                }
                Object propVal = tc.create();
                map.put(propName,propVal);
            } else {
                logger.debug(String.format("%s was optional, ignoring", propName));
            }
        }
        stackSize--;
        return map;
    }
}

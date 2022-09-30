package org.raml.v2.creators.impl;

import org.raml.v2.api.model.v10.datamodel.ArrayTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;
import org.raml.v2.creators.TypeCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ArrayCreator extends TypeCreator<List> {

    static final Logger logger = LoggerFactory.getLogger(ArrayCreator.class);

    final int min;
    final int max;
    final TypeCreator inner;

    public ArrayTypeDeclaration typeDeclaration(){
        return (ArrayTypeDeclaration) declaration;
    }
    public ArrayCreator(TypeDeclaration declaration) {
        super(declaration);
        ArrayTypeDeclaration arrayTypeDeclaration = typeDeclaration();
        // must present
        TypeDeclaration innerType = Objects.requireNonNull( nullOrElse( arrayTypeDeclaration::items, null));
        inner = typeCreatorFactory.creator(innerType);
        min = nullOrElse(arrayTypeDeclaration::minItems, 0);
        max = nullOrElse(arrayTypeDeclaration::maxItems, 10);
    }

    @Override
    public List create() {
        final int size;
        if ( min == max ){
            size = min;
        } else {
            size = random.nextInt(min, max);
        }
        if ( size == 0 ) {
            return Collections.emptyList();
        }
        List l = new ArrayList();
        for ( int i =0; i < size; i++ ){
            if ( inner.maxRecursionReached() ){
                logger.info("Max Recursion Depth Reached for Object Type : " + inner);
                continue;
            }
            Object val = inner.create();
            l.add(val);
        }
        return l;
    }
}

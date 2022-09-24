package org.raml.v2.creators.impl;

import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.UnionTypeDeclaration;
import org.raml.v2.creators.TypeCreator;

import java.util.List;

public class UnionCreator extends TypeCreator {

    final List<TypeCreator<Object>> propCreators ;

    public UnionTypeDeclaration typeDeclaration(){
        return (UnionTypeDeclaration) declaration;
    }

    public UnionCreator(TypeDeclaration declaration) {
        super(declaration);
        UnionTypeDeclaration unionTypeDeclaration = typeDeclaration();
        propCreators = unionTypeDeclaration.of().stream().map(x -> typeCreatorFactory.creator(x)).toList();
    }

    @Override
    public Object create() {
        final int inx;
        if ( propCreators.size() == 1){
            inx = 0;

        } else {
            inx = random.nextInt(propCreators.size());
        }
        return propCreators.get(inx).create();
    }
}

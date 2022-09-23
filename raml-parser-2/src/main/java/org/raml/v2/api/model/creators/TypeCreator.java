package org.raml.v2.api.model.creators;

import org.raml.v2.api.model.v10.datamodel.*;

import java.security.SecureRandom;
import java.util.*;

public abstract class TypeCreator {

    public static final Random random = new SecureRandom();

    protected static final Map<TypeDeclaration,TypeCreator> creators = new HashMap<>();

    public final TypeDeclaration declaration;

    public TypeCreator(TypeDeclaration decl){
        this.declaration = decl;
    }

    public boolean shouldBuild(){
        if ( declaration.required()) return true;
        // Use some distribution, eventually...?
        return random.nextBoolean();
    }

    public boolean isComplex(){
        return declaration instanceof UnionTypeDeclaration ||
                declaration instanceof  ObjectTypeDeclaration ||
                declaration instanceof ArrayTypeDeclaration ||
                declaration instanceof JSONTypeDeclaration;
    }

    public boolean isPrimitive(){
        return !isComplex();
    }

    protected abstract Optional<Object> create();

    public final Optional<Object> buildPrimitive(){
        // all primitive types are handled automatically
        return creators.get(declaration).create();
    }

    public final Optional<Object> buildComplex(){
        return Optional.empty();
    }

    public final Optional<Object> build(){
        if ( !shouldBuild() ) {
            return Optional.empty();
        }
        if ( isPrimitive() ){
            return buildPrimitive();
        }
        return buildComplex();
    }

}

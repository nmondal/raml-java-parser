package org.raml.v2.api.model.creators;

import org.raml.v2.api.model.v10.api.Api;
import org.raml.v2.api.model.v10.datamodel.*;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Supplier;

public abstract class TypeCreator {
    protected static final Map<TypeDeclaration, TypeCreator> creators = new HashMap<>();

    public static TypeCreatorFactory typeCreatorFactory = TypeCreatorFactory.DEFAULT_INSTANCE;

    public final Random random = new SecureRandom();

    protected final TypeDeclaration declaration;

    public TypeCreator(TypeDeclaration declaration) {
        this.declaration = declaration;
    }

    public boolean shouldBuild() {
        if (declaration.required()) return true;
        // Use some distribution, eventually...?
        return random.nextBoolean();
    }

    public static <T> T nullOrElse(Supplier<T> supplier, T defaultValue) {
        T val = supplier.get();
        if (val == null) return defaultValue;
        return val;
    }

    public boolean isComplex() {
        return declaration instanceof UnionTypeDeclaration ||
                declaration instanceof ObjectTypeDeclaration ||
                declaration instanceof ArrayTypeDeclaration ||
                declaration instanceof JSONTypeDeclaration;
    }

    public boolean isPrimitive() {
        return !isComplex();
    }

    protected abstract Object create();

    public static Optional<Object> buildFrom(TypeDeclaration typeDeclaration) {
        TypeCreator typeCreator = typeCreatorFactory.creator(typeDeclaration);
        if (typeCreator.shouldBuild()) {
            return Optional.of(typeCreator.create());
        }
        return Optional.empty();
    }

    public static Optional<Object> buildFrom(Api api, String typeAliasName) {
        List<TypeDeclaration> types = Objects.requireNonNull(api).types();
        for (TypeDeclaration td : types) {
            if ( td.name().equals(typeAliasName)){
                return buildFrom(td);
            }
        }
        return Optional.empty();
    }

}

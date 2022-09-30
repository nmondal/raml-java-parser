package org.raml.v2.creators;

import org.raml.v2.api.model.v10.api.Api;
import org.raml.v2.api.model.v10.datamodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class TypeCreator<R> {
    final static Logger logger = LoggerFactory.getLogger(TypeCreator.class);
    static final Map<String, TypeDeclaration> allTypes = new HashMap<>();
    static final Map<Api, Set<TypeDeclaration>> api2Types = new HashMap<>();
    public static TypeCreatorFactory typeCreatorFactory = TypeCreatorFactory.DEFAULT_INSTANCE;
    public final Random random = new SecureRandom();

    public final TypeDeclaration declaration;

    @Override
    public String toString(){
        return String.format("%s:%s:%s", declaration.name(), declaration.type(), getClass().getName());
    }

    public boolean maxRecursionReached(){
        return false;
    }

    public TypeCreator(TypeDeclaration declaration) {
        this.declaration = declaration;
        typeCreatorFactory.register(declaration,this);
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

    public abstract R create();

    public static <T> Optional<T> buildFrom(TypeDeclaration typeDeclaration) {
        TypeCreator<T> typeCreator = typeCreatorFactory.creator(typeDeclaration);
        if (typeCreator.shouldBuild()) {
            T val = typeCreator.create();
            if ( val == null ){
                logger.error( String.format("Creator [%s] produced 'null' - returning empty monad!", typeCreator));
                return Optional.empty();
            }
            return Optional.of(val);
        }
        logger.debug("shouldBuild() evaluated to 'false' - hence returning empty monad");
        return Optional.empty();
    }

    public static <T> Optional<T> buildFrom(Api api, String typeAliasName) {
        final Set<TypeDeclaration> types ;
        if ( api2Types.containsKey(api) ){
            types = api2Types.get(api);
        } else {
            types = Objects.requireNonNull(api).types()
                    .stream()
                    .peek((td) -> allTypes.put(td.name(), td)).collect(Collectors.toUnmodifiableSet());
            api2Types.put(api,types);
        }
        TypeDeclaration td = allTypes.get(typeAliasName);
        if ( td != null ){
            return buildFrom(td);
        }
        logger.error( String.format("No Top Level Type Name [%s] is found - returning empty monad!", typeAliasName));
        return Optional.empty();
    }
}

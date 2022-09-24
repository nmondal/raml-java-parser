package org.raml.v2.api.model.creators;

import org.raml.v2.api.model.creators.impl.StringCreator;
import org.raml.v2.api.model.v10.datamodel.StringTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface TypeCreatorFactory {

    Map<TypeDeclaration, TypeCreator> creators = new HashMap<>();

    Map<Class<TypeDeclaration>, Function<TypeDeclaration, TypeCreator>> constructors = new HashMap<>();

    TypeCreator creator(TypeDeclaration typeDeclaration);

    default void register(TypeDeclaration declaration, TypeCreator creator){
        creators.put(declaration,creator);
    }

    static void registerClass(Class clazz, Function<TypeDeclaration, TypeCreator> constructor){
        constructors.put(clazz,constructor);
    }

    TypeCreatorFactory DEFAULT_INSTANCE = new TypeCreatorFactory() {
        static {
            // register all...
            registerClass(StringTypeDeclaration.class, StringCreator::new);
        }

        @Override
        public TypeCreator creator(TypeDeclaration typeDeclaration) {
            if ( creators.containsKey(typeDeclaration)) return creators.get(typeDeclaration);
            Function<TypeDeclaration, TypeCreator> constructor = constructors.get( typeDeclaration.getClass());
            if ( constructor == null ) {
                return null;
            }
            return constructor.apply(typeDeclaration);
        }
    };
}

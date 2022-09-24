package org.raml.v2.creators;

import org.raml.v2.creators.impl.StringCreator;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface TypeCreatorFactory {

   final Logger logger = LoggerFactory.getLogger(TypeCreatorFactory.class);

    Map<TypeDeclaration, TypeCreator> creators = new HashMap<>();

    Map<String, Function<TypeDeclaration, TypeCreator>> constructors = new HashMap<>();

    <R> TypeCreator<R> creator(TypeDeclaration typeDeclaration);

    default void register(TypeDeclaration declaration, TypeCreator creator){
        creators.put(declaration,creator);
    }

    static void registerClass(String typeName, Function<TypeDeclaration, TypeCreator> constructor){
        constructors.put(typeName,constructor);
    }

    TypeCreatorFactory DEFAULT_INSTANCE = new TypeCreatorFactory() {
        static {
            // register all...
            registerClass("string", StringCreator::new);
        }

        @Override
        public <R> TypeCreator<R> creator(TypeDeclaration typeDeclaration) {
            if ( creators.containsKey(typeDeclaration)) return creators.get(typeDeclaration);
            Function<TypeDeclaration, TypeCreator> constructor = constructors.get( typeDeclaration.type());
            if ( constructor == null ) { // create a null creator
                logger.error( String.format( "Type Alias [%s] has no associated Type Creator (assigning null creator)!",
                        typeDeclaration.name()));
                return new TypeCreator(typeDeclaration) {
                    @Override
                    protected Object create() {
                        return null;
                    }
                };
            }
            return constructor.apply(typeDeclaration);
        }
    };
}

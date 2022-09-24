package org.raml.v2.creators.impl;

import org.raml.v2.api.model.v10.datamodel.NumberTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;
import org.raml.v2.creators.TypeCreator;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class NumberCreator extends TypeCreator<Number> {

    enum Format{
        INT8,
        INT16,
        INT32,
        INT64,
        FLOAT,
        DOUBLE
    }

    public final Double min;
    public final Double max;
    public final Double multipleOf;
    public final Format format;
    public final List<Number> options;
    public final boolean isIntRange;

    public NumberTypeDeclaration typeDeclaration(){
        return (NumberTypeDeclaration) declaration;
    }

    public boolean isEnum(){
        return options.size() != 0 ;
    }

    public static boolean isInt(double d){
        long l = (long)d;
        return (double)l == d;
    }

    private boolean computeIntRange(){
        return ( isInt(min) && isInt(max) && isInt(multipleOf) );
    }

    public NumberCreator(TypeDeclaration declaration) {
        super(declaration);
        NumberTypeDeclaration numberTypeDeclaration = typeDeclaration();
        String fmtString = nullOrElse(numberTypeDeclaration::format,"int32").toUpperCase(Locale.ROOT);
        format = Enum.valueOf( Format.class, fmtString);
        min = nullOrElse(numberTypeDeclaration::minimum,0.0d);
        max = nullOrElse(numberTypeDeclaration::maximum,100.0d);
        multipleOf = nullOrElse(numberTypeDeclaration::maximum,1.0d);
        options = nullOrElse(numberTypeDeclaration::enumValues, Collections.emptyList());
        isIntRange = computeIntRange();
    }

    @Override
    public Number create() {
        final double ret;
        if ( isEnum() ){
            int inx = random.nextInt(options.size());
            ret = options.get(inx).doubleValue();
        } else {
            //TODO inclusive exclusive multipleOf stuff later
            if ( isIntRange ){
                ret = random.nextLong(min.longValue(), max.longValue());
            } else {
                ret = random.nextDouble(min, max);
            }
        }
        return switch (format){
            case INT8 -> (byte) ret;
            case INT16 -> (short) ret;
            case INT32 -> (int) ret;
            case INT64 -> (long)ret;
            case FLOAT -> (float) ret;
            case DOUBLE -> ret;
        };
    }
}

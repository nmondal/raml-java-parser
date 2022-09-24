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
    public final Format format;
    public final long precision;
    public final List<Number> options;

    public NumberTypeDeclaration typeDeclaration(){
        return (NumberTypeDeclaration) declaration;
    }

    public boolean isEnum(){
        return options.size() != 0 ;
    }

    public NumberCreator(TypeDeclaration declaration) {
        super(declaration);
        NumberTypeDeclaration numberTypeDeclaration = typeDeclaration();
        String fmtString = nullOrElse(numberTypeDeclaration::format,"int32").toUpperCase(Locale.ROOT);
        format = Enum.valueOf( Format.class, fmtString);
        min = nullOrElse(numberTypeDeclaration::minimum,0.0d);
        max = nullOrElse(numberTypeDeclaration::maximum,100.0d);
        int p = Math.min( precision(min), precision(max));
        precision = (long)Math.pow(10,p);
        options = nullOrElse(numberTypeDeclaration::enumValues, Collections.emptyList());
    }

    public static int precision(double d){
        double f = Math.abs(d - (long)d);
        int cnt = 0;
        while( f < 0){
            cnt ++;
            f = f* 10;
        }
        return cnt;
    }

    @Override
    protected Number create() {
        final double ret;
        if ( isEnum() ){
            int inx = random.nextInt(options.size());
            ret = options.get(inx).doubleValue();
        } else {
            //TODO inclusive exclusive stuff later
            long delta = (long)(max - min)*precision;
            random.nextLong(delta);
            ret = (min + delta)/precision;
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

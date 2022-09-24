package org.raml.v2.creators.impl;

import com.mifmif.common.regex.Generex;
import org.raml.v2.creators.TypeCreator;
import org.raml.v2.api.model.v10.datamodel.StringTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;

import java.util.Collections;
import java.util.List;

public class StringCreator extends TypeCreator<String> {
    public final int min;
    public final int max;
    public final String pat;
    public final List<String> enums;
    final Generex generex;

    public StringTypeDeclaration stringTypeDeclaration(){
        return (StringTypeDeclaration) declaration;
    }

    public boolean isEnum(){
        return enums.size() != 0 ;
    }

    public StringCreator(TypeDeclaration declaration) {
        super(declaration);
        StringTypeDeclaration stringTypeDeclaration = stringTypeDeclaration();
        min = nullOrElse(stringTypeDeclaration::minLength, 10 );
        max = nullOrElse(stringTypeDeclaration::maxLength, 15 );
        pat = nullOrElse(stringTypeDeclaration::pattern, "[a-zA-Z0-9 ]*");
        enums = nullOrElse(stringTypeDeclaration::enumValues, Collections.emptyList());
        if ( isEnum() ){
            generex = null;
        } else {
            generex = new Generex(pat);
        }
    }

    @Override
    public String create() {
        final String res;
        // if enum, then sample:
        if ( isEnum() ){
            int inx = random.nextInt( enums.size() );
            res = enums.get(inx);
        } else {
            res = generex.random(min,max);
        }
        return res;
    }
}

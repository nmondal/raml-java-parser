package org.raml.v2.creators.impl;

import org.raml.v2.api.model.v10.datamodel.DateTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TimeOnlyTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;
import org.raml.v2.creators.TypeCreator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeCreator extends TypeCreator<String> {
    final SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
    final SimpleDateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
    final SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    static long minDateTimeStamp = -62135789400000L;
    static long maxDateTimeStamp = new Date().getTime() + 24 * 60* 60* 1000;

    public DateTimeCreator(TypeDeclaration declaration) {
        super(declaration);
    }

    @Override
    public String create() {
        long ts = random.nextLong(minDateTimeStamp, maxDateTimeStamp);
        Date dt = new Date(ts);
        if ( declaration instanceof DateTypeDeclaration ){
            return dateOnly.format(dt);
        } else if ( declaration instanceof TimeOnlyTypeDeclaration){
            return timeOnly.format(dt);
        } else {
            return timeStamp.format(dt);
        }
    }
}

package com.symbolflux.domainconverter;


import java.util.ArrayList;

public interface Domain {
    public void pushOccurred(AbstractConverter<? extends Convertible, ?> converter, Object finalObject);
    public void pushSetOccurred(AbstractConverter<? extends Convertible, ?> converter, ArrayList<?> finalObjects);

    public Object pullOccurred(AbstractConverter<? extends Convertible, ?> converter);
    public ArrayList<?> pullSetOccurred(AbstractConverter<? extends Convertible, ?> converter);

    default public void onReady() {

    }
}
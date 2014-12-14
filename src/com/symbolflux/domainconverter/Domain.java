package com.symbolflux.domainconverter;


import java.util.ArrayList;

public interface Domain {
    public <I extends Convertible, F extends Convertible> void pushOccurred(AbstractConverter<I, F> converter, F finalObject);
    public <I extends Convertible, F extends Convertible> void pushSetOccurred(AbstractConverter<I, F> converter, ArrayList<F> finalObjects);

    public <I extends Convertible, F extends Convertible> I pullOccurred(AbstractConverter<I, F> converter);
    public <I extends Convertible, F extends Convertible> ArrayList<I> pullSetOccurred(AbstractConverter<I, F> converter);
}
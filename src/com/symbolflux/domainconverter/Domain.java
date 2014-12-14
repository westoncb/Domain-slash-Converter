package com.symbolflux.domainconverter;


import java.util.ArrayList;

public interface Domain {
    public <I, F> void pushOccurred(AbstractConverter<I, F> converter, F finalObject);
    public <I, F> void pushSetOccurred(AbstractConverter<I, F> converter, ArrayList<F> finalObjects);

    public <I, F> I pullOccurred(AbstractConverter<I, F> converter);
    public <I, F> ArrayList<I> pullSetOccurred(AbstractConverter<I, F> converter);
}
package com.symbolflux.domainconverter;


import java.util.ArrayList;

public interface Domain {
    public void pushOccurred(AbstractConverter<? extends Convertible, ? extends Convertible> converter, Convertible finalObject);
    public void pushSetOccurred(AbstractConverter<? extends Convertible, ? extends Convertible> converter, ArrayList<? extends Convertible> finalObjects);

    public Convertible pullOccurred(AbstractConverter<? extends Convertible, ? extends Convertible> converter);
    public ArrayList<? extends Convertible> pullSetOccurred(AbstractConverter<? extends Convertible, ? extends Convertible> converter);
}
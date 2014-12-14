package com.symbolflux.domainconverter;

/**
 * Created by weston on 12/12/14.
 */
public interface Convertible {
    default public boolean modifiable() {return false;}
    default public boolean modified() {return false;}
    default public void setUnmodified(){}
    default public void markForDeletion(){}
}

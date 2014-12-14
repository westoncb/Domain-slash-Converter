package com.symbolflux.domainconverter;

/**
 * Created by weston on 12/12/14.
 */
public interface Modifiable {
    public boolean modified();
    public void setUnmodified();
}

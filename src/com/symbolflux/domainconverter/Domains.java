package com.symbolflux.domainconverter;

import java.util.ArrayList;

/**
 * Created by weston on 1/5/15.
 */
public class Domains {
    private static ArrayList<Domain> domains = new ArrayList<>();

    public static void add(Domain domain) {
        if (!domains.contains(domain))
            domains.add(domain);
    }

    public static void ready() {
        for (Domain domain : domains)
            domain.onReady();
    }
}

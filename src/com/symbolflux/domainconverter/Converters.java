package com.symbolflux.domainconverter;

import java.util.HashMap;

/**
 * Created by weston on 1/4/15.
 */
public class Converters {
    private static HashMap<Domain, HashMap<String, AbstractConverter>> map = new HashMap<>();

    public static void add(Domain owningDomain, String converterName, AbstractConverter converter) {
        mapForDomain(owningDomain).put(converterName, converter);
    }

    public static AbstractConverter get(Domain owningDomain, String converterName) {
        return mapForDomain(owningDomain).get(converterName);
    }

    private static HashMap<String, AbstractConverter> mapForDomain(Domain domain) {
        if (map.containsKey(domain))
            return map.get(domain);
        else {
            HashMap<String, AbstractConverter> nameToConverterMap = new HashMap<>();
            map.put(domain, nameToConverterMap);
            return nameToConverterMap;
        }
    }
}

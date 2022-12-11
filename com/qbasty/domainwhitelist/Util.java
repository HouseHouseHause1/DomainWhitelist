package com.qbasty.domainwhitelist;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Util {
    public Util() {
    }

    public static Set<String> getHostNames(List<String> hosts, boolean ignoreCase) {
        Set<String> result = new HashSet();
        String host;
        if (hosts != null) {
            for(Iterator var3 = hosts.iterator(); var3.hasNext(); result.add(host)) {
                host = (String)var3.next();
                if (ignoreCase) {
                    host = host.toLowerCase();
                }
            }
        }

        return result;
    }
}
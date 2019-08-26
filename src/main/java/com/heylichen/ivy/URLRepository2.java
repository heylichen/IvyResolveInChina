package com.heylichen.ivy;

import org.apache.ivy.plugins.repository.Resource;
import org.apache.ivy.plugins.repository.url.URLRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class URLRepository2 extends URLRepository {
    private Map resourcesCache = new HashMap();

    @Override
    public Resource getResource(String source) throws IOException {
        if (source.contains("..")) {
            source = source.replace("..", ".");
        }
        Resource res = (Resource) resourcesCache.get(source);
        if (res == null) {
            res = new HttpResource(source);
            resourcesCache.put(source, res);
        }
        return res;
    }
}

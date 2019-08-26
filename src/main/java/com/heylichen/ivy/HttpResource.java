package com.heylichen.ivy;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.ivy.plugins.repository.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Slf4j
public class HttpResource implements Resource {
    private String url;
    private HttpEntity entity;

    public HttpResource(String url) {
        this.url = url;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept-Encoding", "gzip,deflate");

            try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
                this.entity = response1.getEntity();
            } catch (IOException e) {
                log.error("HttpResource init ERROR", e);
                throw new RuntimeException("HttpResource init ERROR" + url);
            }
        } catch (IOException e) {
            log.error("HttpResource init ERROR", e);
            throw new RuntimeException("HttpResource init ERROR" + url);
        }
    }

    @Override
    public String getName() {
        return url;
    }

    @Override
    public long getLastModified() {
        return new Date().getTime();
    }

    @Override
    public long getContentLength() {
        return entity.getContentLength();
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public boolean isLocal() {
        return false;
    }

    @Override
    public Resource clone(String cloneName) {
        return new HttpResource(cloneName);
    }

    @Override
    public InputStream openStream() throws IOException {

        return entity.getContent();
    }

    @Override
    public String toString() {
        return url;
    }
}

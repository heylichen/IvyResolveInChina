/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.heylichen.ivy;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.ivy.util.CopyProgressListener;
import org.apache.ivy.util.FileUtil;
import org.apache.ivy.util.url.BasicURLHandler;
import org.apache.ivy.util.url.IvyAuthenticator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 */
public class BasicURLHandler2 extends BasicURLHandler {

    private static final int BUFFER_SIZE = 64 * 1024;

    public void download(URL src, File dest, CopyProgressListener l) throws IOException {
        // Install the IvyAuthenticator
        if ("http".equals(src.getProtocol()) || "https".equals(src.getProtocol())) {
            IvyAuthenticator.install();
        }

        try (CloseableHttpResponse resp = getResponse(src.toString())) {
            InputStream inStream = resp.getEntity().getContent();
            FileUtil.copy(inStream, dest, l);
        }
    }

    private CloseableHttpResponse getResponse(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept-Encoding", "gzip,deflate");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        return response1;
    }
}

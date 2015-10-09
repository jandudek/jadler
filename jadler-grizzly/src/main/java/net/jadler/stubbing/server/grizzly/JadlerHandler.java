/*
 * The MIT License
 *
 * Copyright 2015 jandudek.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.jadler.stubbing.server.grizzly;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import net.jadler.KeyValues;
import net.jadler.RequestManager;
import net.jadler.stubbing.StubResponse;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

/**
 *
 * @author jandudek
 */
class JadlerHandler extends HttpHandler {
    
    private final RequestManager requestManager;

    
    public JadlerHandler(final RequestManager requestManager) {
        this.requestManager = requestManager;
    }
    
    

    @Override
    public void service(final Request request, final Response response) throws Exception {
        final net.jadler.Request req = this.convert(request);
        final StubResponse stubResponse = this.requestManager.provideStubResponseFor(req);
        
        response.setStatus(stubResponse.getStatus());
        
        this.insertResponseHeaders(stubResponse.getHeaders(), response);
        
        this.processDelay(stubResponse.getDelay());
        
        this.insertResponseBody(stubResponse.getBody(), response);
    }
    
    private void insertResponseHeaders(final KeyValues headers, final Response response) {
        for (final String key: headers.getKeys()) {
            
            for (final String value: headers.getValues(key)) {
                response.addHeader(key, value);
            }
        }
    }
    
    private void insertResponseBody(final byte[] body, final Response response) throws IOException {
        if (body.length > 0) {
            final OutputStream os = response.getOutputStream();
            os.write(body);
        }
    }
    
    private net.jadler.Request convert(final Request request) throws IOException {
        
        final Charset encoding = isNotBlank(request.getCharacterEncoding())
                ? Charset.forName(request.getCharacterEncoding())
                : null;
        
        final net.jadler.Request.Builder builder = net.jadler.Request.builder()
                .method(request.getMethod().getMethodString())
                .requestURI(URI.create(request.getRequestURL() + getQueryString(request)))
                .body(toByteArray(request.getInputStream()));
        
        if (encoding != null) {
            builder.encoding(encoding);
        }
        
        this.addHeaders(builder, request);
        
        return builder.build();
    }
    

    private String getQueryString(final Request source) {
        return source.getQueryString() != null ? ("?" + source.getQueryString()) : "";
    }

    
    private void addHeaders(final net.jadler.Request.Builder builder, final Request req) {
        
        for (final String name: req.getHeaderNames()) {
            
            for (final String value: req.getHeaders(name)) {
                builder.header(name, value);
            }
        }
    }
    
    
    private void processDelay(final long delay) {
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

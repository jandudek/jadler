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

import net.jadler.RequestManager;
import net.jadler.stubbing.server.StubHttpServer;
import org.glassfish.grizzly.PortRange;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;

public class GrizzlyStubHttpServer implements StubHttpServer {
    
    
    private static final PortRange PORT_RANGE = PortRange.valueOf("1,65535");
    private final HttpServer server;

    public GrizzlyStubHttpServer(final int port) {
        this(new PortRange(port));
    }
    
    public GrizzlyStubHttpServer() {
        this(PORT_RANGE);
    }
    
    private GrizzlyStubHttpServer(final PortRange portRange) {
        this.server = HttpServer.createSimpleServer(null, "127.0.0.1", portRange);
    }
    
    

    @Override
    public void registerRequestManager(final RequestManager requestManager) {
        final HttpHandler handler = new JadlerHandler(requestManager);
        this.server.getServerConfiguration().addHttpHandler(handler);
    }

    @Override
    public void start() throws Exception {
        this.server.start();
    }

    @Override
    public void stop() throws Exception {
        this.server.stop();
    }

    @Override
    public int getPort() {
        return this.server.getListener("grizzly").getPort();
    }
    
}

/*
 * Copyright (c) 2014 Jadler contributors
 * This program is made available under the terms of the MIT License.
 */
package net.jadler;

import java.nio.charset.Charset;
import net.jadler.stubbing.ResponseStubbing;


/**
 * A view on a {@link JadlerMocker} instance SDRUZUJICI methods for post-instantiation configuration of the instance.
 * Used VE SPOJITOSTI S {@link Jadler#mocker()}. 
 */
public interface ConfigurableMocker {

    /**
     * Adds a default header to be included to every stub http response.
     * 
     * @param name header name (cannot be empty)
     * @param value header value (cannot be {@code null})
     */
    ConfigurableMocker defaultHeader(final String name, final String value);

    
    /**
     * Defines a default charset of every stub http response. This value will be user for all stub responses
     * unless redefined in the particular stub using {@link ResponseStubbing#withEncoding(java.nio.charset.Charset)}.
     * 
     * @param defaultEncoding default encoding (cannot be {@code null})
     * @return this instance to allow a fluid configuration
     */
    ConfigurableMocker defaultEncoding(final Charset defaultEncoding);
    
    
    /**
     * Defines a default content type of every stub http response. This value will be used for all stub responses
     * unless redefined in the particular stub using {@link ResponseStubbing#withContentType(java.lang.String)}.
     * 
     * @param defaultContentType default {@code Content-Type} header
     * @return this instance to allow a fluid configuration
     */
    ConfigurableMocker defaultContentType(final String defaultContentType);

    
    /**
     * Defines a default status of every stub http response. This value will be used for all stub responses
     * unless redefined in the particular stub using {@link ResponseStubbing#withStatus(int)}.
     * 
     * @param defaultStatus status to be returned in every stub http response. Must be at least 0.
     * @return this instance to allow a fluid configuration
     */
    ConfigurableMocker defaultStatus(final int defaultStatus);

    
    /**
     * <p>By default Jadler records all incoming requests (including their bodies) so it can provide mocking
     * (verification) features defined in {@link net.jadler.mocking.Mocker}.</p>
     *
     * <p>In some very specific corner cases this implementation of mocking can cause troubles. For example imagine
     * a long running performance test using Jadler for stubbing some remote http service. Since such a test can issue
     * thousands or even millions of requests the memory consumption probably would affect the test results (either
     * by a performance slowdown or even crashes). In this specific scenarios you should consider disabling
     * the incoming requests recording using this method.</p>
     *
     * <p>When disabled calling {@link net.jadler.mocking.Mocker#verifyThatRequest()} will result in
     * {@link java.lang.IllegalStateException}</p>
     *
     * <p>Please note you should ignore this option almost every time you use Jadler unless you are really
     * convinced about it. Because premature optimization is the root of all evil, you know.</p>
     *
     * @param recordRequests {@code true} for enabling http requests recording, {@code false} for disabling it
     * @return this instance to allow a fluid configuration
     */
    ConfigurableMocker recordRequests(final boolean recordRequests);
}

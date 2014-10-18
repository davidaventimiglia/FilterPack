package org.atomicframework.filterwheel;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CacheResponseStream extends AbstractFilterStream {
    protected ServletOutputStream output = null;
    protected OutputStream cache = null;

    public CacheResponseStream (HttpServletResponse response, OutputStream cache) throws IOException {
        super(response, cache);
        closed = false;
        this.cache = cache;}

    protected OutputStream getBaseStream () {
        return cache;}}

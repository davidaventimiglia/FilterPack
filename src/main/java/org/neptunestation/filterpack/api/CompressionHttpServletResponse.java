package org.neptunestation.filterpack.api;

import java.io.*;
import java.net.*;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;

public abstract class CompressionHttpServletResponse extends ByteBucketHttpServletResponse {
    public CompressionHttpServletResponse (HttpServletResponse origRes) throws ServletException {
        super(origRes);}

    @Override public ServletOutputStream getOutputStream () throws IOException {
        if (myOutputStream!=null) throw new IllegalStateException("getOutputStream has already been called.");
        if (myWriter!=null) throw new IllegalStateException("getWriter has already been called.");
        final GZIPOutputStream compressor = new GZIPOutputStream(getBucket());
        return new ComposableServletOutputStream(getBucket()) {
            @Override public void flush () throws IOException {
                getNestedStream().flush();
                compressor.finish();}};}}


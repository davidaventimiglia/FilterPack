package org.neptunestation.filterpack.api;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public abstract class ByteBucketHttpServletResponse extends HttpServletResponseWrapper {
    protected PrintWriter myWriter = null;
    protected ServletOutputStream myOutputStream = null;
    protected ByteArrayOutputStream bucket = new ByteArrayOutputStream();
    protected ComposableServletOutputStream servletStream = new ComposableServletOutputStream(bucket) {};

    public ByteBucketHttpServletResponse (HttpServletResponse origRes) {
        super(origRes);}

    public InputStream getInputStream () {
        return new ByteArrayInputStream(bucket.toByteArray());}

    public byte[] toByteArray () {
        return bucket.toByteArray();}

    @Override public void flushBuffer () throws IOException {
        if (myWriter==null && myOutputStream==null) throw new IllegalStateException("HttpServletResponse is not properly initialized.");
        if (myWriter!=null) myWriter.flush();
        if (myOutputStream!=null) myOutputStream.flush();}

    @Override public ServletOutputStream getOutputStream () throws IOException {
        if (myOutputStream!=null) throw new IllegalStateException("getOutputStream has already been called.");
        if (myWriter!=null) throw new IllegalStateException("getWriter has already been called.");
        myOutputStream = servletStream;
        return myOutputStream;}

    @Override public PrintWriter getWriter () throws IOException {
        if (myOutputStream!=null) throw new IllegalStateException("getOutputStream has already been called.");
        if (myWriter!=null) throw new IllegalStateException("getWriter has already been called.");
        myWriter = new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding()));
        return myWriter;}}

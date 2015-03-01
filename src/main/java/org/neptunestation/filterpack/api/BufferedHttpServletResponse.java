package org.neptunestation.filterpack.api;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public abstract class BufferedHttpServletResponse extends HttpServletResponseWrapper {
    protected ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    protected PrintWriter myWriter = null;
    protected ServletOutputStream myOutputStream = null;

    public BufferedHttpServletResponse (HttpServletResponse origRes) {
        super(origRes);}

    public InputStream getInputStream () {
        return new ByteArrayInputStream(getBuffer().toByteArray());}

    protected byte[] toByteArray () {
        return getBuffer().toByteArray();}

    protected ByteArrayOutputStream getBuffer () {
        return buffer;}

    @Override public void flushBuffer () throws IOException {
        if (myWriter==null && myOutputStream==null) throw new IllegalStateException("HttpServletResponse is not properly initialized.");
        if (myWriter!=null) myWriter.flush();
        if (myOutputStream!=null) myOutputStream.flush();
        super.flushBuffer();}

    @Override public ServletOutputStream getOutputStream () throws IOException {
        if (myWriter!=null) throw new IllegalStateException("getWriter has already been called.");
        if (myOutputStream!=null) return myOutputStream;
        myOutputStream = new ComposableServletOutputStream(getBuffer()) {
            @Override public void flush () throws IOException {
                super.flush();
                commit(toByteArray());}};
        return myOutputStream;}

    protected void commit (byte[] contents) throws IOException {
        setContentLength(contents.length);
        getResponse().getOutputStream().write(contents);
        getResponse().getOutputStream().flush();}

    @Override public PrintWriter getWriter () throws IOException {
        if (myOutputStream!=null) throw new IllegalStateException("getOutputStream has already been called.");
        if (myWriter!=null) return myWriter;
        myWriter = new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding()));
        return myWriter;}}

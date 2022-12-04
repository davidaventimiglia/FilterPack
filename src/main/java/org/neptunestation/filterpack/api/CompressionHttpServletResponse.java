package org.neptunestation.filterpack.api;

import java.io.*;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;

public abstract class CompressionHttpServletResponse extends BufferedHttpServletResponse {
    public CompressionHttpServletResponse (HttpServletResponse origRes) throws ServletException {
        super(origRes);}

    @Override public ServletOutputStream getOutputStream () throws IOException {
        myOutputStream = new ComposableServletOutputStream(getBuffer()) {
            @Override public void flush () throws IOException {
                super.flush();
                commit(toTransformedByteArray());}};
        return myOutputStream;}

    @Override protected void commit (byte[] contents) throws IOException {
        addHeader("Content-Encoding", "gzip");
        super.commit(contents);}

    protected byte[] toTransformedByteArray () throws IOException {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        GZIPOutputStream compressor = new GZIPOutputStream(target);
        compressor.write(toByteArray());
        compressor.finish();
        return target.toByteArray();}}

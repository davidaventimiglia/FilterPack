package org.neptunestation.filterpack.api;

import java.io.*;
import java.net.*;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;

public abstract class CompressionHttpServletResponse extends BufferedHttpServletResponse {
    public CompressionHttpServletResponse (HttpServletResponse origRes) throws ServletException {
        super(origRes);}

    @Override public ServletOutputStream getOutputStream () throws IOException {
        final GZIPOutputStream compressor = new GZIPOutputStream(getBuffer());
        myOutputStream = new ComposableServletOutputStream(compressor) {
            @Override public void flush () throws IOException {
                nestedStream.flush();
                super.flush();
                compressor.finish();
                commit(toByteArray());}};
        return myOutputStream;}}


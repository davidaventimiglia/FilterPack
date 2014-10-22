package org.atomicframework.filterwheel.filters;

import java.io.*;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.atomicframework.filterwheel.api.*;

public class CompressionFilter extends AbstractHttpFilter {
    @Override protected void doFilter (HttpServletRequest origReq, final HttpServletResponse origRes, FilterChain chain) throws IOException, ServletException {
        if (req.getHeader("accept-encoding")==null || req.getHeader("accept-encoding").indexOf("gzip")==-1) {chain.doFilter(req, res); return;}
        ByteArrayOutputStream byteBucket = new ByteArrayOutputStream();
        GZIPOutputStream compressor = new GZIPOutputStream(byteBucket);
        final ComposableServletOutputStream bucketStream = new ComposableServletOutputStream(compressor);
        HttpServletResponseWrapper newResponse = new HttpServletResponseWrapper(res) {
                private PrintWriter myWriter = null;
                private ServletOutputStream myOutputStream = null;
                @Override public void flushBuffer () throws IOException {
                    if (myWriter==null && myOutputStream==null) throw new IllegalStateException("HttpServletResponse is not properly initialized.");
                    if (myWriter!=null) myWriter.flush();
                    if (myOutputStream!=null) myOutputStream.flush();}
                @Override public ServletOutputStream getOutputStream () throws IOException {
                    if (myOutputStream!=null) throw new IllegalStateException("getOutputStream has already been called.");
                    if (myWriter!=null) throw new IllegalStateException("getWriter has already been called.");
                    myOutputStream = bucketStream;
                    return myOutputStream;}
                @Override public PrintWriter getWriter () throws IOException {
                    if (myOutputStream!=null) throw new IllegalStateException("getOutputStream has already been called.");
                    if (myWriter!=null) throw new IllegalStateException("getWriter has already been called.");
                    myWriter = new PrintWriter(new OutputStreamWriter(bucketStream, getCharacterEncoding()));
                    return myWriter;}};
        chain.doFilter(origReq, newResponse);
        newResponse.flushBuffer();
        bucketStream.flush();
        compressor.finish();
        byte[] contents = byteBucket.toByteArray();
        origRes.addHeader("Content-Encoding", "gzip");
        origRes.setContentLength(contents.length);
        origRes.getOutputStream().write(contents);}}

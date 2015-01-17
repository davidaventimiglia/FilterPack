package org.neptunestation.filterpack.filters;

import java.io.*;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.neptunestation.filterpack.api.*;

public class CompressionFilter extends AbstractHttpFilter {
    @Override protected void doFilter (HttpServletRequest origReq, final HttpServletResponse origRes, FilterChain chain) throws IOException, ServletException {
        if (origReq.getHeader("accept-encoding")==null || origReq.getHeader("accept-encoding").indexOf("gzip")==-1) {chain.doFilter(origReq, origRes); return;}
        ByteArrayOutputStream byteBucket = new ByteArrayOutputStream();
        final GZIPOutputStream compressor = new GZIPOutputStream(byteBucket);
        final ComposableServletOutputStream bucketStream = new ComposableServletOutputStream(compressor) {};
        HttpServletResponseWrapper newResponse = new HttpServletResponseWrapper(origRes) {
                private PrintWriter myWriter = null;
                private ServletOutputStream myOutputStream = null;
                @Override public void flushBuffer () throws IOException {
                    if (myWriter==null && myOutputStream==null) throw new IllegalStateException("HttpServletResponse is not properly initialized.");
                    if (myWriter!=null) myWriter.flush();
                    if (myOutputStream!=null) myOutputStream.flush();
                    bucketStream.flush();
                    compressor.finish();}
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
        byte[] contents = byteBucket.toByteArray();
        origRes.addHeader("Content-Encoding", "gzip");
        origRes.setContentLength(contents.length);
        origRes.getOutputStream().write(contents);}}

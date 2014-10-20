package org.atomicframework.filterwheel.filters;

import java.io.*;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.atomicframework.filterwheel.api.*;

public class GZIPFilter extends AbstractHttpFilter {
    protected void doFilter (HttpServletRequest req, final HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getHeader("accept-encoding")==null || req.getHeader("accept-encoding").indexOf("gzip")==-1) {chain.doFilter(req, res); return;}
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        GZIPOutputStream deflator = new GZIPOutputStream(bytes);
        final ComposableServletOutputStream stream = new ComposableServletOutputStream(deflator);
        HttpServletResponseWrapper response = new HttpServletResponseWrapper(res) {
                private PrintWriter writer = null;
                boolean isOutputStreamReady = false;
                boolean isWriterReady = false;
                @Override public void flushBuffer () throws IOException {
                    if (writer!=null) writer.flush(); else stream.flush();}
                @Override public ServletOutputStream getOutputStream () throws IOException {
                    if (isOutputStreamReady) throw new IllegalStateException("getOutputStream has already been called.");
                    if (isWriterReady) throw new IllegalStateException("getWriter has already been called.");
                    isOutputStreamReady = true;
                    return stream;}
                @Override public PrintWriter getWriter () throws IOException {
                    if (isOutputStreamReady) throw new IllegalStateException("getOutputStream has already been called.");
                    if (isWriterReady) throw new IllegalStateException("getWriter has already been called.");
                    writer = new PrintWriter(new OutputStreamWriter(stream));
                    isWriterReady = true;
                    return writer;}};
        chain.doFilter(req, response);
        response.flushBuffer();
        stream.flush();
        deflator.finish();
        byte[] contents = bytes.toByteArray();
        res.addHeader("Content-Encoding", "gzip");
        res.setContentLength(contents.length);
        res.getOutputStream().write(contents);}}

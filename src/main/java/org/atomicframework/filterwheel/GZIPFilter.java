package org.atomicframework.filterwheel;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class GZIPFilter extends AbstractHttpFilter {
    protected void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getHeader("accept-encoding")==null || req.getHeader("accept-encoding").indexOf("gzip")==-1) {chain.doFilter(req, res); return;}
        GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(res);
        chain.doFilter(req, wrappedResponse);
        wrappedResponse.finishResponse();}

    public static class GZIPResponseStream extends AbstractFilterStream {
        protected ByteArrayOutputStream baos = null;
        protected GZIPOutputStream gzipstream = null;

        protected OutputStream getBaseStream () {
            return gzipstream;}

        public GZIPResponseStream (HttpServletResponse response) throws IOException {
            super(response, response.getOutputStream());
            baos = new ByteArrayOutputStream();
            gzipstream = new GZIPOutputStream(baos);}

        public void close () throws IOException {
            if (isClosed()) throw new IOException("This output stream has already been closed");
            gzipstream.finish();
            byte[] bytes = baos.toByteArray();
            getResponse().addHeader("Content-Length", Integer.toString(bytes.length)); 
            getResponse().addHeader("Content-Encoding", "gzip");
            getBaseStream().write(bytes);
            getBaseStream().flush();
            super.close();}}

    public static class GZIPResponseWrapper extends HttpServletResponseWrapper {
        protected HttpServletResponse origResponse = null;
        protected ServletOutputStream stream = null;
        protected PrintWriter writer = null;

        public GZIPResponseWrapper (HttpServletResponse response) {
            super(response);
            origResponse = response;}

        public ServletOutputStream createOutputStream () throws IOException {
            return (new GZIPResponseStream(origResponse));}

        public void finishResponse () {
            try {
                if (writer!=null) {writer.close(); return;}
                if (stream!=null) {stream.close(); return;}}
            catch (IOException e) {}}

        public void flushBuffer () throws IOException {
            stream.flush();}

        public ServletOutputStream getOutputStream () throws IOException {
            if (writer!=null) throw new IllegalStateException("getWriter() has already been called!");
            if (stream==null) stream = createOutputStream();
            return stream;}

        public PrintWriter getWriter () throws IOException {
            if (writer!=null) return writer;
            if (stream!=null) throw new IllegalStateException("getOutputStream() has already been called!");
            stream = createOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(stream, "UTF-8"));
            return writer;}

        public void setContentLength(int length) {}}}

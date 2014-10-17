package org.atomicframework.filterwheel;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class GZIPFilter implements Filter {
    public void doFilter (ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest) doFilter((HttpServletRequest)req, (HttpServletResponse)res, chain);}

    protected void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getHeader("accept-encoding")==null || req.getHeader("accept-encoding").indexOf("gzip")==-1) {chain.doFilter(req, res); return;}
        GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(res);
        chain.doFilter(req, wrappedResponse);
        wrappedResponse.finishResponse();}

    public void init (FilterConfig filterConfig) {}

    public void destroy() {}

    public static class GZIPResponseStream extends ServletOutputStream {
        protected ByteArrayOutputStream baos = null;
        protected GZIPOutputStream gzipstream = null;
        protected boolean closed = false;
        protected HttpServletResponse response = null;
        protected ServletOutputStream output = null;

        public GZIPResponseStream (HttpServletResponse response) throws IOException {
            super();
            closed = false;
            this.response = response;
            this.output = response.getOutputStream();
            baos = new ByteArrayOutputStream();
            gzipstream = new GZIPOutputStream(baos);}

        public void close () throws IOException {
            if (closed) throw new IOException("This output stream has already been closed");
            gzipstream.finish();
            byte[] bytes = baos.toByteArray();
            response.addHeader("Content-Length", Integer.toString(bytes.length)); 
            response.addHeader("Content-Encoding", "gzip");
            output.write(bytes);
            output.flush();
            output.close();
            closed = true;}

        public void flush () throws IOException {
            if (closed) throw new IOException("Cannot flush a closed output stream");
            gzipstream.flush();}

        public void write (int b) throws IOException {
            if (closed) throw new IOException("Cannot write to a closed output stream");
            gzipstream.write((byte)b);}

        public void write (byte b[]) throws IOException {
            write(b, 0, b.length);}

        public void write (byte b[], int off, int len) throws IOException {
            if (closed) throw new IOException("Cannot write to a closed output stream");
            gzipstream.write(b, off, len);}

        public boolean closed () {
            return closed;}
  
        public void reset () {}

        public boolean isReady () {return true;}

        public void setWriteListener (WriteListener listener) {}}

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

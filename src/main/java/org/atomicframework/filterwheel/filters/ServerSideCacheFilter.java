package org.atomicframework.filterwheel.filters;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.atomicframework.filterwheel.api.*;

public class ServerSideCacheFilter extends AbstractHttpFilter {
    private ServletContext sc;
    private long cacheTimeout = Long.MAX_VALUE;

    @Override protected void doFilter (HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // check if was a resource that shouldn't be cached.
        String r = sc.getRealPath("");
        String path = fc.getInitParameter(request.getRequestURI());
        if (path!=null && path.equals("nocache")) {chain.doFilter(request, response); return;}

        path = r+path;

        // customize to match parameters
        String id = request.getRequestURI()+request.getQueryString();
        // optionally append i18n sensitivity
        String localeSensitive = fc.getInitParameter("locale-sensitive");
        if (localeSensitive != null) {
            StringWriter ldata = new StringWriter();
            Enumeration locales = request.getLocales();
            while (locales.hasMoreElements()) {
                Locale locale = (Locale)locales.nextElement();
                ldata.write(locale.getISO3Language());}
            id = id + ldata.toString();}
        File tempDir = (File)sc.getAttribute("javax.servlet.context.tempdir");

        // get possible cache
        String temp = tempDir.getAbsolutePath();
        File file = new File(temp+id);

        // get current resource
        if (path == null) path = sc.getRealPath(request.getRequestURI());
        File current = new File(path);

        try {
            long now = Calendar.getInstance().getTimeInMillis();
            //set timestamp check
            if (!file.exists() || (file.exists() && current.lastModified() > file.lastModified()) || cacheTimeout < now - file.lastModified()) {
                String name = file.getAbsolutePath();
                name = name.substring(0,name.lastIndexOf("/")==-1?0:name.lastIndexOf("/"));
                new File(name).mkdirs();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                CacheResponseWrapper wrappedResponse = new CacheResponseWrapper(response, baos);
                chain.doFilter(request, wrappedResponse);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();}}
        catch (ServletException e) {if (!file.exists()) throw new ServletException(e);}
        catch (IOException e) {if (!file.exists()) throw e;}

        FileInputStream fis = new FileInputStream(file);
        String mt = sc.getMimeType(request.getRequestURI());
        response.setContentType(mt);
        ServletOutputStream sos = response.getOutputStream();
        for (int i = fis.read(); i!= -1; i = fis.read()) sos.write((byte)i);}

    public void init (FilterConfig filterConfig) {
        super.init(filterConfig);
        String ct = fc.getInitParameter("cacheTimeout");
        if (ct!=null) cacheTimeout = 60*1000*Long.parseLong(ct);
        this.sc = filterConfig.getServletContext();}

    public void destroy () {
        super.destroy();
        this.sc = null;}

    public static class CacheResponseStream extends AbstractFilterStream {
        protected ServletOutputStream output = null;
        protected OutputStream cache = null;

        public CacheResponseStream (HttpServletResponse response, OutputStream cache) throws IOException {
            super(response);
            closed = false;
            this.cache = cache;}

        protected OutputStream getBaseStream () {
            return cache;}}

    public static class CacheResponseWrapper extends HttpServletResponseWrapper {
        protected HttpServletResponse origResponse = null;
        protected ServletOutputStream stream = null;
        protected PrintWriter writer = null;
        protected OutputStream cache = null;

        public CacheResponseWrapper (HttpServletResponse response, OutputStream cache) {
            super(response);
            origResponse = response;
            this.cache = cache;}

        public ServletOutputStream createOutputStream () throws IOException {
            return (new CacheResponseStream(origResponse, cache));}

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
            return writer;}}}

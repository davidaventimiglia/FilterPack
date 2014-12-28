package org.neptunestation.filterpack.filters;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import org.neptunestation.filterpack.api.*;

public class XMLTransformFilter extends AbstractHttpFilter {
    public static String XSL = "XSL";

    @Override protected void doFilter (HttpServletRequest origReq, HttpServletResponse origRes, FilterChain chain) throws IOException, ServletException {
        
        ByteArrayOutputStream byteBucket = new ByteArrayOutputStream();
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        final ComposableServletOutputStream bucketStream = new ComposableServletOutputStream(byteBucket);
        HttpServletResponseWrapper newResponse = new HttpServletResponseWrapper(origRes) {
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
        try {
            if ((origRes.getContentType()+"").matches(".*/.*\\+?xml.*")) {
                System.err.println("TRANSFORMING");
                System.err.println(getFilterConfig().getInitParameter(XSL));
                TransformerFactory
                    .newInstance()
                    .newTransformer(new StreamSource(new StringReader(getFilterConfig().getInitParameter(XSL))))
                    .transform(new StreamSource(new StringReader(new String(byteBucket.toByteArray()))), new StreamResult(target));
                byte[] contents = target.toByteArray();
                origRes.setContentLength(contents.length);
                origRes.getOutputStream().write(contents);}
            else {
                System.err.println("NOT TRANSFORMING");
                byte[] contents = byteBucket.toByteArray();
                origRes.setContentLength(contents.length);
                origRes.getOutputStream().write(contents);}}
        catch (Throwable t) {t.printStackTrace(System.err);}}}

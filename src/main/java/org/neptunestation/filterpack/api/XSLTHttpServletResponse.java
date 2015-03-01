package org.neptunestation.filterpack.api;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public abstract class XSLTHttpServletResponse extends BufferedHttpServletResponse {
    protected Source xslt = null;
    protected Transformer transformer = null;

    public XSLTHttpServletResponse (HttpServletResponse origRes, Source xslt) throws ServletException {
        super(origRes);
        this.xslt = xslt;
        try {this.transformer = TransformerFactory.newInstance().newTransformer(xslt);}
        catch (Throwable t) {throw new ServletException(t);}}

    public XSLTHttpServletResponse (HttpServletResponse origRes, Reader xslt) throws ServletException {
        this(origRes, new StreamSource(xslt));}

    public XSLTHttpServletResponse (HttpServletResponse origRes, String xslt) throws ServletException {
        this(origRes, new StringReader(xslt));}

    @Override public ServletOutputStream getOutputStream () throws IOException {
        if (!(getContentType()+"").matches(".*/.*\\+?xml.*")) return super.getOutputStream();
        myOutputStream = new ComposableServletOutputStream(getBuffer()) {
            @Override public void flush () throws IOException {
                super.flush();
                commit(toTransformedByteArray());}};
        return myOutputStream;}

    protected byte[] toTransformedByteArray () throws IOException {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {transformer.transform(new StreamSource(getInputStream()), new StreamResult(target));}
        catch (Throwable t) {throw new IOException(t);}
        return target.toByteArray();}}

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
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ByteBucketHttpServletResponse newResponse = new ByteBucketHttpServletResponse(origRes){};
        chain.doFilter(origReq, newResponse);
        try {
            if ((origRes.getContentType()+"").matches(".*/.*\\+?xml.*")) {
                System.err.println("TRANSFORMING");
                System.err.println(getFilterConfig().getInitParameter(XSL));
                TransformerFactory
                    .newInstance()
                    .newTransformer(new StreamSource(new StringReader(getFilterConfig().getInitParameter(XSL))))
                    .transform(new StreamSource(newResponse.getInputStream()), new StreamResult(target));
                byte[] contents = target.toByteArray();
                origRes.setContentLength(contents.length);
                origRes.getOutputStream().write(contents);}
            else {
                System.err.println("NOT TRANSFORMING");
                byte[] contents = newResponse.toByteArray();
                origRes.setContentLength(contents.length);
                origRes.getOutputStream().write(contents);}}
        catch (Throwable t) {t.printStackTrace(System.err);}}}

package org.neptunestation.filterpack.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.neptunestation.filterpack.api.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class XMLTransformFilter extends AbstractHttpFilter {
    @Override protected void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            StreamSource styleSource = new StreamSource();
            Transformer transformer = TransformerFactory.newInstance().newTransformer(styleSource);
            Document document = builder.parse("foo");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(res.getWriter());
            transformer.transform(source, result);}
        catch (Throwable t) {}
        chain.doFilter(req, res);}}

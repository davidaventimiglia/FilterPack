package org.neptunestation.filterpack.filters;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.neptunestation.filterpack.api.*;

public class XMLTransformFilter extends BufferedFilter {
    public static String XSL = "XSL";

    protected String getXSL () {
        return getFilterConfig().getInitParameter(XSL);}

    @Override protected HttpServletResponse wrapResponse (HttpServletResponse origRes) throws ServletException {
        return new XSLTHttpServletResponse(origRes, getXSL()){};}}

package org.neptunestation.filterpack.filters;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.neptunestation.filterpack.api.*;

public class XSLTStyleSheetInjectorFilter extends XMLTransformFilter {
    public static String XSL = "XSL";

    @Override protected void doFilter (HttpServletRequest origReq, HttpServletResponse origRes, FilterChain chain) throws IOException, ServletException {chain.doFilter(origReq, new XSLTHttpServletResponse(origRes, getFilterConfig().getInitParameter(XSL)){});}}

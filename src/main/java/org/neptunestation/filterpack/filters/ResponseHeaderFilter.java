package org.neptunestation.filterpack.filters;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.neptunestation.filterpack.api.*;

public class ResponseHeaderFilter extends AbstractHttpFilter {
    @Override protected void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        for (Enumeration e=fc.getInitParameterNames(); e.hasMoreElements();) {
            String headerName = (String)e.nextElement();
            res.addHeader(headerName, fc.getInitParameter(headerName));}
        chain.doFilter(req, res);}}

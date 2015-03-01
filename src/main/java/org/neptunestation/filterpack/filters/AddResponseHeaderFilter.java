package org.neptunestation.filterpack.filters;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.neptunestation.filterpack.api.*;

public class AddResponseHeaderFilter extends AbstractHttpFilter {
    @Override protected void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        for (Enumeration e = getFilterConfig().getInitParameterNames(); e.hasMoreElements();) {
            String name = (String)e.nextElement();
            res.addHeader(name, getFilterConfig().getInitParameter(name));}
        chain.doFilter(req, res);}}

package org.neptunestation.filterpack.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.neptunestation.filterpack.api.*;

public class ClientSideCacheFilter extends AbstractHttpFilter {
    public static String MAX_AGE = "MAX_AGE";

    @Override protected void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        res.addHeader("Cache-Control", String.format("max-age=%s", getFilterConfig().getInitParameter(MAX_AGE)));
        chain.doFilter(req, res);}}

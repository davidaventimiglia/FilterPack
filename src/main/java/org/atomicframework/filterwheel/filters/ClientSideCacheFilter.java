package org.atomicframework.filterwheel.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.atomicframework.filterwheel.api.*;

public class ClientSideCacheFilter extends AbstractHttpFilter {
    @Override protected void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        res.addHeader("Cache-Control", "max-age=3600");
        chain.doFilter(req, res);}}

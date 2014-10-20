package org.atomicframework.filterwheel.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.atomicframework.filterwheel.api.*;

public class ClientSideNoCacheFilter extends AbstractHttpFilter {
    @Override protected void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        res.addHeader("Cache-Control", "private,no-cache,no-store");
        chain.doFilter(req, res);}}

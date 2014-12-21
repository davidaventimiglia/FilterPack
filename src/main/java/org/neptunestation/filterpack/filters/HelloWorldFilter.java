package org.neptunestation.filterpack.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.neptunestation.filterpack.api.*;

public class HelloWorldFilter extends AbstractHttpFilter {
    @Override protected void doFilter (HttpServletRequest req, final HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        out.println("Hello, World!");}}

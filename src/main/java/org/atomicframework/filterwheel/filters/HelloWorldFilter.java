package org.atomicframework.filterwheel.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.atomicframework.filterwheel.api.*;

public class HelloWorldFilter extends AbstractHttpFilter {
    protected void doFilter (HttpServletRequest req, final HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        out.println("Hello, World!");}}

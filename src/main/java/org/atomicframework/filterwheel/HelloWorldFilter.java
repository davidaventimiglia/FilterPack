package org.atomicframework.filterwheel;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HelloWorldFilter extends AbstractHttpFilter {
    protected void doFilter (HttpServletRequest req, final HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        out.println("Hello, World!");}}
        

package org.atomicframework.filterwheel;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class GZIPFilter implements Filter {
    public void doFilter (ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest) doFilter((HttpServletRequest)req, (HttpServletResponse)res, chain);}

    protected void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getHeader("accept-encoding")==null || req.getHeader("accept-encoding").indexOf("gzip")==-1) {chain.doFilter(req, res); return;}
        GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(res);
        chain.doFilter(req, wrappedResponse);
        wrappedResponse.finishResponse();}

    public void init (FilterConfig filterConfig) {}

    public void destroy() {}}

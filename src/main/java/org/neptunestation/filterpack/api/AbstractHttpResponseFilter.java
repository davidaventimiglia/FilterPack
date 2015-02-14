package org.neptunestation.filterpack.api;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public abstract class AbstractHttpResponseFilter extends AbstractHttpFilter {

    protected abstract HttpServletResponse wrapResponse (HttpServletResponse origRes) throws ServletException;

    @Override protected void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(req, wrapResponse(res));}}

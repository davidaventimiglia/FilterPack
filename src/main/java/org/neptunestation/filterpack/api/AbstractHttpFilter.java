package org.neptunestation.filterpack.api;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public abstract class AbstractHttpFilter implements Filter {
    protected FilterConfig fc;
    protected abstract void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException;

    protected FilterConfig getFilterConfig () {
        return fc;}

    @Override public void doFilter (ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        getFilterConfig().getServletContext().log(String.format("Applying filter:  %s", getFilterConfig().getFilterName()));
        if (req==null) throw new IllegalArgumentException("HttpServletRequest is required.");
        if (res==null) throw new IllegalArgumentException("HttpServletResponse is required.");
        if (chain==null) throw new IllegalArgumentException("FilterChain is required.");
        if (req instanceof HttpServletRequest) doFilter((HttpServletRequest)req, (HttpServletResponse)res, chain);}

    @Override public void init (FilterConfig filterConfig) {
        this.fc = filterConfig;}

    @Override public void destroy () {
        this.fc = null;}}

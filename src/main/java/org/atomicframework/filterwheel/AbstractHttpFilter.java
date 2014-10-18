package org.atomicframework.filterwheel;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public abstract class AbstractHttpFilter implements Filter {
    protected FilterConfig fc;

    public void doFilter (ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest) doFilter((HttpServletRequest)req, (HttpServletResponse)res, chain);}

    protected abstract void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException;

    public void init (FilterConfig filterConfig) {
        this.fc = filterConfig;}

    public void destroy () {
        this.fc = null;}}

package org.neptunestation.filterpack.filters;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.neptunestation.filterpack.api.*;

public class CompressionFilterExt extends BufferedFilter {
    protected HttpServletResponse wrapResponse (HttpServletResponse origRes) throws ServletException {
        return new CompressionHttpServletResponse(origRes){};}

    @Override protected void doFilter (HttpServletRequest origReq, HttpServletResponse origRes, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(origReq, wrapResponse(origRes));}}

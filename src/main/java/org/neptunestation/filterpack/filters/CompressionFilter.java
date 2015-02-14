package org.neptunestation.filterpack.filters;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.neptunestation.filterpack.api.*;

public class CompressionFilter extends BufferedFilter {
    @Override protected HttpServletResponse wrapResponse (HttpServletResponse origRes) throws ServletException {
        return new CompressionHttpServletResponse(origRes){};}}


package org.neptunestation.filterpack.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.neptunestation.filterpack.api.*;

public class RequestHeaderRewrite extends AbstractHttpFilter {
    @Override protected void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
	if ((""+req.getParameter("method")).equalsIgnoreCase("PUT"))
	    chain.doFilter(new HttpServletRequestWrapper(req) {@Override public String getMethod () {return "PUT";}},
			   res);}}




package org.neptunestation.filterpack.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.neptunestation.filterpack.api.*;

public class TransformResponseHeaderFilter extends AbstractHttpResponseFilter {
    public static String TRIGGER_HEADER = "TRIGGER_HEADER";
    public static String TRIGGER_HEADER_REGEXP = "TRIGGER_HEADER_REGEXP";
    public static String TARGET_HEADER_NAME = "TARGET_HEADER_NAME";
    public static String TARGET_HEADER_VALUE = "TARGET_HEADER_VALUE";

    @Override protected HttpServletResponse wrapResponse (HttpServletResponse origRes) throws ServletException {
        return new HttpServletResponseWrapper(origRes) {
            @Override public void addHeader (String name, String value) {
                if (name!=null && name.equalsIgnoreCase(getFilterConfig().getInitParameter(TARGET_HEADER_NAME))) super.addHeader(getFilterConfig().getInitParameter(TARGET_HEADER_NAME), getFilterConfig().getInitParameter(TARGET_HEADER_VALUE));
                else super.addHeader(name, value);}
            @Override public void setHeader (String name, String value) {
                if (name!=null && name.equalsIgnoreCase(getFilterConfig().getInitParameter(TARGET_HEADER_NAME))) super.setHeader(getFilterConfig().getInitParameter(TARGET_HEADER_NAME), getFilterConfig().getInitParameter(TARGET_HEADER_VALUE));
                else super.setHeader(name, value);}};}

    @Override protected void doFilter (HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if ((req.getHeader(getFilterConfig().getInitParameter(TRIGGER_HEADER))+"").matches(getFilterConfig().getInitParameter(TRIGGER_HEADER_REGEXP)+"")) chain.doFilter(req, wrapResponse(res));
        else chain.doFilter(req, res);}}

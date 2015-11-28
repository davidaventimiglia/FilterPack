package org.neptunestation.filterpack.filters;

import javax.servlet.*;

public class XSLTStyleSheetInjectorFilter extends XMLTransformFilter {
    public static String XSL_URL = "XSL_URL";

    private String xsl = null;

    @Override public String toString () {
        return xsl;}

    @Override public void init (FilterConfig filterConfig) {
        super.init(filterConfig);
        log(String.format(String.format("xslpath: %s/%s", getFilterConfig().getServletContext().getContextPath(), getFilterConfig().getInitParameter(XSL_URL))));
        xsl = String.format("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" + 
                            "<xsl:template match=\"/\">\n" + 
                            "<xsl:processing-instruction name=\"xml-stylesheet\">\n" + 
                            "<xsl:text>type=\"text/xsl\" href=\"%s\"</xsl:text>\n" + 
                            "</xsl:processing-instruction>\n" + 
                            "<xsl:copy-of select=\"*\"/>\n" + 
                            "</xsl:template>\n" + 
                            "</xsl:stylesheet>",
			    String.format("%s/%s", getFilterConfig().getServletContext().getContextPath(), getFilterConfig().getInitParameter(XSL_URL)));}

    protected String getXSL () {
        return xsl;}}

package org.neptunestation.filterpack.filters;

import javax.servlet.*;

public class XSLTStyleSheetInjectorFilter extends XMLTransformFilter {
    public static String XSL_URL = "XSL_URL";

    private String xsl = null;

    @Override public void init (FilterConfig filterConfig) {
        super.init(filterConfig);
        xsl = String.format("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" + 
                            "<xsl:template match=\"/\">" + 
                            "<xsl:processing-instruction name=\"xml-stylesheet\">" + 
                            "<xsl:text>type=\"text/xsl\" href=\"%s\"</xsl:text>" + 
                            "</xsl:processing-instruction>" + 
                            "<xsl:copy-of select=\"*\"/>" + 
                            "</xsl:template>" + 
                            "</xsl:stylesheet>", getFilterConfig().getInitParameter(XSL_URL));}

    @Override protected String getXSL () {
        return xsl;}}

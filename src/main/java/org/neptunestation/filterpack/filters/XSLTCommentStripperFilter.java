package org.neptunestation.filterpack.filters;

import javax.servlet.*;

public class XSLTCommentStripperFilter extends XMLTransformFilter {
    public static String XSL_URL = "XSL_URL";

    private String xsl = null;

    @Override public void init (FilterConfig filterConfig) {
        super.init(filterConfig);
        log(String.format(String.format("xslpath: %s/%s", getFilterConfig().getServletContext().getContextPath(), getFilterConfig().getInitParameter(XSL_URL))));
        xsl = String.format("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" +
			    "  <xsl:template match=\"node()|@*\">" +
			    "    <xsl:copy>" +
			    "      <xsl:apply-templates select=\"node()|@*\"/>" +
			    "    </xsl:copy>" +
			    "  </xsl:template>" +
			    "  <xsl:template match=\"comment()\"/>" +
			    "</xsl:stylesheet>",
			    String.format("%s/%s", getFilterConfig().getServletContext().getContextPath(), getFilterConfig().getInitParameter(XSL_URL)));}

    @Override protected String getXSL () {
        return xsl;}}

package org.neptunestation.filterpack.api;

import java.io.*;
import javax.servlet.*;

public class ComposableServletOutputStream extends ServletOutputStream {
    protected OutputStream nestedStream = null;

    public ComposableServletOutputStream (OutputStream stream) {
        super();
        if (stream==null) throw new IllegalArgumentException("OutputStream is required.");
        this.nestedStream = stream;}

    @Override public void close () throws IOException {
        nestedStream.close();
        super.close();}

    @Override public void flush () throws IOException {
        nestedStream.flush();}

    @Override public void write (byte[] b) throws IOException {
        nestedStream.write(b);}

    @Override public void write (byte[] b, int off, int len) throws IOException {
        nestedStream.write(b, off, len);}

    @Override public void write (int b) throws IOException {
        nestedStream.write(b);}}


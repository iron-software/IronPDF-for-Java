package com.ironsoftware.ironpdf.internal.staticapi;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;

class DownloadInputStream extends InputStream {

    private InputStream in;
    private int length, sumRead;
    private double percent, previousPercent;
    Logger logger;

    public DownloadInputStream(InputStream inputStream, int length, Logger logger) throws IOException {
        this.in = inputStream;
        sumRead = 0;
        previousPercent = -10;
        this.length = length;
        this.logger = logger;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int readCount = in.read(b);
        evaluatePercent(readCount);
        return readCount;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int readCount = in.read(b, off, len);
        evaluatePercent(readCount);
        return readCount;
    }

    @Override
    public long skip(long n) throws IOException {
        long skip = in.skip(n);
        evaluatePercent(skip);
        return skip;
    }

    @Override
    public int read() throws IOException {
        int read = in.read();
        if (read != -1) {
            evaluatePercent(1);
        }
        return read;
    }

    private void evaluatePercent(long readCount) {
        if (readCount != -1) {
            sumRead += readCount;
            percent = sumRead * 0.01 / length;

            if (percent > previousPercent + 10) {
                previousPercent = percent;
                if (logger.isInfoEnabled())
                    logger.info("Downloading.. " + (int) percent + "%");
                else
                    System.out.println("Downloading.. " + (int) percent + "%");
            }
        } else {
            if (logger.isInfoEnabled())
                logger.info("Download finished");
            else
                System.out.println("Download finished");
        }
    }
}
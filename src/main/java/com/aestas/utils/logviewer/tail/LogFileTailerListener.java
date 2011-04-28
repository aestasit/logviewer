package com.aestas.utils.logviewer.tail;

/**
 * Provides listener notification methods when a tailed log file is updated
 */
public interface LogFileTailerListener {
    /**
     * A new line has been added to the tailed log file
     *
     * @param line The new line that has been added to the tailed log file
     */
    public void newLogFileLine(String line);
}
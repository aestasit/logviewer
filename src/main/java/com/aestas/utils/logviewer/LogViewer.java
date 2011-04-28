package com.aestas.utils.logviewer;

import com.aestas.utils.logviewer.tail.LogFileTailer;

import java.io.*;


/**
 * @author satiri
 */
public class LogViewer {

    private String logfilelocation;
    private String hasil = "<pre>";
    private LogFileTailer tailer;

    /**
     * Creates a new instance of LogViewer
     */
    public LogViewer(String logfile) {
        logfilelocation = logfile;
    }

    public LogViewer() {
    }

    public String getLog(String logfile) {

        String row = "";

        tailer = new LogFileTailer(new File(logfile), 1000, false);
        tailer.start();

        hasil += "</pre>";
        return hasil;
    }

    public String getLog() {

        String row = "";
        int c = 0, c2 = 0;

        if (logfilelocation.length() > 1) {

            try {

                //hitung
                BufferedReader bf = new BufferedReader(new FileReader(logfilelocation));
                //hitung dulu;
                while ((row = bf.readLine()) != null) {
                    c++;
                }
                c = c - 100;

                BufferedReader bx = new BufferedReader(new FileReader(logfilelocation));
                //hitung dulu;
                while ((row = bx.readLine()) != null) {
                    if (c2 >= c) {
                        hasil += row + "\n";
                    }
                    c2++;
                }


            } catch (FileNotFoundException fnfe) {
                hasil += fnfe.getMessage();
            } catch (IOException ioe) {
                hasil += ioe.getMessage();
            }

            hasil += "</pre>";
            return hasil;

        } else {
            hasil += "GAGAL : Lokasi Log belum di set \n";
            hasil += "</pre>";
            return hasil;
        }
    }

    public String getLog(boolean unixcommand, String logfile) {
        String row = "";

        if (unixcommand == true) {
            try {
                String cmd = "tail -n15 " + logfile;
                Process myProcess = Runtime.getRuntime().exec(cmd);

                BufferedReader reader = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));
                while ((row = reader.readLine()) != null) {
                    hasil = hasil + row + "\n";
                }

            } catch (IOException ioex) {
                hasil = ioex.getMessage();
            }
        }

        hasil += "</pre>";
        return hasil;
    }

}

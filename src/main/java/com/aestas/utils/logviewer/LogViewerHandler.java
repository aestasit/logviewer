package com.aestas.utils.logviewer;

import com.aestas.utils.logviewer.tail.LogFileTailer;
import com.aestas.utils.logviewer.tail.LogFileTailerListener;
import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.Broadcaster;
import org.json.simple.JSONValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: lfi
 */
public class LogViewerHandler implements AtmosphereHandler<HttpServletRequest, HttpServletResponse>, LogFileTailerListener {

    private final static String FILE_TO_WATCH = "/var/log/";
    //private final static String FILE_TO_WATCH = "d://temp";
    private static LogFileTailer tailer;
    private Broadcaster GLOBAL_BROADCASTER = null;

    //private Map<String, Broadcaster> brs = new HashMap<String, Broadcaster>();

    private static List<String> watchableLogs = new ArrayList<String>();

    public LogViewerHandler() {
        final File logsDir = new File(FILE_TO_WATCH);
        if (logsDir.exists() && logsDir.isDirectory()) {
            File[] logs = logsDir.listFiles();
            System.out.println("got files: " + logs.length);
            for (File f : logs) {
                if (f.getName().endsWith(".log")) {
                    watchableLogs.add(f.getName());
                }
            }
        } else {
            System.out.println("either logsDir doesn't exist or is not a folder");
        }

    }

    @Override
    public void onRequest(final AtmosphereResource<HttpServletRequest, HttpServletResponse> event) throws IOException {

        HttpServletRequest req = event.getRequest();
        HttpServletResponse res = event.getResponse();
        res.setContentType("text/html");
        res.addHeader("Cache-Control", "private");
        res.addHeader("Pragma", "no-cache");

        if (req.getMethod().equalsIgnoreCase("GET")) {

            event.suspend();
//            if (!brs.containsKey(event.getBroadcaster().getID()))
//                brs.put(event.getBroadcaster().getID(), event.getBroadcaster());
            if (GLOBAL_BROADCASTER == null) GLOBAL_BROADCASTER = event.getBroadcaster();

            if (watchableLogs.size() != 0) {
                GLOBAL_BROADCASTER.broadcast(asJsonArray("logs", watchableLogs));
            }

            res.getWriter().flush();
        } else { // POST

            // Very lame... req.getParameterValues("log")[0] doesn't work
            final String postPayload = req.getReader().readLine();
            if (postPayload != null && postPayload.startsWith("log=")) {
                tailer = new LogFileTailer(new File(FILE_TO_WATCH + "//" + postPayload.split("=")[1]), 100, true);
                tailer.start();
                tailer.addLogFileTailerListener(this);
            }
            GLOBAL_BROADCASTER.broadcast(asJson("filename", postPayload.split("=")[1]));
            res.getWriter().flush();
        }
    }

    @Override
    public void onStateChange(
            final AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) throws IOException {

        HttpServletResponse res = event.getResource().getResponse();
        if (event.isResuming()) {
            res.getWriter().write("Atmosphere closed<br/>");
            res.getWriter().write("</body></html>");
        } else {
            res.getWriter().write(event.getMessage().toString());
        }
        res.getWriter().flush();
    }

    private static List<String> buffer = new ArrayList<String>();

    private final Object o = new Object();

    @Override
    public void destroy() {

    }

    @Override
    public void newLogFileLine(final String line) {
        synchronized (o) { // I guess this is a total over kill...

            buffer.add(line);
            if (buffer.size() == 10) {
                GLOBAL_BROADCASTER.broadcast(asJsonArray("tail", buffer));
                buffer.clear();
            }
        }
        //System.out.println(line);
    }

    protected String asJson(final String key, final String value) {
        return "{\"" + key + "\":\"" + value + "\"}";
    }

    protected String asJsonArray(final String key, final List<String> list) {

        return ("{\"" + key + "\":" + JSONValue.toJSONString(list) + "}");
    }

}

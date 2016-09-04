package br.com.sysprod.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Cristiano Bombazar
 */
public class Log {

    private static final String data = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    private static final String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
    private static final String path = System.getProperty("user.dir");
    private static final File   file = new File(path + "/log" + data + ".txt");

    public static void err(String msg) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            } 
             writeLog(msg);
        } catch (IOException e) {
            ErrorVerification.errFile(e);
        }
    }

    private static void writeLog(String msg) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        String separator = System.getProperty("line.separator");
        bw.write(hora+" : "+ msg + separator);
        bw.flush();
        bw.close();
    }
}

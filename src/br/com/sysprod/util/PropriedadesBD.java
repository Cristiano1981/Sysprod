package br.com.sysprod.util;

import br.com.sysprod.vo.BD;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.Inet4Address;
import java.util.Properties;

/**
 *
 * @author Cristiano Bombazar
 */
public class PropriedadesBD {
    
    public BD readyPropertiesBD() {
        String path = System.getProperty("user.dir");
        File pathProperties = new File(path + "/bd.properties");
        FileInputStream file = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        Properties prop = null;
        BD cfgBD = null;
        String separator = System.getProperty("line.separator");
        try {
            if (!pathProperties.exists()) {
                //cria arquivo
                pathProperties.createNewFile();
                //grava propriedades default
                StringBuilder str = new StringBuilder();
                str.append("[Configuração BD]").append(separator);
                str.append("host     = ").append(Inet4Address.getLocalHost().getHostAddress()).append(separator);
                str.append("database = sysprod").append(separator);
                str.append("porta    = 5432").append(separator);
                str.append("user     = postgres").append(separator);
                str.append("password = postgres").append(separator);
                fw = new FileWriter(pathProperties);
                bw = new BufferedWriter(fw);
                bw.write(str.toString());
                bw.flush();
                //le arquivo de configuração para não dar nullpointer
                file = new FileInputStream(pathProperties);
                prop = new Properties();
                prop.load(file);
                cfgBD = getProperties(prop);
            } else {
                //carregando arquivo para buscar informações
                file = new FileInputStream(pathProperties);
                prop = new Properties();
                prop.load(file);
                //seta configuração buscando do arquivo bd.properties
                cfgBD = getProperties(prop);
            }
        } catch (Exception e) {
            ErrorVerification.errFile(e);
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (Exception e) {
                ErrorVerification.errFile(e);
            }
        }
        return cfgBD;
    }

    private BD getProperties(Properties prop) {
        BD cfgBD = null;
        if (prop != null && !prop.isEmpty()  ) {
            cfgBD = new BD();
            cfgBD.setHost(prop.getProperty("host"));
            cfgBD.setDatabase(prop.getProperty("database"));
            cfgBD.setPorta(prop.getProperty("porta"));
            cfgBD.setUser(prop.getProperty("user"));
            cfgBD.setPassword(prop.getProperty("password"));
        }
        return cfgBD;
    }
    
}

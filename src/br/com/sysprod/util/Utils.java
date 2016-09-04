package br.com.sysprod.util;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * @author Cristiano Bombazar
 */
public class Utils {
 
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat sdfBD = new SimpleDateFormat("yyyy-MM-dd");
    public static final String EXTENSION_TXT = ".txt";
    public static final String EXTENSION_PROPERTIES = ".properties";
    public static final String EXTENSION_LOG = ".log";
    
    public static java.sql.Date converteDateBD(Date date){
        return new java.sql.Date(date.getTime());
    }
    
    public static final String codigoFormatado(int codigo) {
        String aux = "";
       // if (codigo != 0) {
            aux = Integer.toString(codigo);
            aux = String.format("%06d", codigo);
        //}
        return aux;
    }
    
    /**
     * Recebe uma String e converte para Date.
     *
     * @param data Recebe uma String de data no formato dd/mm/yyyy (Voc√™ pode
     * alterar o pattern).
     * @return Date - Retorna um objeto do tipo java.util.Date
     */
    public static Date converteDate(String data) {
        Date d = new Date();
        try {
            d = sdf.parse(data);
        } catch (Exception e) {
        }
        return d;
    }

    /**
     * @param date Recebe um Date do tipo java.util.Date
     * @throws IllegalArgumentException
     * @return String - Retorna uma data formatada. Retorna um objeto do tipo String. 
     * Formato: dd/MM/yyyy
     */
    public static final String converteDateParaString(Date date) {
        return sdf.format(date);
    }
    
    /**
     * @throws IllegalArgumentException
     * @return String - Retorna uma data formatada. Retorna um objeto do tipo String para banco de dados. 
     * Formato: yyyy-MM-dd.
     */
    public static final String converteDateParaStringBD(String date) {
        return sdfBD.format(converteDate(date));
    }

    /**
     * Recebe um array de component e verifica se o campo est√° vazio ou n√£o
     * selecionado. (JtextField e JComboBox)
     *
     * @param Component
     * @return boolean
     */
    public static final boolean validaObrigatorios(Component[] t) {
        for (int i = 0; i < t.length; i++) {
            if (t[i] instanceof JTextField) {
                JTextField text = (JTextField) t[i];
                if (text.getText().trim().isEmpty() || text.getText().equals("  /  /    ")
                        || text.getText().equals("000000") || text.getText().equals("   .   .   -  ")
                        || text.getText().equals("(  )     -    ") || text.getText().equals("     -   ")) {

                    text.requestFocus();
                    text.setToolTipText("Campo ObrigatÛrio. Favor, preencher");
                    text.setBorder(new LineBorder(Color.RED));
                    return false;
                } else {
                    text.setBorder(new LineBorder(Color.LIGHT_GRAY));
                }
            } else if (t[i] instanceof JComboBox) {
                JComboBox combo = (JComboBox) t[i];
                if (combo.getSelectedIndex() == 0) {
                    combo.requestFocus();
                    combo.setToolTipText("Campo ObrigatÛrio. Favor, selecionar uma das opÁıes");
                    combo.setBorder(new LineBorder(Color.RED));
                    return false;
                } else {
                    combo.setBorder(new LineBorder(Color.LIGHT_GRAY));
                }
            }
        }
        return true;
    }

    /**
     * @param data
     * @return
     * @throws ParseException
     */
    public static final String formataData(String data) {
        if (data.replaceAll("/", "").length() > 8) {
            throw new IllegalArgumentException("Insira uma data v·lida. Formato: dd/MM/yyyy");
        }
        sdf.setLenient(false);
        Calendar c = Calendar.getInstance();
        Date dd = new Date();
        String ano = Integer.toString(c.get(Calendar.YEAR));
        String mes = Integer.toString(c.get(Calendar.MONTH) + 1);
        String d = data.replaceAll("/", "");

        try {
            if (d.length() <= 2) {
                d = d.concat("/").concat(mes).concat("/").concat(ano);
                dd = sdf.parse(d);
                d = sdf.format(dd);
                return d;

            } else if (d.length() >= 2 && d.length() <= 4) {
                String dia_aux = d.substring(0, 2);
                String mes_aux = d.substring(2, d.length());
                if (d.length() == 3 && d.substring(2, 3).equals("0")){
                    d = dia_aux.concat("/1").concat(mes_aux).concat("/").concat(ano);
                }else{
                    d = dia_aux.concat("/").concat(mes_aux).concat("/").concat(ano);
                }
                dd = sdf.parse(d);
                d = sdf.format(dd);
                return d;
            } else if (d.length() >= 4 && d.length() <= 8) {
                String dia = d.substring(0, 2);
                String mes_aux = d.substring(2, 4);
                String ano_aux = d.substring(4, d.length());

                if (ano_aux.length() == 1) {
                    d = dia.concat("/").concat(mes_aux).concat("/").concat("200").concat(ano_aux);
                    dd = sdf.parse(d);
                    d = sdf.format(dd);
                    return d;
                } else if (ano_aux.length() == 2) {
                    d = dia.concat("/").concat(mes_aux).concat("/").concat("20").concat(ano_aux);
                    dd = sdf.parse(d);
                    d = sdf.format(dd);
                    return d;
                } else {
                    d = dia.concat("/").concat(mes_aux).concat("/").concat(ano_aux);
                    dd = sdf.parse(d);
                    d = sdf.format(dd);
                    return d;
                }
            }
        } catch (Exception e) {
            return "";
        }
        return d;
    }

    public static void clearAll(Component[] comp) {
        for (Component c : comp) {
            if (c instanceof JTextField) {
                ((JTextField) c).setText("");
            } else if (c instanceof JFormattedTextField) {
                ((JFormattedTextField) c).setText("");
            } else if (c instanceof JComboBox) {
                ((JComboBox) c).setSelectedIndex(0);
            } else if (c instanceof JCheckBox) {
                ((JCheckBox) c).setSelected(false);
            } else if (c instanceof JRadioButton) {
                ((JRadioButton) c).setSelected(false);
            } else if (c instanceof JPanel) {
                Component[] jp = ((JPanel) c).getComponents();
                clearAll(jp);
            } else if (c instanceof JRootPane) {
                Component[] jp = ((JRootPane) c).getComponents();
                clearAll(jp);
            } else if (c instanceof JDialog) {
                Component[] jp = ((JDialog) c).getComponents();
                clearAll(jp);
            } else if (c instanceof JInternalFrame) {
                Component[] jp = ((JInternalFrame) c).getComponents();
                clearAll(jp);
            } else if (c instanceof JLabel) {
                continue;
            } else if (c instanceof JButton) {
                continue;
            } else if (c instanceof JLayeredPane) {
                Component[] jp = ((JLayeredPane) c).getComponents();
                clearAll(jp);
            }
        }
    }

    /**
     * @param @param palavra Recebe a palavra a ser completada
     * @param separador Recebe o que vai ser preenchido na palavra
     * @param qtd o tamanho total que deve retornar a String
     * @param lado Qual lado deve ser preenchido a String. 1 - Direito , 2 -
     * Esquerdo, 3 - Centralizado
     * @return String
     */
    public static String preencherString(String palavra, String separador, int qtd, int lado) {
        int original = qtd;
        qtd = qtd - palavra.length();

        if (!palavra.trim().equals("")) {
            if (!separador.equals("")) {
                if (lado == 1) { //lado direito
                    for (int i = 0; i < qtd; i++) {
                        palavra = palavra.concat(separador);
                    }
                } else if (lado == 2) { //lado esquerdo
                    for (int i = 0; i < qtd; i++) {
                        palavra = separador.concat(palavra);
                    }
                } else if (lado == 3) { //centralizado
                    int aux = (Integer) qtd / 2;
                    for (int i = 0; i < aux; i++) {
                        palavra = palavra.concat(separador);
                    }
                    for (int i = 0; i < aux; i++) {
                        palavra = separador.concat(palavra);
                    }

                    if (palavra.length() < original) {
                        palavra = palavra.concat(separador);
                    }
                } else {
                    throw new IllegalArgumentException("Par√¢metro Inv√°lido. Par√¢metro lado deve ser prenchido da seguinte Maneira: \n 1 - Direito , 2 - Esquerdo, 3 - Centralizado");
                }
            }
        }
        return palavra;
    }

    private static String calcDigVerif(String num) {
        Integer primDig, segDig;
        int soma = 0, peso = 10;
        for (int i = 0; i < num.length(); i++) {
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
        }

        if (soma % 11 == 0 | soma % 11 == 1) {
            primDig = new Integer(0);
        } else {
            primDig = new Integer(11 - (soma % 11));
        }

        soma = 0;
        peso = 11;
        for (int i = 0; i < num.length(); i++) {
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
        }

        soma += primDig * 2;
        if (soma % 11 == 0 | soma % 11 == 1) {
            segDig = 0;
        } else {
            segDig = 11 - (soma % 11);
        }

        return primDig.toString() + segDig.toString();
    }

    private static int calcSegDig(String cpf, int primDig) {
        int soma = 0, peso = 11;
        for (int i = 0; i < cpf.length(); i++) {
            soma += Integer.parseInt(cpf.substring(i, i + 1)) * peso--;
        }

        soma += primDig * 2;
        if (soma % 11 == 0 | soma % 11 == 1) {
            return 0;
        } else {
            return 11 - (soma % 11);
        }
    }

    public static String geraCPF() {
        String iniciais = "";
        Integer numero;
        for (int i = 0; i < 9; i++) {
            numero = new Integer((int) (Math.random() * 10));
            iniciais += numero.toString();
        }
        return iniciais + calcDigVerif(iniciais);
    }

    public static boolean validaCPF(String cpf) {
        if (cpf.length() != 11) {
            return false;
        }
        String numDig = cpf.substring(0, 9);
        return calcDigVerif(numDig).equals(cpf.substring(9, 11));
    }
    
    public static String removeCaracteresEspeciais(String texto){
        return texto.replaceAll("[-]+", "");
    }
    
    public static void saveFile(String text) throws IOException{
        File file = new File("Erros Gabarito.txt");
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text);
        bw.flush();
        bw.close();
    }
    
    public static void saveFile(String text, String extension) throws IOException{
        File file = new File("Erros Gabarito."+extension);
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text);
        bw.flush();
        bw.close();
    }
    
    public static void saveFile(String text, String path, String extension) throws IOException{
        File file = new File(path+"/Erros Gabarito"+extension);
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text);
        bw.flush();
        bw.close();
    }
    
    public static void saveFile(String text, String path,String name, String extension) throws IOException{
        File file = new File(path+"/"+name+extension);
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text);
        bw.flush();
        bw.close();
    }
}

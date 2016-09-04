package br.com.sysprod.browse;

import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.dao.AcademicoBD;
import br.com.sysprod.dao.CursoBD;
import br.com.sysprod.dao.ProvaBD;
import br.com.sysprod.dao.ProvaDetAcademicoBD;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Academico;
import br.com.sysprod.vo.Curso;
import br.com.sysprod.vo.Prova;
import br.com.sysprod.vo.ProvaDet;
import br.com.sysprod.vo.ProvaDetAcademico;
import br.com.sysprod.vo.Turma;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

/**
 * @author Cristiano Bombazar
 */
public final class BrowseImportaGabarito extends javax.swing.JInternalFrame {

    private Map<Integer, Turma> mapaTurmas = null;
    private Map<Turma, Prova> mapaProvas = null;
    private List<ProvaDetAcademico> listaProvaDetAcademico = null;

    public BrowseImportaGabarito() {
        initComponents();
        initComponent();
        initForm();
    }

    private void initComponent() {
        btEscolheArquivo.setToolTipText("Clique para abrir janela de pesquisa do arquivo para importar");
        btImportar.setToolTipText("Faz a importação do arquivo");
        btFechar.setToolTipText("Fechar");
        progress.setToolTipText("Barra de progresso");
        area.setToolTipText("Área de texto");
        ((DefaultCaret) area.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    private void initForm() {
        setResizable(false);
    }

    public void posicao() {
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 4);
    }

    private void openChooser() {
        String pathUser = System.getProperty("user.dir");
        JFileChooser fc = new JFileChooser(pathUser);
        fc.setApproveButtonText("Importar");
        fc.setFileFilter(new FileNameExtensionFilter("Somente arquivo txt", "txt"));
        int result = fc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            tfPath.setText(fc.getSelectedFile().getAbsolutePath());
        } else {
            tfPath.setText("");
        }
    }

    private boolean confereResposta(String resposta) {
        String respostas = "ABCDE_";
        boolean flag = false;
        char[] chain = respostas.toCharArray();
        for (char c : chain) {
            String aux = String.valueOf(c);
            if (aux.equals(resposta)) {
                flag = true;
            }
        }
        return flag;
    }

    private void imprimeProva() {
        Turma t = mapaTurmas.get(4);
        if (t != null) {
            Prova p = mapaProvas.get(t);
            if (p != null) {
                int cc = 0;
                for (ProvaDet det : p.getListaProvaDet()) {
                    for (ProvaDetAcademico acad : det.getListaProvaDetAcademico()) {
                        System.out.println("Detalhe: " + cc);
                        System.out.println(acad.getAcademico().getCodigo() + " - " + acad.getAcademico().getNome());
                        System.out.println(acad.getAssinalado());
                        System.out.println("--------------------------------------------------");
                    }
                    cc++;
                }
            } else {
                System.out.println("Prova é null");
            }
        } else {
            System.out.println("Turma é null");
        }
    }

    private void zeraAcademicosProva(Prova p) {
        for (ProvaDet det : p.getListaProvaDet()) {
            if (det.getListaProvaDetAcademico() != null) {
                det.getListaProvaDetAcademico().clear();
            }
        }
    }

    private boolean isEmpty(String[] text) {
        for (int i = 0; i < text.length; i++) {
            if (text[i].trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void abreConsultaTurmas() {
        BrowseConsultaTurma cons = new BrowseConsultaTurma(null, true);
        cons.setVisible(true);
        List<Turma> lista = cons.getTurmaSelection();
        String montaTurma = "";
        for (Turma turma : lista) {
            montaTurma += turma.getCurso().getDescricao() + " " + turma.getFase() + "ª, ";
        }
        tfTurmas.setText(montaTurma);
        if (lista != null && !lista.isEmpty()) {
            if (mapaTurmas == null) {
                mapaTurmas = new HashMap<>();
            }
            mapaTurmas.clear();
            for (Turma turma : lista) {
                mapaTurmas.put(turma.getFase(), turma);
            }
        }
    }

    private boolean validateForm() {
        if (mapaTurmas == null || mapaTurmas.isEmpty()) {
            tfTurmas.setBorder(new LineBorder(Color.RED));
            JOptionPane.showMessageDialog(null, "Selecione pelo menos uma turma!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (tfPath.getText().trim().isEmpty()) {
            tfPath.setBorder(new LineBorder(Color.RED));
            JOptionPane.showMessageDialog(null, "Selecione o arquivo!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    private void buscaProvas() {
        if (mapaProvas == null) {
            mapaProvas = new HashMap<Turma, Prova>();
        }
        Set<Integer> keys = mapaTurmas.keySet();
        ProvaBD provaBD = new ProvaBD();
        for (Integer key : keys) {
            Turma turma = mapaTurmas.get(key);
            Prova prova = null;
            try {
                prova = provaBD.selectByIDTurma(turma.getId());
            } catch (SQLException ex) {
                ErrorVerification.ErrDetalhe(ex, "Erro ao buscar prova da " + turma.getFase() + "ª fase");
            }
            if (prova != null) {
                zeraAcademicosProva(prova);
                mapaProvas.put(turma, prova);
            } else {
                //COLOCAR LOG DE ERRO AQUI. "PROVA NÃO ENCONTRADA".
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfPath = new javax.swing.JTextField();
        btEscolheArquivo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        area = new javax.swing.JTextArea();
        btFechar = new javax.swing.JButton();
        btImportar = new javax.swing.JButton();
        progress = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        btBusca = new javax.swing.JButton();
        tfTurmas = new javax.swing.JTextField();

        setTitle("Importação de Provas");

        jLabel1.setText("Arquivo:");

        tfPath.setEditable(false);
        tfPath.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfPath.setText("C:\\Users\\User\\OneDrive\\TCC\\Arquivos sistema\\Tabela de gabarito - Orleans.txt");

        btEscolheArquivo.setText("Escolher Arquivo");
        btEscolheArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEscolheArquivoActionPerformed(evt);
            }
        });

        area.setEditable(false);
        area.setColumns(20);
        area.setRows(5);
        jScrollPane1.setViewportView(area);

        btFechar.setText("Fechar");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        btImportar.setText("Importar");
        btImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btImportarActionPerformed(evt);
            }
        });

        jLabel2.setText("Turmas:");

        btBusca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaActionPerformed(evt);
            }
        });

        tfTurmas.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btImportar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btFechar))
                    .addComponent(progress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfTurmas)
                            .addComponent(tfPath, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btEscolheArquivo))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btEscolheArquivo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfTurmas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btImportar)
                    .addComponent(btFechar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btEscolheArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEscolheArquivoActionPerformed
        openChooser();
    }//GEN-LAST:event_btEscolheArquivoActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        dispose();
    }//GEN-LAST:event_btFecharActionPerformed

    private void btImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btImportarActionPerformed
        if (validateForm()) {
            buscaProvas();
            ImportaProva impProva = new ImportaProva();
            Thread t = new Thread(impProva);
            t.setPriority(Thread.MAX_PRIORITY);
            t.setDaemon(true);
            t.start();
        }
    }//GEN-LAST:event_btImportarActionPerformed

    private void btBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaActionPerformed
        abreConsultaTurmas();
    }//GEN-LAST:event_btBuscaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea area;
    private javax.swing.JButton btBusca;
    private javax.swing.JButton btEscolheArquivo;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btImportar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JProgressBar progress;
    private javax.swing.JTextField tfPath;
    private javax.swing.JTextField tfTurmas;
    // End of variables declaration//GEN-END:variables

    private class ImportaProva implements Runnable {

        public ImportaProva() {
        }

        @Override
        public void run() {
            importar();
        }

        private void importar() {
            progress.setBorderPainted(true);
            getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            area.setText("");
            area.append("Iniciando importação...\n");
            String path = tfPath.getText().trim();
            try {
                String error = readyFile(path);
                Set<Integer> set = mapaTurmas.keySet();
                for (Integer key : set) {
                    Turma turma = mapaTurmas.get(key);
                    Prova prova = mapaProvas.get(turma);
                    if (new ProvaBD().saveOrUpdate(prova)) {
                        getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        if (error != null){
                            area.append("O arquivo foi importado com algumas inconsistências.");
                            Integer result = JOptionPane.showConfirmDialog(null, "O arquivo foi importado algumas inconsistências. Deseja emitir relatório de erro?", "Atenção", JOptionPane.YES_NO_OPTION);
                            if (result == JOptionPane.YES_OPTION){
                                Utils.saveFile(error);
                                Desktop.getDesktop().edit(new File("Erros Gabarito.txt"));
                            }
                        }
                    }
                }
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
                area.append("Não foi possível fazer a importação do arquivo!");
                ErrorVerification.ErrDetalhe(ex, "Erro ao importar arquivo");
            }
            getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        private String readyFile(String path) throws IOException, SQLException {
            File              file         = new File(path);
            FileReader        fr           = new FileReader(file);
            BufferedReader    br           = new BufferedReader(fr);
            Academico         academico    = null;
            ProvaDetAcademico provaDetAcad = null;
            Integer           count        = 0;
            StringBuilder     error        = null;
            String            line         = null;
            String[]          text         = null;
            String            separator    = System.getProperty("line.separator");
            area.append("Fazendo leitura do arquivo..\n");
            while (br.ready()) {
                count++;
                line = br.readLine();
                if (line.isEmpty()){
                    continue;
                }
                text = line.split(";");
                if (isEmpty(text)) {
                    if (error == null) {
                        error = new StringBuilder("Foram encontrados as seguintes inconsistências no arquivo: "+separator);
                    }
                    error.append("Linha do erro:    " + count).append(". Verifique se nenhum dos campos está vazio. "+separator);
                    error.append("Detalhe da linha: " + line).append(separator);
                } else {
                    //IDENTIFICA FASE, ALUNO, LÊ AS RESPOSTA DO ALUNO.
                    area.append("Lendo registros e validando-os separadamente..."+separator);
                    Integer  fase      = Integer.parseInt(text[0]);
                    Integer  aluno     = Integer.parseInt(text[1]);
                    String[] respostas = new String[25];
                    fillRespostas(respostas, text);
                    academico = new AcademicoBD().findByCodigo(aluno);
                    if (academico == null) {
                        if (error == null) {
                            error = new StringBuilder("Foram encontrados as seguintes inconsistências no arquivo: "+separator);
                        }
                        error.append("Acadêmico não encontrado. Código do acadêmico: " + aluno + ". Linha: " + count + separator);
                        error.append("Detalhe da linha " + line+ separator);
                        continue;
                    }
                    //fim

                    //RECUPERA PROVA E INFORMAÇÕES DA MESMA.
                    Turma turma = mapaTurmas.get(fase);
                    if (turma != null) {
                        Prova prova = mapaProvas.get(turma);
                        if (prova != null) {
                            area.append("Buscando informações da prova "+Utils.codigoFormatado(prova.getId())+"..."+separator);
                            //deleta todos os academicos vinculados aquela prova para importar de novo. Se der erro, volta 
                            Connection con = ConnectionManager.getConnection();
                            con.setAutoCommit(true);
                            ProvaDetAcademicoBD acadBD = new ProvaDetAcademicoBD(con);
                            acadBD.deleteByProva(prova.getId());
                            //fim

                            int cc = 0; //apenas pra pegar das respostas.
                            area.append("Lendo perguntas e inserindo respostas... "+separator);
                            for (ProvaDet det : prova.getListaProvaDet()) {
                                if (det.getListaProvaDetAcademico() == null) {
                                    det.setListaProvaDetAcademico(new ArrayList<ProvaDetAcademico>());
                                }
                                provaDetAcad = new ProvaDetAcademico();
                                provaDetAcad.setAcademico(academico);
                                if (confereResposta(respostas[cc].toUpperCase())) {
                                    if (respostas[cc].toUpperCase().equals("_")) {
                                        respostas[cc] = "N";
                                    }
                                    provaDetAcad.setAssinalado(respostas[cc].toUpperCase());
//                                if (det.getListaProvaDetAcademico().contains(provaDetAcad)){
//                                    det.getListaProvaDetAcademico().remove(provaDetAcad);
//                                    if (det.getListaProvaDetAcademicoExcluir() == null){
//                                        det.setListaProvaDetAcademicoExcluir(new ArrayList<ProvaDetAcademico>());
//                                    }
//                                    det.getListaProvaDetAcademicoExcluir().add(provaDetAcad);
//                                }
                                    det.getListaProvaDetAcademico().add(provaDetAcad);
                                    cc++;
                                } else {
                                    if (error == null) {
                                        error = new StringBuilder("Foram encontrados as seguintes inconsistências no arquivo: "+separator);
                                    }
                                    error.append("Conteúdo da resposta do acadêmico inválida. Verificar resposta de número " + cc + " na linha " + count +separator);
                                    error.append("Conteúdo: " + respostas[cc] + ". Valores Válidos: A-B-C-D-E-_ "+separator);
                                }
                            }
                            area.append("Atualizando dados da prova "+Utils.codigoFormatado(prova.getId())+"..."+separator);
                            mapaProvas.put(turma, prova);
                        }
                    }
                }
            }
            br.close();
            fr.close();
            return error.toString();
        }

        private void fillRespostas(String[] respostas, String text[]) {
            int contResposta = 0;
            for (int i = 3; i < text.length; i++) {
                respostas[contResposta] = text[i].trim();
                contResposta++;
            }
        }
    }
}

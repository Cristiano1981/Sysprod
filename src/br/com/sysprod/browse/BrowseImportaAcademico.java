package br.com.sysprod.browse;

import br.com.sysprod.Global;
import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.AcademicoBD;
import br.com.sysprod.dao.CursoBD;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Academico;
import br.com.sysprod.vo.Curso;
import java.awt.Cursor;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

/**
 * @author Cristiano Bombazar
 */
public final class BrowseImportaAcademico extends javax.swing.JInternalFrame {
    
    private Curso curso = null;

    public BrowseImportaAcademico() {
        initComponents();
        initComponent();
        initForm();
    }
    
    private void initComponent(){
        btEscolheArquivo.setToolTipText("Clique para abrir janela de pesquisa do arquivo para importar");
        btImportar.setToolTipText("Faz a importação do arquivo");
        btFechar.setToolTipText("Fechar");
        progress.setToolTipText("Barra de progresso");
        area.setToolTipText("Área de texto");
        tfCodigoCurso.setToolTipText("Código do curso");
        btBusca.setToolTipText("Abre tela de pesquisa de cursos");
        tfNomeCurso.setToolTipText("Descrição do curso");
        tfPath.setToolTipText("Caminho absoluto do arquivo");
        tfCodigoCurso.setInputVerifier(new MyVerify());
        ((DefaultCaret) area.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }
    
     private void initForm() {
        setResizable(false);
    }

    public void posicao() {
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 4);
    }
    
    private void openChooser(){
        String pathUser = System.getProperty("user.dir");
        JFileChooser fc = new JFileChooser(pathUser);
        fc.setApproveButtonText("Importar");
        fc.setFileFilter(new FileNameExtensionFilter("Somente arquivo txt", "txt"));
        int result = fc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION){
            tfPath.setText(fc.getSelectedFile().getAbsolutePath());
        }else{
            tfPath.setText("");
        }
    }
    
    private void importar(){
        progress.setBorderPainted(true);
        getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        area.setText("");
        area.append("Iniciando importação...\n");
        CursoBD         cursoBD         = new CursoBD();
        AcademicoBD     acadBD          = new AcademicoBD();
        List<Academico> listaAcademico  = null;
        String          path            = tfPath.getText().trim();
        Integer         codCurso        = Integer.parseInt(tfCodigoCurso.getText());
        if (path.isEmpty()){
            getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            openChooser();
        }
        if (codCurso == 0){
            area.append("Selecione um curso para fazer a importação. \n");
            tfCodigoCurso.requestFocusInWindow();
            tfCodigoCurso.selectAll();
            return;
        }
        try {
            setCurso(cursoBD.find(codCurso));
            listaAcademico = readyFile(path);
            if (listaAcademico != null && !listaAcademico.isEmpty()){
                progress.setMaximum(listaAcademico.size());
                int progresso = 0;
                area.append("Preparando-se para importar "+listaAcademico.size()+ " acadêmicos. \n");
                for (Academico academico : listaAcademico) {
                    progress.setValue(++progresso);
                    Academico aux = acadBD.findByCodigo(academico.getCodigo());
                    if (aux != null){
                        academico.setId(aux.getId());
                    }
                    if (acadBD.saveOrUpdate(academico)){
                        area.append("Importando e salvando acadêmico: "+academico.getCodigo()+ " - "+academico.getNome()+"\n");
                    }
                }
            }
            area.append("Lista de acadêmicos importados com sucesso.");
            JOptionPane.showMessageDialog(null, "Importação realizada com suceso.", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro na importação");
        }
        getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    private List<Academico> readyFile(String path) throws IOException{
        File            file           = new File(path);
        FileReader      fr             = new FileReader(file);
        BufferedReader  br             = new BufferedReader(fr);
        List<Academico> listaAcademico = null;
        Academico       academico      = null;
        String          line           = null;
        while (br.ready()){
            if (listaAcademico == null){
                listaAcademico = new ArrayList<Academico>();
            }
            line = br.readLine();
            String[] text = line.split(";");
            if (!isEmpty(text)){
                academico = new Academico();
                academico.setCodigo(Integer.parseInt(text[0].trim()));
                academico.setNome(text[1].trim().toUpperCase());
                academico.setAtivo(true);
                academico.setCurso(getCurso());
                academico.setUnidade(Global.getUnidade());
                listaAcademico.add(academico);
            }
        }
        br.close();
        fr.close();
        return listaAcademico;
    }
    
    private boolean isEmpty(String[] text){
        return text[0].trim().isEmpty() || text[1].trim().isEmpty();
    }
    
     private Curso abreConsultaCurso() {
        ConsultaDialog consulta = new ConsultaDialog(null, true);
        consulta.vinculaCurso();
        consulta.initForm();
        consulta.setVisible(true);
        Curso c = consulta.getBrowseCurso().getCurso();
        return c;
    }

    private void fillCurso(Curso c) {
        tfCodigoCurso.setText(Utils.codigoFormatado(c.getId()));
        tfNomeCurso.setText(c.getDescricao());
    }

    private void carregaCurso() {
        Integer codigo = Integer.parseInt(tfCodigoCurso.getText());
        if (codigo > 0) {
            CursoBD curBD = new CursoBD();
            Curso curso = null;
            try {
                curso = curBD.find(codigo);
            } catch (SQLException e) {
                ErrorVerification.ErrDetalhe(e, "Erro ao buscar curso");
            }
            if (curso != null) {
                fillCurso(curso);
            } else {
                Curso c = abreConsultaCurso();
                if (c != null) {
                    fillCurso(c);
                } else {
                    tfCodigoCurso.setText("000000");
                    tfNomeCurso.setText("");
                    tfCodigoCurso.selectAll();
                }
            }
        } else {
            tfCodigoCurso.setText("000000");
            tfNomeCurso.setText("");
            tfCodigoCurso.selectAll();
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
        tfCodigoCurso = new javax.swing.JTextField();
        btBusca = new javax.swing.JButton();
        tfNomeCurso = new javax.swing.JTextField();

        jLabel1.setText("Arquivo:");

        tfPath.setEditable(false);
        tfPath.setHorizontalAlignment(javax.swing.JTextField.CENTER);

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

        jLabel2.setText("Curso:");

        tfCodigoCurso.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigoCurso.setText("000000");
        tfCodigoCurso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfCodigoCursoFocusLost(evt);
            }
        });

        btBusca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaActionPerformed(evt);
            }
        });

        tfNomeCurso.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfPath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btEscolheArquivo))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btImportar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btFechar))
                    .addComponent(progress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(tfCodigoCurso, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNomeCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(tfNomeCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(tfCodigoCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btFechar)
                    .addComponent(btImportar))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                importar();
            }
        });
        
    }//GEN-LAST:event_btImportarActionPerformed

    private void tfCodigoCursoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoCursoFocusLost
        carregaCurso();
    }//GEN-LAST:event_tfCodigoCursoFocusLost

    private void btBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaActionPerformed
        Curso c = abreConsultaCurso();
        if (c != null) {
            fillCurso(c);
        } else {
            tfCodigoCurso.setText("000000");
            tfNomeCurso.setText("");
            tfCodigoCurso.selectAll();
        }
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
    private javax.swing.JTextField tfCodigoCurso;
    private javax.swing.JTextField tfNomeCurso;
    private javax.swing.JTextField tfPath;
    // End of variables declaration//GEN-END:variables

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    private class MyVerify extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            try {
                Integer.parseInt(tfCodigoCurso.getText());
            } catch (Exception e) {
                tfCodigoCurso.setText("000000");
                tfCodigoCurso.selectAll();
                return false;
            }
            return true;
        }
    }
}

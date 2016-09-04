package br.com.sysprod.browse;

import br.com.sysprod.Global;
import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.AcademicoBD;
import br.com.sysprod.dao.CursoBD;
import br.com.sysprod.dao.PeriodoBD;
import br.com.sysprod.dao.TurmaBD;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Academico;
import br.com.sysprod.vo.Curso;
import br.com.sysprod.vo.Periodo;
import br.com.sysprod.vo.Turma;
import br.com.sysprod.vo.TurmaDet;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

/**
 * @author Cristiano Bombazar
 */
public final class BrowseImportaTurma extends javax.swing.JInternalFrame {

    private Curso   curso;
    private Periodo periodo;
    
    public BrowseImportaTurma() {
        initComponents();
        initComponent();
        initForm();
    }
    
     private void initComponent(){
        btEscolheArquivo.setToolTipText("Clique para abrir janela de pesquisa do arquivo para importar");
        btImportar.setToolTipText("Faz a importação do arquivo");
        btFechar.setToolTipText("Fechar");
        area.setToolTipText("Área de texto");
        tfQtdMaximaAcertos.setToolTipText("Quantidade de perguntas que a turma deverá acertar para obter nota máxima.");
        tfCodigoCurso.setToolTipText("Código do curso");
        btBuscaCurso.setToolTipText("Abre tela de pesquisa de cursos");
        tfNomeCurso.setToolTipText("Descrição do curso");
        btBuscaPeriodo.setToolTipText("Abre tela de pesquisa de períodos");
        tfCodigoPeriodo.setToolTipText("Código do período");
        tfDescricaoPeriodo.setToolTipText("Descrição do período");
        tfPath.setToolTipText("Caminho absoluto do arquivo");
        tfCodigoCurso.setInputVerifier(new MyVerify());
        tfCodigoPeriodo.setInputVerifier(new MyVerify());
        tfQtdMaximaAcertos.setInputVerifier(new MyVerify());
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
            if (!fc.getSelectedFile().getAbsolutePath().endsWith("txt")){
                JOptionPane.showMessageDialog(null, "Selecione um arquivo com extensão '.txt' para fazer importação.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                tfPath.setText("");
                openChooser();
            }else{
                tfPath.setText(fc.getSelectedFile().getAbsolutePath());
            }
        }else{
            tfPath.setText("");
        }
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
            } catch (Exception e) {
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
    
    private Periodo abreConsultaPeriodo() {
        ConsultaDialog consulta = new ConsultaDialog(null, true);
        consulta.vinculaPeriodo();
        consulta.initForm();
        consulta.setVisible(true);
        Periodo c = consulta.getBrowsePeriodo().getPeriodo();
        return c;
    }
    
    private void fillPeriodo(Periodo p){
        tfCodigoPeriodo.setText(Utils.codigoFormatado(p.getId()));
        tfDescricaoPeriodo.setText(p.getDescricao());
    }
    
    private void carregaPeriodo() {
        Integer codigo = Integer.parseInt(tfCodigoPeriodo.getText());
        if (codigo > 0) {
            PeriodoBD perBD = new PeriodoBD();
            Periodo periodo = null;
            try {
                periodo = perBD.find(codigo);
            } catch (SQLException e) {
                ErrorVerification.ErrDetalhe(e, "Erro ao buscar período");
            }
            if (periodo != null) {
                fillPeriodo(periodo);
            } else {
                Periodo c = abreConsultaPeriodo();
                if (c != null) {
                    fillPeriodo(c);
                } else {
                    tfCodigoPeriodo.setText("000000");
                    tfDescricaoPeriodo.setText("");
                    tfCodigoPeriodo.selectAll();
                }
            }
        } else {
            tfCodigoPeriodo.setText("000000");
            tfDescricaoPeriodo.setText("");
            tfCodigoPeriodo.selectAll();
        }
    }
    
    private void importar(){
        getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        area.setText("");
        area.append("Iniciando importação...\n");
        CursoBD            cursoBD       = new CursoBD();
        PeriodoBD          periodoBD     = new PeriodoBD();
        TurmaBD            turmaBD       = new TurmaBD();
        Turma              turma         = new Turma();
        List<TurmaDet>     listaTurmaDet = null;
        Integer            codCurso      = Integer.parseInt(tfCodigoCurso.getText());
        Integer            codPeriodo    = Integer.parseInt(tfCodigoPeriodo.getText());
        String             path          = tfPath.getText().trim();
        if (path.isEmpty()){
            area.append("Selecione um arquivo para fazer a importação.");
            getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            openChooser();
        }
        if (codCurso == 0){
            tfCodigoCurso.selectAll();
            return;
        }
        if (codPeriodo == 0){
            tfCodigoPeriodo.selectAll();
            return;
        }
        try {
            setCurso(cursoBD.find(codCurso));
            setPeriodo(periodoBD.find(codPeriodo));
            turma.setId(turmaBD.nextCode());
            turma.setCurso(getCurso());
            turma.setPeriodo(getPeriodo());
            turma.setUnidade(Global.getUnidade());
            turma.setFase(cbFase.getSelectedIndex()+1);
            turma.setQtdMaximaAcertos(Integer.parseInt(tfQtdMaximaAcertos.getText()));
            listaTurmaDet = readyFile(path, turma.getId());
            if (listaTurmaDet != null && !listaTurmaDet.isEmpty()){
                area.append("Preparando-se para importar turma.\n");
                turma.setListaTurmaDet(listaTurmaDet);
                if (turmaBD.saveOrUpdate(turma)){
                    area.append("Turma importada com sucesso.\n");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Erro ao salvar/importar turma.");
        }
        getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    private List<TurmaDet> readyFile(String path, Integer codTurma) throws IOException, SQLException{
        File            file           = new File(path);
        FileReader      fr             = new FileReader(file);
        BufferedReader  br             = new BufferedReader(fr);
        List<TurmaDet>  listaTurmaDet  = null;
        TurmaDet        turmaDet       = null;
        String          line           = null;
        while (br.ready()){
            if (listaTurmaDet == null){
                listaTurmaDet = new ArrayList<TurmaDet>();
            }
            line = br.readLine();
            if (line == null){
                continue;
            }
            String[] text = line.split(";");
            if (!isEmpty(text)){
                turmaDet = new TurmaDet();
                Integer codigo = Integer.parseInt(text[0].trim());
                String   nome   = text[1].trim();
                turmaDet.setId(codTurma);
                turmaDet.setAcademico(new AcademicoBD().findByCodigo(codigo));
                if (turmaDet.getAcademico() != null){
                    listaTurmaDet.add(turmaDet);
                    area.append("Importando acadêmico '"+turmaDet.getAcademico().getCodigo()+ " - "+turmaDet.getAcademico().getNome()+"'.\n");
                }else{
                    area.append("Acadêmico "+codigo+" - "+nome+" não cadastrado.");
                }
            }
        }
        br.close();
        fr.close();
        return listaTurmaDet;
    }
    
     private boolean isEmpty(String[] text){
        return text[0].trim().isEmpty() || text[1].trim().isEmpty();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfPath = new javax.swing.JTextField();
        btEscolheArquivo = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        tfCodigoCurso = new javax.swing.JTextField();
        btBuscaCurso = new javax.swing.JButton();
        tfNomeCurso = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfCodigoPeriodo = new javax.swing.JTextField();
        btBuscaPeriodo = new javax.swing.JButton();
        tfDescricaoPeriodo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbFase = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        area = new javax.swing.JTextArea();
        btFechar = new javax.swing.JButton();
        btImportar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        tfQtdMaximaAcertos = new javax.swing.JTextField();

        jLabel1.setText("Arquivo:");

        tfPath.setEditable(false);
        tfPath.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btEscolheArquivo.setText("Escolher Arquivo");
        btEscolheArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEscolheArquivoActionPerformed(evt);
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

        btBuscaCurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBuscaCurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaCursoActionPerformed(evt);
            }
        });

        tfNomeCurso.setEditable(false);

        jLabel3.setText("Período:");

        tfCodigoPeriodo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigoPeriodo.setText("000000");
        tfCodigoPeriodo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfCodigoPeriodoFocusLost(evt);
            }
        });

        btBuscaPeriodo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBuscaPeriodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaPeriodoActionPerformed(evt);
            }
        });

        tfDescricaoPeriodo.setEditable(false);

        jLabel4.setText("Fase:");

        cbFase.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

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

        jLabel5.setText("Acertos:");

        tfQtdMaximaAcertos.setText("00");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(tfCodigoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btBuscaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(tfDescricaoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tfQtdMaximaAcertos, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(tfPath, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(btEscolheArquivo))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2)
                                .addGap(27, 27, 27)
                                .addComponent(tfCodigoCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(btBuscaCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(tfNomeCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel4)
                                .addGap(27, 27, 27)
                                .addComponent(cbFase, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(458, 458, 458)
                                .addComponent(btImportar)
                                .addGap(6, 6, 6)
                                .addComponent(btFechar)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(tfPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btEscolheArquivo))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfCodigoCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBuscaCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfNomeCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel3))
                    .addComponent(tfCodigoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBuscaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfDescricaoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(tfQtdMaximaAcertos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btImportar)
                    .addComponent(btFechar)))
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

    private void tfCodigoCursoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoCursoFocusLost
        carregaCurso();
    }//GEN-LAST:event_tfCodigoCursoFocusLost

    private void btBuscaCursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaCursoActionPerformed
        Curso c = abreConsultaCurso();
        if (c != null) {
            fillCurso(c);
        } else {
            tfCodigoCurso.setText("000000");
            tfNomeCurso.setText("");
            tfCodigoCurso.selectAll();
        }
    }//GEN-LAST:event_btBuscaCursoActionPerformed

    private void tfCodigoPeriodoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoPeriodoFocusLost
        carregaPeriodo();
    }//GEN-LAST:event_tfCodigoPeriodoFocusLost

    private void btBuscaPeriodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaPeriodoActionPerformed
        Periodo p = abreConsultaPeriodo();
        if (p != null){
            fillPeriodo(p);
        }else{
            tfCodigoPeriodo.setText("000000");
            tfDescricaoPeriodo.setText("");
            tfCodigoPeriodo.selectAll();
        }
    }//GEN-LAST:event_btBuscaPeriodoActionPerformed

    private void btImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btImportarActionPerformed
        importar();
    }//GEN-LAST:event_btImportarActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        dispose();
    }//GEN-LAST:event_btFecharActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea area;
    private javax.swing.JButton btBuscaCurso;
    private javax.swing.JButton btBuscaPeriodo;
    private javax.swing.JButton btEscolheArquivo;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btImportar;
    private javax.swing.JComboBox cbFase;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField tfCodigoCurso;
    private javax.swing.JTextField tfCodigoPeriodo;
    private javax.swing.JTextField tfDescricaoPeriodo;
    private javax.swing.JTextField tfNomeCurso;
    private javax.swing.JTextField tfPath;
    private javax.swing.JTextField tfQtdMaximaAcertos;
    // End of variables declaration//GEN-END:variables
    
       private class MyVerify extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            try {
                Integer.parseInt(tfCodigoCurso.getText());
            } catch (NumberFormatException e) {
                tfCodigoCurso.setText("000000");
                tfCodigoCurso.selectAll();
                return false;
            }
            try {
                Integer.parseInt(tfCodigoPeriodo.getText());
            } catch (NumberFormatException e) {
                tfCodigoPeriodo.setText("000000");
                tfCodigoPeriodo.selectAll();
                return false;
            }
             try {
                Integer.parseInt(tfQtdMaximaAcertos.getText());
            } catch (NumberFormatException e) {
                tfQtdMaximaAcertos.setText("00");
                tfQtdMaximaAcertos.selectAll();
                return false;
            }
             return true;
        }
    }
    
    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }
}

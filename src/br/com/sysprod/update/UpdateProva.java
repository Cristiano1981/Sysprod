package br.com.sysprod.update;

import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.ProvaBD;
import br.com.sysprod.dao.ProvaDetBD;
import br.com.sysprod.dao.TurmaBD;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.model.TableProvaDet;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Prova;
import br.com.sysprod.vo.ProvaDet;
import br.com.sysprod.vo.Turma;
import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.DefaultCaret;

/**
 * @author Cristiano Bombazar
 */

public final class UpdateProva extends javax.swing.JDialog implements Formulario {

    private List<ProvaDet> listaProvaDet = new ArrayList<ProvaDet>();
    private List<ProvaDet> listaProvaDetExcluir = new ArrayList<ProvaDet>();
    private ProvaDet provaDet;
    
    
    public UpdateProva(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initForm();
        initComponent();
        tableSelection();
        propriedadeTable();
    }

    @Override
    public void initForm() {
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void initComponent() {
        tfCodigo.setToolTipText("Código da prova");
        tfDataCad.setToolTipText("Data de cadastro da prova");
        tfDtAplicacao.setToolTipText("Data de aplicação da prova");
        tfObs.setToolTipText("Observações da prova");
        tfDescricaoTurma.setToolTipText("Descrição da turma");
        tfCodigoTurma.setInputVerifier(new MyVerify());
        tfDataCad.setText(Utils.converteDateParaString(new Date()));
        tfCodigoTurma.selectAll();
        ((DefaultCaret) tfObs.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    @Override
    public boolean validateForm() {
        Component[] c = {tfCodigoTurma, tfDataCad, tfDtAplicacao};
        return Utils.validaObrigatorios(c);
    }

    @Override
    public void end() {
        dispose();
    }
    
    public void novo(){
        setTitle("Nova Prova");
        Integer codigo = null;
        ProvaBD provBD = new ProvaBD();
        try {
            codigo = provBD.nextCode();
            tfCodigo.setText(Utils.codigoFormatado(codigo));
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro buscar próximo código da prova.");
        }
    }
    
    public void propriedadeTable() {
        tbProvaDet.getColumnModel().getColumn(0).setPreferredWidth(30);
        tbProvaDet.getColumnModel().getColumn(1).setPreferredWidth(380);
        tbProvaDet.getTableHeader().setReorderingAllowed(false);
        tbProvaDet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void atualizaTabela() {
        labelQuantidade.setText("Quantidade de perguntas: "+listaProvaDet.size());
        ((AbstractTableModel) tbProvaDet.getModel()).fireTableDataChanged();
    }

    private Turma abreConsultaTurma() {
        ConsultaDialog consulta = new ConsultaDialog(null, true);
        consulta.vinculaTurma();
        consulta.initForm();
        consulta.setVisible(true);
        return consulta.getBrowseTurma().getTurma();
    }

    private void fillTurma(Turma t) {
        tfCodigoTurma.setText(Utils.codigoFormatado(t.getId()));
        tfDescricaoTurma.setText(t.getCurso().getDescricao() + " - " + t.getFase() + "ª");
    }

    private void carregaTurma() {
        Integer codigo = Integer.parseInt(tfCodigoTurma.getText());
        if (codigo > 0) {
            TurmaBD turBD = new TurmaBD();
            Turma turma = null;
            try {
                turma = turBD.find(codigo, false);
            } catch (SQLException e) {
                e.printStackTrace();
                ErrorVerification.ErrDetalhe(e, "Erro ao consulta turma.");
            }
            if (turma != null) {
                fillTurma(turma);
            } else {
                Turma t = abreConsultaTurma();
                if (t != null) {
                    fillTurma(t);
                } else {
                    tfCodigoTurma.setText("000000");
                    tfDescricaoTurma.setText("");
                    tfCodigoTurma.selectAll();
                }
            }
        }
    }
    
    private void salvar(){
        Prova prova = new Prova();
        Turma turma = new Turma();
        ProvaBD provaBD = new ProvaBD();
        prova.setId(Integer.parseInt(tfCodigo.getText()));
        prova.setDtCadastro(Utils.converteDate(tfDataCad.getText()));
        prova.setDtAplicacao(Utils.converteDate(tfDtAplicacao.getText()));
        prova.setObs(tfObs.getText());
        turma.setId(Integer.parseInt(tfCodigoTurma.getText()));
        prova.setTurma(turma);
        prova.setListaProvaDet(listaProvaDet);
        prova.setListaProvaDetExcluir(listaProvaDetExcluir);
        try {
            if (provaBD.saveOrUpdate(prova)){
                end();
            }
        } catch (SQLException e) {
            ErrorVerification.ErrDetalhe(e, "Erro ao gravar prova");
            e.printStackTrace();
        }
    }
    
    public void editar(Prova p){
        tfCodigo.setText(Utils.codigoFormatado(p.getId()));
        tfDataCad.setText(Utils.converteDateParaString(p.getDtCadastro()));
        tfDtAplicacao.setText(Utils.converteDateParaString(p.getDtAplicacao()));
        tfObs.setText(p.getObs());
        fillTurma(p.getTurma());
        List<ProvaDet> listaAux = null;
        if (p.getListaProvaDet() == null){
            ProvaDetBD provaDetBD = new ProvaDetBD();
            try {
                listaAux = provaDetBD.queryAllByIdProva(p.getId());
            } catch (SQLException e) {
                ErrorVerification.ErrDetalhe(e, "Erro ao buscar lista de questões.");
                e.printStackTrace();
            }
        }else{
            listaAux = p.getListaProvaDet();
        }
        if (listaAux != null && !listaAux.isEmpty()){
            listaProvaDet.addAll(listaAux);
        }
        atualizaTabela();
    }
    
    private void excluir(){
        if (getProvaDet() != null){
            int result = JOptionPane.showConfirmDialog(null, "Deseja excluir o registro selecionado?", "Atenção!", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION){
                listaProvaDetExcluir.add(getProvaDet());
                listaProvaDet.remove(getProvaDet());
                setProvaDet(null);
                atualizaTabela();
            }
        }
    } 
    
    private void novaProvaDet(){
        Integer idProva  = Integer.parseInt(tfCodigo.getText());
        Integer idTurma = Integer.parseInt(tfCodigoTurma.getText());
        UpdateProvaDet update = new UpdateProvaDet(null, true, listaProvaDet, idProva, idTurma);
        update.setTitle("Nova pergunta");
        update.setVisible(true);
        atualizaTabela();
    }
    
    private void editarProvaDet(){
        if (getProvaDet()!= null){
            Integer index = listaProvaDet.indexOf(getProvaDet());
            Integer idProva = Integer.parseInt(tfCodigo.getText());
            Integer idTurma = Integer.parseInt(tfCodigoTurma.getText());
            UpdateProvaDet updateProvaDet = new UpdateProvaDet(null, true, listaProvaDet, idProva, idTurma);
            updateProvaDet.editar(getProvaDet(), index);
            updateProvaDet.setVisible(true);
            atualizaTabela();
        }
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfCodigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfCodigoTurma = new javax.swing.JTextField();
        btBusca = new javax.swing.JButton();
        tfDescricaoTurma = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfDataCad = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tfDtAplicacao = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tfObs = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProvaDet = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        labelQuantidade = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações da Prova"));

        jLabel1.setText("Código:");

        tfCodigo.setEditable(false);
        tfCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigo.setText("000000");

        jLabel2.setText("Turma:");

        tfCodigoTurma.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigoTurma.setText("000000");
        tfCodigoTurma.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfCodigoTurmaFocusLost(evt);
            }
        });

        btBusca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaActionPerformed(evt);
            }
        });

        tfDescricaoTurma.setEditable(false);

        jLabel3.setText("Dt Cad:");

        tfDataCad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfDataCad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfDataCadFocusLost(evt);
            }
        });

        jLabel4.setText("Dt Aplicação:");

        tfDtAplicacao.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfDtAplicacao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfDtAplicacaoFocusLost(evt);
            }
        });

        tfObs.setColumns(20);
        tfObs.setRows(5);
        jScrollPane1.setViewportView(tfObs);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                            .addComponent(tfCodigoTurma))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfDescricaoTurma))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfDataCad, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfDtAplicacao, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 67, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tfDataCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(tfDtAplicacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfDescricaoTurma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(tfCodigoTurma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Informações", jPanel3);

        tbProvaDet.setModel(new TableProvaDet(listaProvaDet));
        jScrollPane2.setViewportView(tbProvaDet);

        jButton3.setText("Excluir");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Editar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Novo");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        labelQuantidade.setText("Quantidade:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelQuantidade)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(labelQuantidade))
                .addGap(86, 86, 86))
        );

        jTabbedPane1.addTab("Perguntas", jPanel4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton1.setText("Fechar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Gravar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
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

    private void btBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaActionPerformed
        Turma t = abreConsultaTurma();
        if (t != null) {
            fillTurma(t);
        } else {
            tfCodigoTurma.setText("000000");
            tfDescricaoTurma.setText("");
            tfCodigoTurma.selectAll();
        }
    }//GEN-LAST:event_btBuscaActionPerformed

    private void tfCodigoTurmaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoTurmaFocusLost
        carregaTurma();
    }//GEN-LAST:event_tfCodigoTurmaFocusLost

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (validateForm()){
            salvar();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        excluir();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        end();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tfDataCadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDataCadFocusLost
        tfDataCad.setText(Utils.formataData(tfDataCad.getText()));
    }//GEN-LAST:event_tfDataCadFocusLost

    private void tfDtAplicacaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDtAplicacaoFocusLost
//        Date dtCadastro  = Utils.converteDate(tfDataCad.getText());
//        Date dtAplicacao = Utils.converteDate(tfDtAplicacao.getText());
//        if (dtAplicacao.getTime() < dtCadastro.getTime()){
//            JOptionPane.showMessageDialog(null, "A data de aplicação da prova tem que ser igual ou maior a data de cadastro.!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
//            tfDtAplicacao.setText("");
//            tfDtAplicacao.requestFocusInWindow();
//        }else{
            tfDtAplicacao.setText(Utils.formataData(tfDtAplicacao.getText()));
//        }
    }//GEN-LAST:event_tfDtAplicacaoFocusLost

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (validateForm()){
            novaProvaDet();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       if (validateForm()){
           editarProvaDet();
       }
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBusca;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelQuantidade;
    private javax.swing.JTable tbProvaDet;
    private javax.swing.JTextField tfCodigo;
    private javax.swing.JTextField tfCodigoTurma;
    private javax.swing.JTextField tfDataCad;
    private javax.swing.JTextField tfDescricaoTurma;
    private javax.swing.JTextField tfDtAplicacao;
    private javax.swing.JTextArea tfObs;
    // End of variables declaration//GEN-END:variables

    private void tableSelection() {
        tbProvaDet.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    int rowSelected = lsm.getMinSelectionIndex();
                    setProvaDet(listaProvaDet.get(rowSelected));
                }
            }
        });
    }
    
    private class MyVerify extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            try {
                Integer.parseInt(tfCodigoTurma.getText());
            } catch (Exception e) {
                tfCodigoTurma.setText("000000");
                tfCodigoTurma.selectAll();
                return false;
            }
             return true;
        }
    }
    
    public ProvaDet getProvaDet() {
        return provaDet;
    }

    public void setProvaDet(ProvaDet provaDet) {
        this.provaDet = provaDet;
    }

}

package br.com.sysprod.browse;

import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.ProvaBD;
import br.com.sysprod.dao.ProvaDetBD;
import br.com.sysprod.dao.TurmaBD;
import br.com.sysprod.interfaces.FormPainel;
import br.com.sysprod.model.TableProva;
import br.com.sysprod.update.DuplicaProva;
import br.com.sysprod.update.UpdateProva;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Prova;
import br.com.sysprod.vo.ProvaDet;
import br.com.sysprod.vo.Turma;
import java.awt.Cursor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public final class BrowseProva extends javax.swing.JPanel implements FormPainel{
    
    private List<Prova> listaProva = new ArrayList<Prova>();
    private Prova prova;

    public BrowseProva() {
        initComponents();
        initComponent();
        propriedadeTable();
        filtro();
        tableSelection();
    }
    
        @Override
    public void initComponent() {
        tfCodigoTurma.setToolTipText("Digite um código ou clique no ícone ao lado para abrir tela de busca");
        btBusca.setToolTipText("Clica para abrir tela de busca");
        tfDescricaoTurma.setToolTipText("Descrição da turma");
        tfCodigo.setInputVerifier(new MyVerify());
        tfCodigoTurma.setInputVerifier(new MyVerify());
        
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date dt1 = c.getTime();
        c.add(Calendar.MONTH, +1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date dt2 = c.getTime();
        
        tfData1.setText(Utils.converteDateParaString(dt1));
        tfData2.setText(Utils.converteDateParaString(dt2));
    }

    @Override
    public void propriedadeTable() {
        tbCodigo.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbCodigo.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbCodigo.getColumnModel().getColumn(2).setPreferredWidth(50);
        tbCodigo.getColumnModel().getColumn(3).setPreferredWidth(50);
        tbCodigo.getTableHeader().setReorderingAllowed(false);
        tbCodigo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void atualizaTabela() {
        ((AbstractTableModel) tbCodigo.getModel()).fireTableDataChanged();
    }

    @Override
    public void filtro() {
        Integer codTurma = Integer.parseInt(tfCodigoTurma.getText());
        String  dt1      = Utils.converteDateParaStringBD(tfData1.getText());
        String  dt2      = Utils.converteDateParaStringBD(tfData2.getText());
        Integer tpData   = cbData.getSelectedIndex();
        Integer fase     = cbFase.getSelectedIndex();
        String  filtro   = "";
        if (codTurma > 0){
            filtro = " AND turma.id_turma = "+codTurma;
        }
        if (tpData == 0){
            filtro += " AND prova.dt_cadastro  BETWEEN '"+dt1+"' AND '"+dt2+"'";
        }else{
            filtro += " AND prova.dt_aplicacao BETWEEN '"+dt1+"' AND '"+dt2+"'";
        }
        if (fase != 10){
            filtro += " AND turma.fase = "+(fase+1);
        }
        
        List<Prova> lista = null;
        ProvaBD provaBD = new ProvaBD();
        try {
            listaProva.clear();
            lista = provaBD.queryAll(filtro, null);
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Erro ao buscar lista de provas");
        }
        if (lista != null && !lista.isEmpty()){
            listaProva.addAll(lista);
        }
        atualizaTabela();
    }

    @Override
    public void novo() {
        UpdateProva update = new UpdateProva(null, true);
        update.novo();
        update.setVisible(true);
        filtro();
        limpa();
    }

    @Override
    public void editar() {
        UpdateProva update = new UpdateProva(null, true);
        update.editar(getProva());
        update.setVisible(true);
        filtro();
        limpa();
    }

    @Override
    public void excluir() throws Exception {
        if (getProva() != null){
             int result = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir o registro selecionado?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
             getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
             if (result == JOptionPane.YES_OPTION){
                 setProva(new ProvaBD().find(getProva().getId(), true));
                 if (new ProvaBD().delete(getProva())){
                     JOptionPane.showMessageDialog(null, "Registro excluído com sucesso.");
                     limpa();
                 }
             }
             getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private Turma abreConsultaTurma() {
        ConsultaDialog consulta = new ConsultaDialog(null, true);
        consulta.vinculaTurma();
        consulta.initForm();
        consulta.setVisible(true);
        Turma t = consulta.getBrowseTurma().getTurma();
        return t;
    }

    private void fillTruma(Turma t) {
        if (t == null){
            tfCodigoTurma.setText("000000");
            tfDescricaoTurma.setText("");
        }else{
            tfCodigoTurma.setText(Utils.codigoFormatado(t.getId()));
            tfDescricaoTurma.setText(t.getCurso().getDescricao() + " - "+t.getFase()+"ª");
        }
    }

    private void carregaTurma() {
        Integer codigo = Integer.parseInt(tfCodigoTurma.getText());
        if (codigo > 0) {
            TurmaBD turBD = new TurmaBD();
            Turma turma = null;
            try {
                turma = turBD.find(codigo, false);
            } catch (SQLException e) {
                ErrorVerification.ErrDetalhe(e, "Erro ao buscar turma");
            }
            if (turma != null) {
                fillTruma(turma);
            } else {
                Turma c = abreConsultaTurma();
                if (c != null) {
                    fillTruma(c);
                } else {
                    fillTruma(null);
                    tfCodigoTurma.selectAll();
                }
            }
        } else {
            fillTruma(null);
            tfCodigoTurma.selectAll();
        }
        filtro();
    }
    
    private void limpa(){
        tbCodigo.clearSelection();
        setProva(null);
    }
    
    private void duplicar(){
        if (getProva() != null){
            DuplicaProva duplica = new DuplicaProva(null, true, getProva().getId());
            duplica.setVisible(true);
            filtro();
        }
    }
    
    private void codeSelected() {
        Integer codigo = Integer.parseInt(tfCodigo.getText());
        for (int i = 0; i < listaProva.size(); i++) {
            if (codigo.equals(listaProva.get(i).getId())) {
                tbCodigo.setRowSelectionInterval(i, i);
                break;
            }
        }
        tfCodigo.setText(Utils.codigoFormatado(codigo));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfCodigoTurma = new javax.swing.JTextField();
        btBusca = new javax.swing.JButton();
        tfDescricaoTurma = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cbData = new javax.swing.JComboBox();
        tfData1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfData2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbFase = new javax.swing.JComboBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCodigo = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        tfCodigo = new javax.swing.JTextField();
        btFechar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        btSelecionar = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros"));

        jLabel1.setText("Turma:");

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

        jLabel2.setText("Data:");

        cbData.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cadatro", "Aplicação" }));
        cbData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDataActionPerformed(evt);
            }
        });

        tfData1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfData1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfData1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfData1FocusLost(evt);
            }
        });

        jLabel3.setText("Até:");

        tfData2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfData2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfData2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfData2FocusLost(evt);
            }
        });

        jLabel4.setText("Fase:");

        cbFase.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Todas" }));
        cbFase.setSelectedIndex(10);
        cbFase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFaseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbData, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfCodigoTurma))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfDescricaoTurma, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(tfData1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfData2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbFase, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfDescricaoTurma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(tfCodigoTurma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfData1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tfData2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(cbFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tbCodigo.setModel(new TableProva(listaProva));
        jScrollPane1.setViewportView(tbCodigo);

        jLabel5.setText("Código:");

        tfCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigo.setText("000000");
        tfCodigo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfCodigoFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Código", jPanel3);

        btFechar.setText("Fechar");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        jButton1.setText("Excluir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Editar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Novo");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Duplicar Prova");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        btSelecionar.setText("Selecionar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btSelecionar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btFechar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btFechar)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(btSelecionar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaActionPerformed
         Turma c = abreConsultaTurma();
         if (c != null) {
           fillTruma(c);
         } else {
           fillTruma(null);
           tfCodigoTurma.selectAll();
         }
        filtro();
    }//GEN-LAST:event_btBuscaActionPerformed

    private void tfCodigoTurmaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoTurmaFocusLost
        carregaTurma();
    }//GEN-LAST:event_tfCodigoTurmaFocusLost

    private void cbDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDataActionPerformed
        filtro();
    }//GEN-LAST:event_cbDataActionPerformed

    private void tfData1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfData1FocusLost
        tfData1.setText(Utils.formataData(tfData1.getText().trim()));
        if (tfData2.getText().trim().isEmpty()){
            tfData2.setText(Utils.formataData(tfData1.getText()));
        }
        filtro();
    }//GEN-LAST:event_tfData1FocusLost

    private void tfData2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfData2FocusLost
        tfData2.setText(Utils.formataData(tfData2.getText()));
        filtro();
    }//GEN-LAST:event_tfData2FocusLost

    private void cbFaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFaseActionPerformed
        filtro();
    }//GEN-LAST:event_cbFaseActionPerformed

    private void tfData1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfData1FocusGained
        tfData1.selectAll();
    }//GEN-LAST:event_tfData1FocusGained

    private void tfData2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfData2FocusGained
        tfData2.selectAll();
    }//GEN-LAST:event_tfData2FocusGained

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        novo();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        editar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        duplicar();
        }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            excluir();
        } catch (Exception e) {
            ErrorVerification.ErrDetalhe(e, "Erro ao excluir turma");
        }
        filtro();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tfCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoFocusLost
        codeSelected();
    }//GEN-LAST:event_tfCodigoFocusLost

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        limpa();
    }//GEN-LAST:event_btFecharActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBusca;
    public javax.swing.JButton btFechar;
    public javax.swing.JButton btSelecionar;
    private javax.swing.JComboBox cbData;
    private javax.swing.JComboBox cbFase;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbCodigo;
    private javax.swing.JTextField tfCodigo;
    private javax.swing.JTextField tfCodigoTurma;
    private javax.swing.JTextField tfData1;
    private javax.swing.JTextField tfData2;
    private javax.swing.JTextField tfDescricaoTurma;
    // End of variables declaration//GEN-END:variables

     private void tableSelection(){
         tbCodigo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    int rowSelected = lsm.getMinSelectionIndex();
                    int codSelected = Integer.parseInt(tbCodigo.getValueAt(rowSelected, 0).toString());
                    ProvaBD prBD = new ProvaBD();
                    try {
                        setProva(prBD.find(codSelected, false));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        ErrorVerification.ErrDetalhe(ex, "Erro ao buscar prova.");
                    }
                }
            }
        });
    }

    private class MyVerify extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            try {
                Integer.parseInt(tfCodigo.getText());
            } catch (NumberFormatException e) {
                tfCodigo.setText("000000");
                tfCodigo.selectAll();
                return false;
            }
            try {
                Integer.parseInt(tfCodigoTurma.getText());
            } catch (NumberFormatException e) {
                tfCodigoTurma.setText("000000");
                tfCodigoTurma.selectAll();
                return false;
            }
            return true;
        }
    }
    
    public Prova getProva() {
        return prova;
    }

    public void setProva(Prova prova) {
        this.prova = prova;
    }
}

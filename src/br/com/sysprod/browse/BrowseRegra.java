package br.com.sysprod.browse;

import br.com.sysprod.dao.DisciplinaBD;
import br.com.sysprod.dao.RegraBD;
import br.com.sysprod.interfaces.FormPainel;
import br.com.sysprod.model.TableRegra;
import br.com.sysprod.update.UpdateRegra;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Regra;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
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
public final class BrowseRegra extends javax.swing.JInternalFrame implements FormPainel{

    private List<Regra> listaRegra = new ArrayList<Regra>();
    private Regra regra;
    
    
    public BrowseRegra() {
        initComponents();
        initComponent();
        tableSelection();
        propriedadeTable();
    }
    
     @Override
    public void initComponent() {
        tfCodigo.setToolTipText("Código para buscar");
        btFechar.setToolTipText("Fechar a tela");
        btExcluir.setToolTipText("Excluir uma regra");
        btEditar.setToolTipText("Editar uma regra");
        btNovo.setToolTipText("Nova regra");
        cbStatus.setSelectedIndex(0);
        tfCodigo.setInputVerifier(new MyVerify());
    }

    @Override
    public void propriedadeTable() {
        tbCodigo.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbCodigo.getColumnModel().getColumn(1).setPreferredWidth(70);
        tbCodigo.getColumnModel().getColumn(2).setPreferredWidth(70);
        tbCodigo.getColumnModel().getColumn(3).setPreferredWidth(70);
        tbCodigo.getTableHeader().setReorderingAllowed(false);
        tbCodigo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void atualizaTabela() {
        ((AbstractTableModel) tbCodigo.getModel()).fireTableDataChanged();
    }

    @Override
    public void filtro() {
        listaRegra.clear();
        Integer status  = cbStatus.getSelectedIndex();
        boolean ativo   = false;
        String  filtro  = "";
        RegraBD regraBD = new RegraBD();
         if (status != 2) {
            ativo = status == 0;
            filtro += " WHERE ativo is " + ativo;
        }
       List<Regra> lista = null;
        try {
            lista = regraBD.queryAll(filtro, null);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Erro ao buscar regras");
        }
        if (lista != null && !lista.isEmpty()){
            listaRegra.addAll(lista);
        }
        atualizaTabela();
    }

    @Override
    public void novo() {
        UpdateRegra update = new UpdateRegra(null, true);
        update.novo();
        update.setVisible(true);
        filtro();
        limpa();
    }

    @Override
    public void editar() {
        if (getRegra() != null){
            UpdateRegra update = new UpdateRegra(null, true);
            update.editar(getRegra());
            update.setVisible(true);
            filtro();
            limpa();
        }
    }

    @Override
    public void excluir() throws Exception {
         int result = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir o registro selecionado?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            if (getRegra()!= null) {
                RegraBD regraBD = new RegraBD();
                if (regraBD.delete(getRegra())) {
                    JOptionPane.showMessageDialog(null, "Registro excluído com sucesso.");
                }
                filtro();
                limpa();
            }
        }
    }
    
    
    public void posicao() {
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 4);
    }
    
    private void limpa(){
        setRegra(null);
        tbCodigo.clearSelection();
    }
    
     private void codeSelected() {
        Integer codigo = Integer.parseInt(tfCodigo.getText());
        for (int i = 0; i < listaRegra.size(); i++) {
            if (codigo.equals(listaRegra.get(i).getId())) {
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
        cbStatus = new javax.swing.JComboBox();
        btFechar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btNovo = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCodigo = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        tfCodigo = new javax.swing.JTextField();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros"));

        jLabel1.setText("Status:");

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ativo", "Inativo", "Todos" }));
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
            .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        btFechar.setText("Fechar");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        btExcluir.setText("Excluir");
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btEditar.setText("Editar");
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btNovo.setText("Novo");
        btNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoActionPerformed(evt);
            }
        });

        tbCodigo.setModel(new TableRegra(listaRegra));
        jScrollPane1.setViewportView(tbCodigo);

        jLabel2.setText("Código:");

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Codigo", jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btNovo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btFechar))
                    .addComponent(jTabbedPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btFechar)
                    .addComponent(btExcluir)
                    .addComponent(btEditar)
                    .addComponent(btNovo))
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

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        filtro();
    }//GEN-LAST:event_cbStatusActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        try {
            excluir();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Erro ao excluir regra");
        }
    }//GEN-LAST:event_btExcluirActionPerformed

    private void tfCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoFocusLost
        codeSelected();
    }//GEN-LAST:event_tfCodigoFocusLost

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        editar();
    }//GEN-LAST:event_btEditarActionPerformed

    private void btNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNovoActionPerformed
        novo();
    }//GEN-LAST:event_btNovoActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        dispose();
    }//GEN-LAST:event_btFecharActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btNovo;
    private javax.swing.JComboBox cbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbCodigo;
    private javax.swing.JTextField tfCodigo;
    // End of variables declaration//GEN-END:variables

     private class MyVerify extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            try {
                Integer.parseInt(tfCodigo.getText());
                return true;
            } catch (Exception e) {
                tfCodigo.setText("000000");
                tfCodigo.selectAll();
                return false;
            }
        }
    }
     
    private void tableSelection() {
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
                    RegraBD regraBD = new RegraBD();
                    try {
                        setRegra(regraBD.find(codSelected));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        ErrorVerification.ErrDetalhe(ex, "Erro ao buscar regra por código.");
                    }
                }
            }
        });
    }
    
    public Regra getRegra() {
        return regra;
    }

    public void setRegra(Regra regra) {
        this.regra = regra;
    }

}

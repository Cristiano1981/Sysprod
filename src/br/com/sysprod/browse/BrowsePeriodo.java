package br.com.sysprod.browse;

import br.com.sysprod.dao.PeriodoBD;
import br.com.sysprod.interfaces.FormPainel;
import br.com.sysprod.model.TablePeriodo;
import br.com.sysprod.update.UpdatePeriodo;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.vo.Periodo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 * @author Cristiano Bombazar
 */
public final class BrowsePeriodo extends javax.swing.JPanel implements FormPainel {

    private List<Periodo> listaPeriodos = new ArrayList<Periodo>();
    private Periodo periodo;

    public BrowsePeriodo() {
        initComponents();
        filtro();
        initComponent();
        tableSelection();
        propriedadeTable();
    }

    @Override
    public void initComponent() {
        tbPeriodo.setToolTipText("Lista de períodos cadastrados");
    }

    public void propriedadeTable() {
        tbPeriodo.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbPeriodo.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbPeriodo.getColumnModel().getColumn(2).setPreferredWidth(100);
        tbPeriodo.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbPeriodo.getTableHeader().setReorderingAllowed(false);
        tbPeriodo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void atualizaTabela() {
        ((AbstractTableModel) tbPeriodo.getModel()).fireTableDataChanged();
    }

    @Override
    public void novo() {
        UpdatePeriodo upPer = new UpdatePeriodo(null, true);
        upPer.novo();
        upPer.setVisible(true);
        filtro();
        limpa();
    }

    @Override
    public void editar() {
        UpdatePeriodo upPer = new UpdatePeriodo(null, true);
        upPer.editar(getPeriodo());
        upPer.setVisible(true);
        filtro();
        limpa();
    }

    @Override
    public void excluir() throws Exception {
        int result = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir o registro selecionado?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            if (getPeriodo() != null) {
                PeriodoBD perBD = new PeriodoBD();
                if (perBD.delete(getPeriodo())) {
                    JOptionPane.showMessageDialog(null, "Registro excluído com sucesso.");
                }
                filtro();
                limpa();
            }
        }
    }

    public void filtro() {
        int status = cbStatus.getSelectedIndex();
        int ordem  = cbOrdem.getSelectedIndex()+1;
        String filtro = null;
        String order  = null;
        if (status != 2){
            boolean aberto = status == 1;
            filtro = " WHERE encerrado is "+aberto; 
        }
        order = " ORDER BY "+ordem;
        PeriodoBD unBD = new PeriodoBD();
        try {
            listaPeriodos.clear();
            List<Periodo> lista = unBD.queryAll(filtro, order);
            if (lista != null) {
                listaPeriodos.addAll(lista);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro ao buscar periodos");
        }
        atualizaTabela();
    }

    private void limpa() {
        tbPeriodo.clearSelection();
        setPeriodo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cbOrdem = new javax.swing.JComboBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbPeriodo = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        btSelecionar = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros"));
        jPanel2.setFocusTraversalPolicyProvider(true);

        jLabel1.setText("Status:");

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aberto", "Encerrado", "Todos" }));
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });

        jLabel2.setText("Ordem:");

        cbOrdem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Código", "Dt Inicio", "Dt Encerramento", "Aberto/Encerrado" }));
        cbOrdem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOrdemActionPerformed(evt);
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
                .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cbOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbPeriodo.setModel(new TablePeriodo(listaPeriodos));
        jScrollPane1.setViewportView(tbPeriodo);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Código", jPanel3);

        jButton1.setText("Novo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btEditar.setText("Editar");
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btExcluir.setText("Excluir");
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btFechar.setText("Fechar");

        btSelecionar.setText("Selecionar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btSelecionar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btExcluir)
                        .addGap(10, 10, 10)
                        .addComponent(btFechar))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btFechar)
                    .addComponent(btExcluir)
                    .addComponent(btEditar)
                    .addComponent(jButton1)
                    .addComponent(btSelecionar))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        filtro();
    }//GEN-LAST:event_cbStatusActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        novo();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        editar();
    }//GEN-LAST:event_btEditarActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        try {
            excluir();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Erro ao excluir período");
        }

    }//GEN-LAST:event_btExcluirActionPerformed

    private void cbOrdemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOrdemActionPerformed
        filtro();
    }//GEN-LAST:event_cbOrdemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    public javax.swing.JButton btFechar;
    public javax.swing.JButton btSelecionar;
    private javax.swing.JComboBox cbOrdem;
    private javax.swing.JComboBox cbStatus;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbPeriodo;
    // End of variables declaration//GEN-END:variables

    private void tableSelection() {
        tbPeriodo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    int rowSelected = lsm.getMinSelectionIndex();
                    int codSelected = Integer.parseInt(tbPeriodo.getValueAt(rowSelected, 0).toString());
                    PeriodoBD unBD = new PeriodoBD();
                    try {
                        setPeriodo(unBD.find(codSelected));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        ErrorVerification.ErrDetalhe(ex, "Erro ao buscar período.");
                    }
                }
            }
        });
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

}

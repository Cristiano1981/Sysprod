package br.com.sysprod.browse;

import br.com.sysprod.dao.TurmaBD;
import br.com.sysprod.model.TableTurmaSelection;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.vo.Turma;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

/**
 * @author Cristiano Bombazar
 */

public class BrowseConsultaTurma extends javax.swing.JDialog {
    
    private List<Turma> listaTurma          = new ArrayList<Turma>();
    private List<Turma> turmaSelection      = new ArrayList<Turma>();
    TableTurmaSelection tableTurmaSelection = new TableTurmaSelection(listaTurma);

    public BrowseConsultaTurma(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setModel();
        propriedadesTable();
        initForm();
        tableSelection();
        carrega();
    }
    
    private void setModel(){
        tbCodigo.setModel(tableTurmaSelection);
    }
    
    private void initForm(){
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Consulta turmas com período em aberto");
    }
    
    private void propriedadesTable(){
        tbCodigo.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbCodigo.getColumnModel().getColumn(1).setPreferredWidth(70);
        tbCodigo.getColumnModel().getColumn(2).setPreferredWidth(150);
        tbCodigo.getColumnModel().getColumn(3).setPreferredWidth(70);
        tbCodigo.getColumnModel().getColumn(4).setPreferredWidth(70);
        tbCodigo.getTableHeader().setReorderingAllowed(false);
        tbCodigo.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }
    
    
    private void tableSelection(){
        tbCodigo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                 if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    turmaSelection.clear();
                    turmaSelection.addAll(tableTurmaSelection.getSelecteds());
                }
            }
        });
    }
    
    private void carrega(){
        listaTurma.clear();
        TurmaBD turmaBD = new TurmaBD();
        List<Turma> listaAux = null;
        try {
            listaAux = turmaBD.queryAll(" AND d.encerrado is false ", null);
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Erro ao buscar lista de turmas");
        }
        if (listaAux != null && !listaAux.isEmpty()){
            listaTurma.addAll(listaAux);
        }
        atualizaTabela();
    }
    
    public void atualizaTabela() {
        ((AbstractTableModel) tbCodigo.getModel()).fireTableDataChanged();
    }
    
    private void limpa(){
        setTurmaSelection(null);
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btFechar = new javax.swing.JButton();
        btSelecionar = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbCodigo = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        tfCodigo = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btFechar.setText("Fechar");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        btSelecionar.setText("Selecionar");
        btSelecionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelecionarActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(tbCodigo);

        jLabel1.setText("Código:");

        tfCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigo.setText("000000");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Código", jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btSelecionar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btFechar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btFechar)
                    .addComponent(btSelecionar))
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

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        limpa();
        dispose();
    }//GEN-LAST:event_btFecharActionPerformed

    private void btSelecionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelecionarActionPerformed
        dispose();
    }//GEN-LAST:event_btSelecionarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btSelecionar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbCodigo;
    private javax.swing.JTextField tfCodigo;
    // End of variables declaration//GEN-END:variables

    public List<Turma> getTurmaSelection() {
        return turmaSelection;
    }

    public void setTurmaSelection(List<Turma> turmaSelection) {
        this.turmaSelection = turmaSelection;
    }
    
}

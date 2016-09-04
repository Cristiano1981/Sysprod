package br.com.sysprod.browse;

import br.com.sysprod.dao.DisciplinaBD;
import br.com.sysprod.interfaces.FormPainel;
import br.com.sysprod.model.TableDisciplina;
import br.com.sysprod.update.UpdateDisciplina;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Disciplina;
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
public final class BrowseDisciplina extends javax.swing.JPanel implements FormPainel {

    private List<Disciplina> listaDisciplinaCodigo = new ArrayList<Disciplina>();
    private List<Disciplina> listaDisciplinaNome = new ArrayList<Disciplina>();
    private Disciplina disciplina;

    public BrowseDisciplina() {
        initComponents();
        initComponent();
        propriedadeTable();
        tableSelection();
    }

    @Override
    public void initComponent() {
        tbCodigo.setToolTipText("Lista de disciplinas cadastrados");
        tbNome.setToolTipText("Lista de disciplinas cadastrados");
        cbStatus.setToolTipText("Status do curso");
        cbOrdem.setToolTipText("Ordem da busca");
        cbStatus.setSelectedIndex(0);
        tfCodigo.setInputVerifier(new MyVerify());
    }

    @Override
    public void propriedadeTable() {
        tbCodigo.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbCodigo.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbCodigo.getTableHeader().setReorderingAllowed(false);
        tbCodigo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbNome.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbNome.getColumnModel().getColumn(1).setPreferredWidth(300);
        tbNome.getTableHeader().setReorderingAllowed(false);
        tbNome.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void atualizaTabela() {
        ((AbstractTableModel) tbCodigo.getModel()).fireTableDataChanged();
        ((AbstractTableModel) tbNome.getModel()).fireTableDataChanged();
    }

    @Override
    public void novo() {
        UpdateDisciplina up = new UpdateDisciplina(null, true);
        up.novo();
        up.setVisible(true);
        filtro();
        limpa();
    }

    @Override
    public void editar() {
        UpdateDisciplina up = new UpdateDisciplina(null, true);
        up.editar(getDisciplina());
        up.setVisible(true);
        filtro();
        limpa();
    }

    @Override
    public void excluir() throws Exception {
        int result = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir o registro selecionado?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            if (getDisciplina()!= null) {
                DisciplinaBD disBD = new DisciplinaBD();
                if (disBD.delete(getDisciplina())) {
                    JOptionPane.showMessageDialog(null, "Registro excluído com sucesso.");
                }
                filtro();
                limpa();
            }
        }
    }

    @Override
    public void filtro() {
        Integer ordem  = cbOrdem.getSelectedIndex();
        Integer status = cbStatus.getSelectedIndex();
        String  nome   = tfDescricao.getText().trim().toUpperCase();
        Integer fase   = cbFase.getSelectedIndex();
        boolean ativo  = false;
        String filtro  = "";
        String order   = "";
        DisciplinaBD disBD = new DisciplinaBD();
        if (painel.getSelectedIndex() == 1) {
            listaDisciplinaNome.clear();
            if (!nome.trim().equals("")) {
                filtro = "AND nome like '" + nome + "%'";
            }
        } else {
            listaDisciplinaCodigo.clear();
        }
        if (status != 2) {
            ativo = status == 0;
            filtro += " AND ativo is " + ativo;
        }
        if (fase != 10){
            filtro += " AND fase = "+Integer.parseInt(cbFase.getSelectedItem().toString());
        }
        if (ordem == 0) {
            order = " ORDER BY id_disciplina";
        } else {
            order = " ORDER BY nome;";
        }
        List<Disciplina> lista = null;
        try {
            lista = disBD.queryAll(filtro, order);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Erro ao buscar lista de disciplinas");
        }

        if (lista != null && !lista.isEmpty()) {
            listaDisciplinaCodigo.addAll(lista);
            listaDisciplinaNome.addAll(lista);
        }
        atualizaTabela();
    }

    private void codeSelected() {
        Integer codigo = Integer.parseInt(tfCodigo.getText());
        for (int i = 0; i < listaDisciplinaCodigo.size(); i++) {
            if (codigo.equals(listaDisciplinaCodigo.get(i).getId())) {
                tbCodigo.setRowSelectionInterval(i, i);
                break;
            }
        }
        tfCodigo.setText(Utils.codigoFormatado(codigo));
    }

    private void limpa() {
        tbCodigo.clearSelection();
        tbNome.clearSelection();
        setDisciplina(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbOrdem = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        cbFase = new javax.swing.JComboBox();
        painel = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCodigo = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        tfCodigo = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbNome = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        tfDescricao = new javax.swing.JTextField();
        btSelecionar = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btNovo = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros"));

        jLabel1.setText("Ordenar: ");

        cbOrdem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Código", "Nome" }));
        cbOrdem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOrdemActionPerformed(evt);
            }
        });

        jLabel4.setText("Status:");

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ativo", "Inativo", "Todos" }));
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });

        jLabel5.setText("Fase:");

        cbFase.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Todas" }));
        cbFase.setSelectedItem("Todas");
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
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cbFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        painel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                painelStateChanged(evt);
            }
        });

        tbCodigo.setModel(new TableDisciplina(listaDisciplinaCodigo));
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        painel.addTab("Código", jPanel3);

        tbNome.setModel(new TableDisciplina(listaDisciplinaNome));
        jScrollPane2.setViewportView(tbNome);

        jLabel3.setText("Nome:");

        tfDescricao.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        tfDescricao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfDescricaoFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfDescricao)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        painel.addTab("Nome", jPanel4);

        btSelecionar.setText("Selecionar");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(painel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btSelecionar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btNovo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btExcluir)
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
                .addComponent(painel, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btSelecionar)
                    .addComponent(btFechar)
                    .addComponent(btExcluir)
                    .addComponent(btEditar)
                    .addComponent(btNovo))
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

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        filtro();
    }//GEN-LAST:event_cbStatusActionPerformed

    private void cbOrdemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOrdemActionPerformed
        filtro();
    }//GEN-LAST:event_cbOrdemActionPerformed

    private void painelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_painelStateChanged
        filtro();
    }//GEN-LAST:event_painelStateChanged

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        try {
            excluir();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Erro ao excluir curso.");
        }
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        limpa();
    }//GEN-LAST:event_btFecharActionPerformed

    private void tfCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoFocusLost
        codeSelected();
    }//GEN-LAST:event_tfCodigoFocusLost

    private void btNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNovoActionPerformed
        novo();
    }//GEN-LAST:event_btNovoActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        editar();
    }//GEN-LAST:event_btEditarActionPerformed

    private void tfDescricaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDescricaoFocusLost
        filtro();
    }//GEN-LAST:event_tfDescricaoFocusLost

    private void cbFaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFaseActionPerformed
        filtro();
    }//GEN-LAST:event_cbFaseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    public javax.swing.JButton btFechar;
    private javax.swing.JButton btNovo;
    public javax.swing.JButton btSelecionar;
    private javax.swing.JComboBox cbFase;
    private javax.swing.JComboBox cbOrdem;
    private javax.swing.JComboBox cbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane painel;
    private javax.swing.JTable tbCodigo;
    private javax.swing.JTable tbNome;
    private javax.swing.JTextField tfCodigo;
    private javax.swing.JTextField tfDescricao;
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
                    DisciplinaBD disBD = new DisciplinaBD();
                    try {
                        setDisciplina(disBD.find(codSelected));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        ErrorVerification.ErrDetalhe(ex, "Erro ao buscar disciplina por código.");
                    }
                }
            }
        });
        tbNome.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    int rowSelected = lsm.getMinSelectionIndex();
                    int codSelected = Integer.parseInt(tbNome.getValueAt(rowSelected, 0).toString());
                    DisciplinaBD disBD = new DisciplinaBD();
                    try {
                        setDisciplina(disBD.find(codSelected));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        ErrorVerification.ErrDetalhe(ex, "Erro ao buscar disciplina por nome.");
                    }
                }
            }
        });
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }
}

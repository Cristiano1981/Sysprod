package br.com.sysprod.browse;

import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.AcademicoBD;
import br.com.sysprod.dao.DisciplinaBD;
import br.com.sysprod.dao.PerguntaBD;
import br.com.sysprod.interfaces.FormPainel;
import br.com.sysprod.model.TablePergunta;
import br.com.sysprod.update.UpdatePergunta;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Academico;
import br.com.sysprod.vo.Curso;
import br.com.sysprod.vo.Disciplina;
import br.com.sysprod.vo.Pergunta;
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
public class BrowsePergunta extends javax.swing.JPanel implements FormPainel{

    private List<Pergunta> listaPergunta = new ArrayList<Pergunta>();
    private Pergunta pergunta;
    
    
    public BrowsePergunta() {
        initComponents();
        initComponent();
        propriedadeTable();
        filtro();
        tableSelection();
    }
    
      @Override
    public void initComponent() {
        tfCodigo.setToolTipText("Digite um código para buscar");
        tfCodigoDisciplina.setToolTipText("Digite um código para buscar ou clique no ícone ao lado para abrir tela de busca.");
        btBusca.setToolTipText("Clique para abrir tela de busca.");
        tfNomeDisciplina.setToolTipText("Nome da disciplina");
        tfCodigo.setInputVerifier(new MyVerify());
        tfCodigoDisciplina.setInputVerifier(new MyVerify());
    }

    @Override
    public void propriedadeTable() {
        tbCodigo.getColumnModel().getColumn(0).setPreferredWidth(30);
        tbCodigo.getColumnModel().getColumn(1).setPreferredWidth(500);
        tbCodigo.getColumnModel().getColumn(2).setPreferredWidth(50);
        tbCodigo.getTableHeader().setReorderingAllowed(false);
        tbCodigo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void atualizaTabela() {
        ((AbstractTableModel) tbCodigo.getModel()).fireTableDataChanged();
    }

    @Override
    public void filtro() {
        Integer codigoDisciplina = Integer.parseInt(tfCodigoDisciplina.getText());
        Integer status           = cbStatus.getSelectedIndex();
        String  tipo             = cbTipo.getSelectedItem().toString().substring(0, 1);
        String  filtro           = "";
        String  order            = null;
        boolean ativo            = false;
        PerguntaBD perBD = new PerguntaBD();
        listaPergunta.clear();
        if (status != 2) {
            ativo = status == 0;
            filtro += " AND pergunta.ativo is " + ativo;
        }
        if (!tipo.equals("T")){
            filtro += " AND pergunta.tipo = '"+tipo+"'";
        }
        if (codigoDisciplina > 0) {
            filtro += " AND disciplina.id_disciplina = " + codigoDisciplina;
        }
        List<Pergunta> lista = null;
        try {
            lista = perBD.queryAll(filtro, order);
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro ao buscar pergunta.");
        }
        if (lista != null && !lista.isEmpty()) {
            listaPergunta.addAll(lista);
        }
        atualizaTabela();
    }

    @Override
    public void novo() {
        UpdatePergunta update = new UpdatePergunta(null, true);
        update.novo();
        update.setVisible(true);
        limpa();
        filtro();
    }

    @Override
    public void editar() {
        if (getPergunta() != null){
            UpdatePergunta update = new UpdatePergunta(null, true);
            update.editar(getPergunta());
            update.setVisible(true);
            limpa();
            filtro();
        }
    }

    @Override
    public void excluir() throws Exception {
        int result = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir o registro selecionado?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            if (getPergunta()!= null) {
                PerguntaBD perBD = new PerguntaBD();
                if (perBD.delete(getPergunta())) {
                    JOptionPane.showMessageDialog(null, "Registro excluído com sucesso.");
                }
                filtro();
                limpa();
            }
        }
    }
    
    private Disciplina abreConsultaDisciplina() {
        ConsultaDialog consulta = new ConsultaDialog(null, true);
        consulta.vinculaDisciplina();
        consulta.initForm();
        consulta.setVisible(true);
        Disciplina c = consulta.getBrowseDisciplina().getDisciplina();
        return c;
    }

    private void fillDisciplina(Disciplina c) {
        tfCodigoDisciplina.setText(Utils.codigoFormatado(c.getId()));
        tfNomeDisciplina.setText(c.getNome());
    }

    private void carregaDisciplina() {
        Integer codigo = Integer.parseInt(tfCodigoDisciplina.getText());
        if (codigo > 0) {
            DisciplinaBD disBD = new DisciplinaBD();
            Disciplina disciplina = null;
            try {
                disciplina = disBD.find(codigo);
            } catch (SQLException e) {
                ErrorVerification.ErrDetalhe(e, "Erro ao buscar disciplina");
            }
            if (disciplina != null) {
                fillDisciplina(disciplina);
            } else {
                Disciplina c = abreConsultaDisciplina();
                if (c != null) {
                    fillDisciplina(c);
                } else {
                    tfCodigoDisciplina.setText("000000");
                    tfNomeDisciplina.setText("");
                    tfCodigoDisciplina.selectAll();
                }
            }
        } else {
            tfCodigoDisciplina.setText("000000");
            tfNomeDisciplina.setText("");
            tfCodigoDisciplina.selectAll();
        }
        filtro();
    }
    
     private void codeSelected() {
        Integer codigo = Integer.parseInt(tfCodigo.getText());
        for (int i = 0; i < listaPergunta.size(); i++) {
            if (codigo.equals(listaPergunta.get(i).getId())) {
                tbCodigo.setRowSelectionInterval(i, i);
                break;
            }
        }
        tfCodigo.setText(Utils.codigoFormatado(codigo));
    }
    
     private void limpa(){
         tbCodigo.clearSelection();
         setPergunta(null);
     }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfCodigoDisciplina = new javax.swing.JTextField();
        btBusca = new javax.swing.JButton();
        tfNomeDisciplina = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cbTipo = new javax.swing.JComboBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCodigo = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        tfCodigo = new javax.swing.JTextField();
        btFechar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btSelecionar = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros"));

        jLabel1.setText("Disciplina:");

        tfCodigoDisciplina.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigoDisciplina.setText("000000");
        tfCodigoDisciplina.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfCodigoDisciplinaFocusLost(evt);
            }
        });

        btBusca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaActionPerformed(evt);
            }
        });

        tfNomeDisciplina.setEditable(false);

        jLabel3.setText("Status:");

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ativo", "Inativo", "Todas" }));
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });

        jLabel4.setText("Tipo:");

        cbTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pergunta", "Alternativa", "Todos" }));
        cbTipo.setSelectedIndex(2);
        cbTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoActionPerformed(evt);
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
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfCodigoDisciplina)
                    .addComponent(cbStatus, 0, 72, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(tfNomeDisciplina)
                        .addGap(47, 47, 47))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfNomeDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(tfCodigoDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        tbCodigo.setModel(new TablePergunta(listaPergunta));
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Código", jPanel3);

        btFechar.setText("Fechar");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        jButton1.setText("Editar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btExcluir.setText("Excluir");
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        jButton2.setText("Novo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btSelecionar.setText("Selecionar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btSelecionar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btFechar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTabbedPane1))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btFechar)
                    .addComponent(jButton1)
                    .addComponent(btExcluir)
                    .addComponent(jButton2)
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

    private void tfCodigoDisciplinaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoDisciplinaFocusLost
        carregaDisciplina();
    }//GEN-LAST:event_tfCodigoDisciplinaFocusLost

    private void btBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaActionPerformed
        Disciplina c = abreConsultaDisciplina();
        if (c != null) {
            fillDisciplina(c);
        } else {
            tfCodigoDisciplina.setText("000000");
            tfNomeDisciplina.setText("");
            tfCodigoDisciplina.selectAll();
        }
        filtro();
    }//GEN-LAST:event_btBuscaActionPerformed

    private void tfCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoFocusLost
        codeSelected();
    }//GEN-LAST:event_tfCodigoFocusLost

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        filtro();
    }//GEN-LAST:event_cbStatusActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
          try {
            excluir();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro ao gravar registro.");
        }
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        limpa();
    }//GEN-LAST:event_btFecharActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        novo();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        editar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoActionPerformed
        filtro();
    }//GEN-LAST:event_cbTipoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBusca;
    private javax.swing.JButton btExcluir;
    public javax.swing.JButton btFechar;
    public javax.swing.JButton btSelecionar;
    private javax.swing.JComboBox cbStatus;
    public javax.swing.JComboBox cbTipo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbCodigo;
    private javax.swing.JTextField tfCodigo;
    private javax.swing.JTextField tfCodigoDisciplina;
    private javax.swing.JTextField tfNomeDisciplina;
    // End of variables declaration//GEN-END:variables

    private class MyVerify extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            try {
                Integer.parseInt(tfCodigo.getText());
            } catch (Exception e) {
                tfCodigo.setText("000000");
                tfCodigo.selectAll();
                return false;
            }
            try {
                Integer.parseInt(tfCodigoDisciplina.getText());
            } catch (Exception e) {
                tfCodigoDisciplina.setText("000000");
                tfCodigoDisciplina.selectAll();
                return false;
            }
            return true;
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
                    PerguntaBD perguntaBD = new PerguntaBD();
                    try {
                        setPergunta(perguntaBD.find(codSelected));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        ErrorVerification.ErrDetalhe(ex, "Erro ao buscar pergunta por código.");
                    }
                }
            }
        });
    }
    
    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

}

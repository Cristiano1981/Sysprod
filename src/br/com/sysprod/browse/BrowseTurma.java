package br.com.sysprod.browse;

import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.TurmaBD;
import br.com.sysprod.dao.CursoBD;
import br.com.sysprod.dao.PeriodoBD;
import br.com.sysprod.interfaces.FormPainel;
import br.com.sysprod.update.UpdateTurma;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Turma;
import br.com.sysprod.vo.Curso;
import br.com.sysprod.vo.Periodo;
import java.awt.Cursor;
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
public final class BrowseTurma extends javax.swing.JPanel implements FormPainel{

    
    private List<Turma> listaTurma = new ArrayList<Turma>();
    private Turma turma;
    
    public BrowseTurma() {
        initComponents();
        filtro();
        initComponent();
        propriedadeTable();
        tableSelection();
    }
    
      @Override
    public void initComponent() {
        tfCodigo.setToolTipText("Digite um código para busca");
        tfCodigoCurso.setToolTipText("Digite um código ou clique no ícone ao lado para busca");
        tfNomeCurso.setToolTipText("Nome do curso");
        tfCodigoPeriodo.setToolTipText("Digite um codigo ou clique no ícone ao lado para busca");
        cbFase.setToolTipText("Selecione uma fase para filtrar");
        cbStatus.setToolTipText("Selecione um status para filtrar o período");
        cbFase.setSelectedIndex(10);
        cbStatus.setSelectedIndex(0);
        tfCodigo.setInputVerifier(new MyVerify());
        tfCodigoCurso.setInputVerifier(new MyVerify());
        tfCodigoPeriodo.setInputVerifier(new MyVerify());
    }

    @Override
    public void propriedadeTable() {
        tbCodigo.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbCodigo.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbCodigo.getTableHeader().setReorderingAllowed(false);
        tbCodigo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void atualizaTabela() {
        ((AbstractTableModel) tbCodigo.getModel()).fireTableDataChanged();
    }

    @Override
    public void filtro() {
        Integer         codigoCurso   = Integer.parseInt(tfCodigoCurso.getText());
        Integer         codigoPeriodo = Integer.parseInt(tfCodigoPeriodo.getText());
        Integer         fase          = cbFase.getSelectedIndex();
        Integer         status        = cbStatus.getSelectedIndex();
        Integer         ordem         = cbOrdem.getSelectedIndex();
        String          filtro        = "";
        String          order         = "";
        TurmaBD acadBD        = new TurmaBD();
        if (codigoCurso > 0){
            filtro = " AND c.id_curso = "+codigoCurso; 
        }
        if (codigoPeriodo > 0){
            filtro += " AND d.id_periodo = "+codigoPeriodo;
        }
        if (fase != 10){
            filtro += " AND a.fase = "+(fase+1); //10 = TODAS. O +1 É PRA USAR O ÍNDICE COMO NÚMERO DA FASE.
        }
        if (status != 2){
            boolean aberto = status == 1;
            filtro += " AND d.encerrado is "+aberto;
        }
        if (ordem == 0){
            order = " ORDER BY a.id_turma";
        }else if (ordem == 1){
            order = " ORDER BY c.descricao";
        }else if (ordem == 2){
            order = " ORDER BY d.descricao";
        }else{
            order = " ORDER BY a.fase";
        }
        try {
            List<Turma> lista = acadBD.queryAll(filtro, order);
            listaTurma.clear();
            if (lista != null && !lista.isEmpty()){
                listaTurma.addAll(lista);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Erro ao buscar Turma");
        }
        atualizaTabela();
    }
     private void limpa() {
        tbCodigo.clearSelection();
        setTurma(null);
    }
    

    @Override
    public void novo() {
        UpdateTurma update = new UpdateTurma(null, true);
        update.novo();
        update.setVisible(true);
        filtro();
        limpa();
    }

    @Override
    public void editar() {
        UpdateTurma update = new UpdateTurma(null, true);
        update.editar(getTurma());
        update.setVisible(true);
        filtro();
        limpa();
    }

    @Override
    public void excluir() throws Exception {
        int result = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir o registro selecionado?", "Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (result == JOptionPane.YES_OPTION) {
            if (getTurma()!= null) {
                TurmaBD turBD = new TurmaBD();
                TurmaBD.TurmaDetBD detBD = new TurmaBD.TurmaDetBD();
                getTurma().setListaTurmaDet(detBD.queryAllByTurma(getTurma().getId()));
                if (turBD.delete(getTurma())) {
                    JOptionPane.showMessageDialog(null, "Registro excluído com sucesso.");
                }
                filtro();
                limpa();
            }
        }
        getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    
    private Curso abreConsultaCurso() {
        ConsultaDialog consulta = new ConsultaDialog(null, true);
        consulta.vinculaCurso();
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
        filtro();
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
            } catch (Exception e) {
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
        filtro();
    }
    
    private void codeSelected() {
        Integer codigo = Integer.parseInt(tfCodigo.getText());
        for (int i = 0; i < listaTurma.size(); i++) {
            if (codigo.equals(listaTurma.get(i).getId())) {
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
        tfCodigoCurso = new javax.swing.JTextField();
        btBuscaCurso = new javax.swing.JButton();
        tfNomeCurso = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfCodigoPeriodo = new javax.swing.JTextField();
        btBuscaPeriodo = new javax.swing.JButton();
        tfDescricaoPeriodo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cbFase = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cbOrdem = new javax.swing.JComboBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCodigo = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        tfCodigo = new javax.swing.JTextField();
        btFechar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btSelecionar = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros"));

        jLabel1.setText("Curso:");

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

        jLabel2.setText("Período:");

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

        jLabel3.setText("Fase:");

        cbFase.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Todas" }));
        cbFase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFaseActionPerformed(evt);
            }
        });

        jLabel4.setText("Status:");

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aberto", "Encerrado", "Todos" }));
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });

        jLabel6.setText("Ordem:");

        cbOrdem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Código", "Curso", "Período", "Fase" }));
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(tfCodigoCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btBuscaCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNomeCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfCodigoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btBuscaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfDescricaoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfNomeCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBuscaCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(tfCodigoCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfCodigoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(btBuscaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfDescricaoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cbOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbCodigo.setModel(new br.com.sysprod.model.TableTurma(listaTurma));
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
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

        jButton1.setText("Novo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
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
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btFechar)
                    .addComponent(btExcluir)
                    .addComponent(btEditar)
                    .addComponent(jButton1)
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
        filtro();
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
        filtro();
    }//GEN-LAST:event_btBuscaPeriodoActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        limpa();
    }//GEN-LAST:event_btFecharActionPerformed

    private void cbFaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFaseActionPerformed
        filtro();
    }//GEN-LAST:event_cbFaseActionPerformed

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        filtro();
    }//GEN-LAST:event_cbStatusActionPerformed

    private void cbOrdemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOrdemActionPerformed
        filtro();
    }//GEN-LAST:event_cbOrdemActionPerformed

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
            ErrorVerification.ErrDetalhe(e, "Erro ao excluir turma");
        }
        filtro();
    }//GEN-LAST:event_btExcluirActionPerformed

    private void tfCodigoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoFocusLost
        codeSelected();
    }//GEN-LAST:event_tfCodigoFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBuscaCurso;
    private javax.swing.JButton btBuscaPeriodo;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    public javax.swing.JButton btFechar;
    public javax.swing.JButton btSelecionar;
    private javax.swing.JComboBox cbFase;
    private javax.swing.JComboBox cbOrdem;
    private javax.swing.JComboBox cbStatus;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbCodigo;
    private javax.swing.JTextField tfCodigo;
    private javax.swing.JTextField tfCodigoCurso;
    private javax.swing.JTextField tfCodigoPeriodo;
    private javax.swing.JTextField tfDescricaoPeriodo;
    private javax.swing.JTextField tfNomeCurso;
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
                Integer.parseInt(tfCodigoCurso.getText());
            } catch (Exception e) {
                tfCodigoCurso.setText("000000");
                tfCodigoCurso.selectAll();
                return false;
            }
            
            try {
                Integer.parseInt(tfCodigoPeriodo.getText());
            } catch (Exception e) {
                tfCodigoPeriodo.setText("000000");
                tfCodigoPeriodo.selectAll();
                return false;
            }
            return true;
        }
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
                    int rowSelected = lsm.getMinSelectionIndex();
                    int codSelected = Integer.parseInt(tbCodigo.getValueAt(rowSelected, 0).toString());
                    TurmaBD turBD = new TurmaBD();
                    try {
                        setTurma(turBD.find(codSelected, false));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        ErrorVerification.ErrDetalhe(ex, "Erro ao buscar turma.");
                    }
                }
            }
        });
    }
    
    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

}

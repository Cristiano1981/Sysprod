package br.com.sysprod.update;

import br.com.sysprod.Global;
import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.CursoBD;
import br.com.sysprod.dao.PeriodoBD;
import br.com.sysprod.dao.TurmaBD;
import br.com.sysprod.dao.UsuarioBD;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.model.TableTurmaDet;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Curso;
import br.com.sysprod.vo.Periodo;
import br.com.sysprod.vo.Turma;
import br.com.sysprod.vo.TurmaDet;
import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class UpdateTurma extends javax.swing.JDialog implements Formulario{

    private List<TurmaDet> listaTurmaDet = new ArrayList<TurmaDet>();
    private List<TurmaDet> listaTurmaDetExcluir = new ArrayList<TurmaDet>();
    private TurmaDet turmaDet;
    
    public UpdateTurma(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initForm();
        initComponent();
        propriedadeTable();
        tableSelection();
    }
    
   @Override
    public void initForm() {
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void initComponent() {
        tfCodigoCurso.setToolTipText("Digite um código ou clique no ícone ao lado para busca");
        tfNomeCurso.setToolTipText("Nome do curso");
        tfCodigoPeriodo.setToolTipText("Digite um codigo ou clique no ícone ao lado para busca");
        tfDescricaoPeriodo.setToolTipText("Descrição do período");
        tfQtdMaximoAcerto.setToolTipText("Quantidade de perguntas que a turma deverá acertar para obter nota máxima.");
        tfCodigoCurso.setInputVerifier(new MyVerify());
        tfCodigoPeriodo.setInputVerifier(new MyVerify());
        tfQtdMaximoAcerto.setInputVerifier(new MyVerify());
        ((DefaultCaret) tfObs.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    @Override
    public boolean validateForm() {
        Component[] c = {tfCodigoCurso, tfCodigoPeriodo, tfQtdMaximoAcerto};
        return Utils.validaObrigatorios(c);
    }

    @Override
    public void end() {
        dispose();
    }
    
    public void propriedadeTable() {
        tbAcademicos.getColumnModel().getColumn(0).setPreferredWidth(10);
        tbAcademicos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbAcademicos.getTableHeader().setReorderingAllowed(false);
        tbAcademicos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void atualizaTabela() {
        labelQuantidade.setText("Quantidade de acadêmicos: "+listaTurmaDet.size());
        ((AbstractTableModel) tbAcademicos.getModel()).fireTableDataChanged();
    }
    
    public void novo(){
        setTitle("Nova Turma");
        Integer codigo = null;
        TurmaBD turBD = new TurmaBD();
        try {
            codigo = turBD.nextCode();
            tfCodigo.setText(Utils.codigoFormatado(codigo));
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro buscar próximo código da turma");
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
    
    private void salvar(){
        Turma turma     = new Turma();
        Curso curso     = new Curso();
        Periodo periodo = new Periodo();
        turma.setId(Integer.parseInt(tfCodigo.getText()));
        turma.setFase(Integer.parseInt(cbFase.getSelectedItem().toString()));
        curso.setId(Integer.parseInt(tfCodigoCurso.getText()));
        turma.setCurso(curso);
        turma.setUnidade(Global.getUnidade());
        periodo.setId(Integer.parseInt(tfCodigoPeriodo.getText()));
        turma.setPeriodo(periodo);
        turma.setObs(tfObs.getText().trim().toUpperCase());
        turma.setQtdMaximaAcertos(Integer.parseInt(tfQtdMaximoAcerto.getText()));
        turma.setListaTurmaDet(listaTurmaDet);
        turma.setListaTurmaDetExcluir(listaTurmaDetExcluir);
        TurmaBD t = new TurmaBD();
        try {
            if (t.saveOrUpdate(turma)){
                end();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro ao gravar turma");
        }
    }
    
    private void novoTurmaDet(){
        UpdateTurmaDet up = new UpdateTurmaDet(null, true, listaTurmaDet);
        up.setTitle("Novo acadêmico");
        up.setVisible(true);
        atualizaTabela();
    }
    
    public void editar(Turma turma){
        setTitle("Editar turma");
        tfCodigo.setText(Utils.codigoFormatado(turma.getId()));
        tfCodigoCurso.setText(Utils.codigoFormatado(turma.getCurso().getId()));
        tfNomeCurso.setText(turma.getCurso().getDescricao());
        tfCodigoPeriodo.setText(Utils.codigoFormatado(turma.getPeriodo().getId()));
        tfDescricaoPeriodo.setText(turma.getPeriodo().getDescricao());
        tfObs.setText(turma.getObs());
        cbFase.setSelectedItem(turma.getFase().toString());
        tfQtdMaximoAcerto.setText(Integer.toString(turma.getQtdMaximaAcertos()));
        List<TurmaDet> listaTurmaDet = null;
        if (turma.getListaTurmaDet() == null){
            try {
                TurmaBD.TurmaDetBD turmaDetBD = new TurmaBD.TurmaDetBD();
                listaTurmaDet = turmaDetBD.queryAllByTurma(turma.getId());
            } catch (SQLException e) {
                e.printStackTrace();
                ErrorVerification.ErrDetalhe(e, "Erro ao buscar acadêmicos da turma");
            }
        }else{
            listaTurmaDet = turma.getListaTurmaDet();
        }
        if (listaTurmaDet != null && !listaTurmaDet.isEmpty()){
            this.listaTurmaDet.addAll(listaTurmaDet);
        }
        atualizaTabela();
    }
    
    private void excluir(){
        if (getTurmaDet() != null){
            int result = JOptionPane.showConfirmDialog(null, "Deseja excluir o registro selecionado?", "Atenção!", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.OK_OPTION){
                listaTurmaDetExcluir.add(getTurmaDet());
                listaTurmaDet.remove(getTurmaDet());
                setTurmaDet(null);
                atualizaTabela();
            }
        }
    }
    
    private void editarTurmaDet(){
        if (getTurmaDet() != null){
            UpdateTurmaDet det = new UpdateTurmaDet(null, true, listaTurmaDet);
            det.editar(getTurmaDet());
            det.setVisible(true);
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
        tfCodigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        tfCodigoCurso = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfCodigoPeriodo = new javax.swing.JTextField();
        btBuscaPeriodo = new javax.swing.JButton();
        btBuscaCurso = new javax.swing.JButton();
        tfNomeCurso = new javax.swing.JTextField();
        tfDescricaoPeriodo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbFase = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tfObs = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        tfQtdMaximoAcerto = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbAcademicos = new javax.swing.JTable();
        btExcluir = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btNovo = new javax.swing.JButton();
        labelQuantidade = new javax.swing.JLabel();
        btFechar = new javax.swing.JButton();
        btGravar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações Turma"));

        tfCodigo.setEditable(false);
        tfCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigo.setText("000000");

        jLabel2.setText("Código:");

        jLabel1.setText("Curso:");

        tfCodigoCurso.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigoCurso.setText("000000");
        tfCodigoCurso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfCodigoCursoFocusLost(evt);
            }
        });

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

        btBuscaCurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBuscaCurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaCursoActionPerformed(evt);
            }
        });

        tfNomeCurso.setEditable(false);

        tfDescricaoPeriodo.setEditable(false);

        jLabel4.setText("Fase:");

        cbFase.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

        tfObs.setColumns(20);
        tfObs.setRows(5);
        jScrollPane1.setViewportView(tfObs);

        jLabel5.setText("Acertos Máximo:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(40, 40, 40))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(tfCodigoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btBuscaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(tfCodigo)
                            .addComponent(tfCodigoCurso, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btBuscaCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfNomeCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tfQtdMaximoAcerto, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(103, 103, 103))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tfDescricaoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(cbFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(tfQtdMaximoAcerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfNomeCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btBuscaCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfCodigoCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tfCodigoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addComponent(btBuscaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfDescricaoPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Informações", jPanel3);

        tbAcademicos.setModel(new br.com.sysprod.model.TableTurmaDet(listaTurmaDet));
        jScrollPane2.setViewportView(tbAcademicos);

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

        labelQuantidade.setText("Quantidade:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelQuantidade)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btNovo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btExcluir)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btExcluir)
                            .addComponent(btEditar)
                            .addComponent(btNovo))
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelQuantidade)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Acadêmicos", jPanel4);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btFechar.setText("Fechar");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        btGravar.setText("Gravar");
        btGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGravarActionPerformed(evt);
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
                        .addComponent(btGravar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btFechar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btFechar)
                    .addComponent(btGravar))
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

    private void tfCodigoCursoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoCursoFocusLost
        carregaCurso();
    }//GEN-LAST:event_tfCodigoCursoFocusLost

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

    private void btGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGravarActionPerformed
        if (validateForm()){
            salvar();
        }
        
    }//GEN-LAST:event_btGravarActionPerformed

    private void btNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNovoActionPerformed
        novoTurmaDet();
        
    }//GEN-LAST:event_btNovoActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        end();
    }//GEN-LAST:event_btFecharActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        editarTurmaDet();
    }//GEN-LAST:event_btEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBuscaCurso;
    private javax.swing.JButton btBuscaPeriodo;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btGravar;
    private javax.swing.JButton btNovo;
    private javax.swing.JComboBox cbFase;
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
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelQuantidade;
    private javax.swing.JTable tbAcademicos;
    private javax.swing.JTextField tfCodigo;
    private javax.swing.JTextField tfCodigoCurso;
    private javax.swing.JTextField tfCodigoPeriodo;
    private javax.swing.JTextField tfDescricaoPeriodo;
    private javax.swing.JTextField tfNomeCurso;
    private javax.swing.JTextArea tfObs;
    private javax.swing.JTextField tfQtdMaximoAcerto;
    // End of variables declaration//GEN-END:variables

    private void tableSelection() {
        tbAcademicos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    int rowSelected = lsm.getMinSelectionIndex();
                    setTurmaDet(listaTurmaDet.get(rowSelected));
                }
            }
        });
    }
    
    
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
                Integer.parseInt(tfQtdMaximoAcerto.getText());
            } catch (NumberFormatException e) {
                tfQtdMaximoAcerto.setText("00");
                tfQtdMaximoAcerto.selectAll();
                return false;
            }
            
            return true;
        }
    }
    
    public TurmaDet getTurmaDet() {
        return turmaDet;
    }

    public void setTurmaDet(TurmaDet turmaDet) {
        this.turmaDet = turmaDet;
    }



}

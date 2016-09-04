package br.com.sysprod.update;

import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.PerguntaBD;
import br.com.sysprod.dao.ProvaDetAcademicoBD;
import br.com.sysprod.dao.ProvaDetItemBD;
import br.com.sysprod.dao.TurmaBD;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.model.TableProvaDetAcademico;
import br.com.sysprod.model.TableProvaDetItem;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Pergunta;
import br.com.sysprod.vo.ProvaDet;
import br.com.sysprod.vo.ProvaDetAcademico;
import br.com.sysprod.vo.ProvaDetItem;
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
public final class UpdateProvaDet extends javax.swing.JDialog implements Formulario{

    private List<ProvaDet>          listaProvaDet                 = null;
    private List<ProvaDetItem>      listaProvaDetItem             = new ArrayList<ProvaDetItem>();
    private List<ProvaDetItem>      listaProvaDetItemExcluir      = new ArrayList<ProvaDetItem>();
    private List<ProvaDetAcademico> listaProvaDetAcademico        = new ArrayList<ProvaDetAcademico>();
    private List<ProvaDetAcademico> listaProvaDetAcademicoExcluir = new ArrayList<ProvaDetAcademico>();
    private Integer                 idProva                       = 0;
    private Integer                 idTurma                       = 0;
    private Integer                 index                         = 0;
    private ProvaDet                provaDetAtual                 = null;
    private ProvaDetItem            provaDetItem                  = null;
    private ProvaDetAcademico       provaDetAcademico             = null;
    
    
    public UpdateProvaDet(java.awt.Frame parent, boolean modal, List<ProvaDet> listaProvaDet, Integer idProva, Integer idTurma) {
        super(parent, modal);
        this.listaProvaDet = listaProvaDet;
        this.idProva       = idProva;
        this.idTurma       = idTurma;
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
        tfCodigoProvaDet.setToolTipText("Código da pergunta. (Sequencial, gerado pelo próprio sistema);");
        tfCodigoProva.setToolTipText("Código da prova;");
        tfCodigoPergunta.setToolTipText("Digite um código da pergunta ou clique no ícone ao lado para abrir tela de busca;");
        btBusca.setToolTipText("Clique no ícone para abrir tela de consulta de perguntas;");
        tfDescricaoPergunta.setToolTipText("Descrição da pergunta;");
        tfCodigoProva.setText(Utils.codigoFormatado(idProva));
        tfCodigoPergunta.setInputVerifier(new MyVerify());
        ((DefaultCaret) tfDescricaoPergunta.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }
    
    private void adicionaAcademicos(){
        Turma turma = null;
        try {
            turma = new TurmaBD().find(idTurma, true);
        } catch (SQLException e) {
            ErrorVerification.ErrDetalhe(e, "Erro ao buscar turma /UpdateProvaDet");
        }
        if (turma != null && turma.getListaTurmaDet() != null){
            for(TurmaDet det : turma.getListaTurmaDet()){
                ProvaDetAcademico acad = new ProvaDetAcademico();
                acad.setId(0);
                acad.setAcademico(det.getAcademico());
                if (!listaProvaDetAcademico.contains(acad)){
                    listaProvaDetAcademico.add(acad);
                }
            }
        }
        atualizaTabela();
    }
    

    @Override
    public boolean validateForm() {
        Component[] c = {tfCodigoPergunta};
        return Utils.validaObrigatorios(c);
    }

    @Override
    public void end() {
        dispose();
    }
    
    private void propriedadeTable(){
        tbAlternativas.getColumnModel().getColumn(0).setPreferredWidth(20);
        tbAlternativas.getColumnModel().getColumn(1).setPreferredWidth(450);
        tbAlternativas.getColumnModel().getColumn(2).setPreferredWidth(20);
        tbAlternativas.setAutoCreateRowSorter(true);
        tbAlternativas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void atualizaTabela() {
        labelAlternativa.setText("Quantidade: "+listaProvaDetItem.size());
        ((AbstractTableModel) tbAlternativas.getModel()).fireTableDataChanged();
        ((AbstractTableModel) tbAcademicos.getModel()).fireTableDataChanged();
    }
    
    private Pergunta abreConsultaPergunta() {
        ConsultaDialog consulta = new ConsultaDialog(null, true);
        consulta.vinculaPergunta();
        consulta.getBrowsePergunta().cbTipo.setSelectedItem("Pergunta");
        consulta.initForm();
        consulta.setVisible(true);
        return consulta.getBrowsePergunta().getPergunta();
    }
     
   private void fillPergunta(Pergunta c) {
       if (c == null){
           tfCodigoPergunta.setText("000000");
            tfDescricaoPergunta.setText("");
       }else{
            tfCodigoPergunta.setText(Utils.codigoFormatado(c.getId()));
            tfDescricaoPergunta.setText(c.getDescricao());
       }
    }

    private void carregaPergunta() {
        Integer codigo = Integer.parseInt(tfCodigoPergunta.getText());
        if (codigo > 0) {
            PerguntaBD perBD = new PerguntaBD();
            Pergunta pergunta = null;
            try {
                pergunta = perBD.find(codigo);
            } catch (SQLException e) {
                ErrorVerification.ErrDetalhe(e, "Erro ao buscar pergunta");
            }
            if (pergunta != null) {
                if (pergunta.getTipo().equals("A")){
                    JOptionPane.showMessageDialog(null, "A questão está cadastrada como 'Item de uma Pergunta.' Favor, selecione outra!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                    fillPergunta(null);
                    return;
                }
                fillPergunta(pergunta);
            } else {
                Pergunta c = abreConsultaPergunta();
                if (c != null) {
                    if (pergunta.getTipo().equals("A")){
                    JOptionPane.showMessageDialog(null, "A questão está cadastrada como 'Item de uma Pergunta.' Favor, selecione outra!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                    fillPergunta(null);
                    return;
                }
                    fillPergunta(c);
                } else {
                    fillPergunta(null);
                    tfCodigoPergunta.selectAll();
                }
            }
        } else {
            fillPergunta(null);
            tfCodigoPergunta.selectAll();
        }
    }
    
    private void salvar(){
        ProvaDet provaDet = new ProvaDet();
        provaDet.setId(Integer.parseInt(tfCodigoProvaDet.getText()));
        provaDet.setListaProvaDetItem(listaProvaDetItem);
        provaDet.setListaProvaDetItemExcluir(listaProvaDetItemExcluir);
        provaDet.setListaProvaDetAcademico(listaProvaDetAcademico);
        provaDet.setListaProvaDetAcademicoExcluir(listaProvaDetAcademicoExcluir);
        try {
            provaDet.setPergunta(new PerguntaBD().find(Integer.parseInt(tfCodigoPergunta.getText())));
        } catch (SQLException e) {
            ErrorVerification.ErrDetalhe(e, "Erro ao buscar pegunta.");
        }
        //NÃO MEXER. DAQUI PRA BAIXO É GAMBIARRA. CRISTIANO 01/02/2015
        if (provaDetAtual != null && !provaDetAtual.equals(provaDet)){
            listaProvaDet.remove(provaDetAtual);
        }
        if (provaDetAtual != null && provaDetAtual.equals(provaDet)){          
           
        }else{
            listaProvaDet.add(index, provaDet);
        }
        //FIM
        end();
    }
    
    public void editar(ProvaDet provaDet, Integer index){
        this.provaDetAtual     = provaDet;
        this.index             = index;
        tfCodigoProvaDet.setText(Utils.codigoFormatado(provaDet.getId()));
        tfCodigoPergunta.setText(Utils.codigoFormatado(provaDet.getPergunta().getId()));
        tfDescricaoPergunta.setText(provaDet.getPergunta().getDescricao());
        List<ProvaDetItem>      listaAuxItem      = null;
        List<ProvaDetAcademico> listaAuxAcademico = null;
        if (provaDet.getListaProvaDetItem() == null){
            ProvaDetItemBD provaDetItemBD = new ProvaDetItemBD();
            try {
                listaAuxItem = provaDetItemBD.queryAllByProvaDet(provaDet.getId());
            } catch (SQLException e) {
                ErrorVerification.ErrDetalhe(e, "Erro ao buscar lista de alternativas");
                e.printStackTrace();
            }
        }else{
            listaAuxItem = provaDet.getListaProvaDetItem();
        }
        if(provaDet.getListaProvaDetAcademico() == null){
            ProvaDetAcademicoBD provaDetAcademicoBD = new ProvaDetAcademicoBD();
            try {
                listaAuxAcademico = provaDetAcademicoBD.queryAllByProvaDet(provaDet.getId());
            } catch (SQLException e) {
                ErrorVerification.ErrDetalhe(e, "Erro ao buscar lista de acadêmicos");
                e.printStackTrace();
            }
        }else{
            listaAuxAcademico = provaDet.getListaProvaDetAcademico();
        }
        if (listaAuxItem != null && !listaAuxItem.isEmpty()){
            listaProvaDetItem.addAll(listaAuxItem);
        }
        if (listaAuxAcademico != null && !listaAuxAcademico.isEmpty()){
            listaProvaDetAcademico.addAll(listaAuxAcademico);
        }
        atualizaTabela();
    }
    
    private void excluirProvaDetItem(){
        if (getProvaDetItem() != null){
            int result = JOptionPane.showConfirmDialog(null, "Deseja excluir o registro selecionado?", "Atenção!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION){
                if (getProvaDetItem().getId() != null || getProvaDetItem().getId() > 0){
                    listaProvaDetItemExcluir.add(getProvaDetItem());
                }
                listaProvaDetItem.remove(getProvaDetItem());
                setProvaDetItem(null);
                atualizaTabela();
            }
        }
    }
    
    private void novaProvaDetItem(){
        UpdateProvaDetItem update = new UpdateProvaDetItem(null, true, listaProvaDetItem);
        update.setVisible(true);
        atualizaTabela();
    }
    
    private void editarProvaDetAcademico(){
        if (getProvaDetAcademico() != null){
            UpdateProvaDetAcademico update = new UpdateProvaDetAcademico(null, true, listaProvaDetAcademico);
            update.editar(getProvaDetAcademico());
            update.setVisible(true);
            atualizaTabela();
        }
    }
    
    private void novoProvaDetAcademico(){
        UpdateProvaDetAcademico update = new UpdateProvaDetAcademico(null, true, listaProvaDetAcademico);
        update.setVisible(true);
        atualizaTabela();
    }
    
     private void excluirProvaDetAcademico(){
        if (getProvaDetAcademico() != null){
            int result = JOptionPane.showConfirmDialog(null, "Deseja excluir o registro selecionado?", "Atenção!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION){
                listaProvaDetAcademicoExcluir.add(getProvaDetAcademico());
                listaProvaDetAcademico.remove(getProvaDetAcademico());
                setProvaDetAcademico(null);
                atualizaTabela();
            }
        }
    }
     
    
    private boolean verificaAlternativas(){
        boolean letraA      = false;
        boolean letraB      = false;
        boolean letraC      = false;
        boolean letraD      = false;
        boolean letraE      = false;
        Integer qtdLetraA   = 0;
        Integer qtdLetraB   = 0;
        Integer qtdLetraC   = 0;
        Integer qtdLetraD   = 0;
        Integer qtdLetraE   = 0;
        Integer qtdCorretas = 0 ;
        for (ProvaDetItem item : listaProvaDetItem) {
            switch (item.getLetra()) {
                case "A":
                    if (item.isCorreto()){
                        qtdCorretas++;
                    }
                    letraA = true;
                    qtdLetraA++;
                    break;
                case "B":
                    if (item.isCorreto()){
                        qtdCorretas++;
                    }
                    letraB = true;
                    qtdLetraB++;
                    break;
                case "C":
                    if (item.isCorreto()){
                        qtdCorretas++;
                    }
                    letraC = true;
                    qtdLetraC++;
                    break;
                case "D":
                    if (item.isCorreto()){
                        qtdCorretas++;
                    }
                    letraD = true;
                    qtdLetraD++;
                    break;
                default:
                    if (item.isCorreto()){
                        qtdCorretas++;
                    }
                    letraE = true;
                    qtdLetraE++;
                    break;
            }
        }
        if (letraA && letraB && letraC && letraD && letraD && letraE){
            return true;
        }else{
            StringBuilder sb = new StringBuilder();
            if (qtdLetraA > 1){
                sb.append("Existem "+qtdLetraA+" letras 'A';\n");
            }
            if (qtdLetraB > 1){
                sb.append("Existem "+qtdLetraB+" letras 'B';\n");
            }
            if (qtdLetraC > 1){
                sb.append("Existem "+qtdLetraC+" letras 'C';\n");
            }
            if (qtdLetraD > 1){
                sb.append("Existem "+qtdLetraD+" letras 'D';\n");
            }
            if (qtdLetraE > 1){
                sb.append("Existem "+qtdLetraE+" letras 'E';\n");
            }
            if (qtdCorretas != 1){
                sb.append("Deve existir somente uma alternativa correta;");
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Atenção", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }
    
    private void verificaAcerto(){
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfCodigoProvaDet = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfCodigoProva = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfCodigoPergunta = new javax.swing.JTextField();
        btBusca = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tfDescricaoPergunta = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbAlternativas = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        labelAlternativa = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbAcademicos = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        btGravar = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações Pergunta"));

        jLabel1.setText("Código:");

        tfCodigoProvaDet.setEditable(false);
        tfCodigoProvaDet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigoProvaDet.setText("000000");

        jLabel2.setText("Prova:");

        tfCodigoProva.setEditable(false);
        tfCodigoProva.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigoProva.setText("000000");

        jLabel3.setText("Pergunta:");

        tfCodigoPergunta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigoPergunta.setText("000000");
        tfCodigoPergunta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfCodigoPerguntaActionPerformed(evt);
            }
        });
        tfCodigoPergunta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfCodigoPerguntaFocusLost(evt);
            }
        });

        btBusca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaActionPerformed(evt);
            }
        });

        tfDescricaoPergunta.setEditable(false);
        tfDescricaoPergunta.setColumns(20);
        tfDescricaoPergunta.setRows(5);
        jScrollPane2.setViewportView(tfDescricaoPergunta);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(tfCodigoProvaDet, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(tfCodigoProva, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(289, 289, 289))
                            .addComponent(jScrollPane2)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(23, 23, 23)
                        .addComponent(tfCodigoPergunta, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfCodigoProvaDet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(tfCodigoProva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(tfCodigoPergunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Dados", jPanel3);

        tbAlternativas.setModel(new TableProvaDetItem(listaProvaDetItem));
        jScrollPane1.setViewportView(tbAlternativas);

        jButton1.setText("Excluir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Novo");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        labelAlternativa.setText("Quantidade:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelAlternativa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(labelAlternativa))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Alternativas", jPanel4);

        tbAcademicos.setModel(new TableProvaDetAcademico(this.listaProvaDetAcademico));
        jScrollPane3.setViewportView(tbAcademicos);

        jButton6.setText("Excluir");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Editar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Novo");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Acadêmicos", jPanel5);

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
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jButton4.setText("Carrega Acadêmicos");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
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
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btFechar)
                    .addComponent(btGravar)
                    .addComponent(jButton4))
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

    private void tfCodigoPerguntaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoPerguntaFocusLost
        carregaPergunta();
    }//GEN-LAST:event_tfCodigoPerguntaFocusLost

    private void btBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaActionPerformed
        Pergunta c = abreConsultaPergunta();
        if (c != null) {
            fillPergunta(c);
        } else {
            fillPergunta(null);
            tfCodigoPergunta.selectAll();
        }
    }//GEN-LAST:event_btBuscaActionPerformed

    private void tfCodigoPerguntaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfCodigoPerguntaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfCodigoPerguntaActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        int request = JOptionPane.showConfirmDialog(null, "Deseja realmente sair? As alterações efetuadas não serão gravadas.", "ATENÇÃO!", JOptionPane.YES_NO_OPTION);
        if (request == JOptionPane.YES_OPTION){
            end();
        }
    }//GEN-LAST:event_btFecharActionPerformed

    private void btGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGravarActionPerformed
        if (validateForm()){
            if (verificaAlternativas()){
                salvar();
            }
        }
    }//GEN-LAST:event_btGravarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        excluirProvaDetItem();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        novaProvaDetItem();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        editarProvaDetAcademico();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        novoProvaDetAcademico();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        excluirProvaDetAcademico();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        adicionaAcademicos();
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBusca;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btGravar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelAlternativa;
    private javax.swing.JTable tbAcademicos;
    private javax.swing.JTable tbAlternativas;
    private javax.swing.JTextField tfCodigoPergunta;
    private javax.swing.JTextField tfCodigoProva;
    private javax.swing.JTextField tfCodigoProvaDet;
    private javax.swing.JTextArea tfDescricaoPergunta;
    // End of variables declaration//GEN-END:variables

    private void tableSelection() {
        tbAlternativas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    int rowSelected = lsm.getMinSelectionIndex();
                    setProvaDetItem(listaProvaDetItem.get(rowSelected));
                }
            }
        });
         tbAcademicos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (!lsm.isSelectionEmpty()) {
                    int rowSelected = lsm.getMinSelectionIndex();
                    setProvaDetAcademico(listaProvaDetAcademico.get(rowSelected));
                }
            }
        });
    }

    public ProvaDetItem getProvaDetItem() {
        return provaDetItem;
    }

    public void setProvaDetItem(ProvaDetItem provaDetItem) {
        this.provaDetItem = provaDetItem;
    }

    public ProvaDetAcademico getProvaDetAcademico() {
        return provaDetAcademico;
    }

    public void setProvaDetAcademico(ProvaDetAcademico provaDetAcademico) {
        this.provaDetAcademico = provaDetAcademico;
    }
    
  private class MyVerify extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            try {
                Integer.parseInt(tfCodigoPergunta.getText());
            } catch (Exception e) {
                tfCodigoPergunta.setText("000000");
                tfCodigoPergunta.selectAll();
                return false;
            }
             return true;
        }
    }
}
package br.com.sysprod.update;

import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.AcademicoBD;
import br.com.sysprod.dao.DisciplinaBD;
import br.com.sysprod.dao.PerguntaBD;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Disciplina;
import br.com.sysprod.vo.Pergunta;
import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.text.DefaultCaret;

/**
 * @author Cristiano Bombazar
 */
public final class UpdatePergunta extends javax.swing.JDialog implements Formulario{

    public UpdatePergunta(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initComponent();
        initForm();
    }
    
     public void novo(){
        setTitle("Nova pergunta");
        Integer codigo = null;
        PerguntaBD perBD = new PerguntaBD();
        try {
            codigo = perBD.nextCode();
            tfCodigo.setText(Utils.codigoFormatado(codigo));
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro buscar próximo código da pergunta");
        }
    }
    
        @Override
    public void initForm() {
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void initComponent() {
        tfCodigo.setToolTipText("Código da pergunta");
        tfCodigoDisciplina.setToolTipText("Digite um código de disicplina ou clique no ícone ao lado para buscar");
        btBusca.setToolTipText("Clica para abrir a consulta de disciplina");
        tfNomeDisciplina.setToolTipText("Nome da disciplina");
        tfDescricao.setToolTipText("Digite a pergunte...");
        ((DefaultCaret) tfDescricao.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    @Override
    public boolean validateForm() {
        Component[] c = {tfCodigoDisciplina, tfDescricao};
        return Utils.validaObrigatorios(c);
    }

    @Override
    public void end() {
        dispose();
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
    }
    
    private void salvar(){
        Pergunta p = new Pergunta();
        Disciplina dis = new Disciplina();
        PerguntaBD perBD = new PerguntaBD();
        p.setId(Integer.parseInt(tfCodigo.getText()));
        p.setAtivo(chkAtivo.isSelected());
        p.setDescricao(tfDescricao.getText());
        p.setTipo(cbTipo.getSelectedItem().toString().substring(0, 1));
        dis.setId(Integer.parseInt(tfCodigoDisciplina.getText()));
        p.setDisciplina(dis);
        try {
            if (perBD.saveOrUpdate(p)){
                end();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Erro ao salvar pergunta");
        }
    }
    
    public void editar(Pergunta pergunta){
        setTitle("Editar pergunta");
        tfCodigo.setText(Utils.codigoFormatado(pergunta.getId()));
        tfCodigoDisciplina.setText(Utils.codigoFormatado(pergunta.getDisciplina().getId()));
        tfDescricao.setText(pergunta.getDescricao());
        tfNomeDisciplina.setText(pergunta.getDisciplina().getNome());
        if (pergunta.getTipo().equals("P")){
            cbTipo.setSelectedIndex(0);
        }else{
            cbTipo.setSelectedIndex(1);
        }
        chkAtivo.setSelected(pergunta.isAtivo());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tfCodigoDisciplina = new javax.swing.JTextField();
        btBusca = new javax.swing.JButton();
        tfNomeDisciplina = new javax.swing.JTextField();
        tfCodigo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tfDescricao = new javax.swing.JTextArea();
        chkAtivo = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        cbTipo = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações pergunta"));

        jLabel1.setText("Código:");

        jLabel2.setText("Disciplina:");

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

        tfCodigo.setEditable(false);
        tfCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigo.setText("000000");

        jLabel3.setText("Descrição:");

        tfDescricao.setColumns(20);
        tfDescricao.setRows(5);
        jScrollPane1.setViewportView(tfDescricao);

        chkAtivo.setSelected(true);
        chkAtivo.setText("Ativo");

        jLabel4.setText("Tipo:");

        cbTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pergunta", "Alternativa" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(468, 468, 468))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(tfCodigoDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(tfNomeDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(chkAtivo)))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkAtivo)
                    .addComponent(jLabel4)
                    .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfNomeDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(tfCodigoDisciplina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
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
    }//GEN-LAST:event_btBuscaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        end();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (validateForm()){
            salvar();
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBusca;
    private javax.swing.JComboBox cbTipo;
    private javax.swing.JCheckBox chkAtivo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField tfCodigo;
    private javax.swing.JTextField tfCodigoDisciplina;
    private javax.swing.JTextArea tfDescricao;
    private javax.swing.JTextField tfNomeDisciplina;
    // End of variables declaration//GEN-END:variables

}

package br.com.sysprod.update;

import br.com.sysprod.Global;
import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.AcademicoBD;
import br.com.sysprod.dao.CursoBD;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Academico;
import br.com.sysprod.vo.Curso;
import java.awt.Component;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * @author Cristiano Bombazar
 */
public final class UpdateAcademico extends javax.swing.JDialog implements Formulario{
    
    
    public UpdateAcademico(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initComponent();
        initForm();
    }

       @Override
    public void initForm() {
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void initComponent() {
        tfCodigo.setToolTipText("Código do acadêmico");
        tfCodigoCurso.setToolTipText("Código do curso");
        tfNome.setToolTipText("Nome do curso");
        chkAtivo.setToolTipText("Status do acadêmico");
        tfCodigoCurso.setInputVerifier(new MyVerify());
        tfNome.requestFocusInWindow();
    }

    @Override
    public boolean validateForm() {
        Component[] c = {tfNome, tfCodigoCurso};
        return Utils.validaObrigatorios(c);
    }

    @Override
    public void end() {
        dispose();
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
    }
    
    public void novo(){
        setTitle("Nova acadêmico");
        Integer codigo = null;
        AcademicoBD acadBD = new AcademicoBD();
        try {
            codigo = acadBD.nextCode();
            tfCodigo.setText(Utils.codigoFormatado(codigo));
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro buscar próximo código do acadêmico");
        }
    }
    
    public void editar(Academico a){
        setTitle("Editar acadêmico");
        tfId.setText(Utils.codigoFormatado(a.getId()));
        tfCodigo.setText(Utils.codigoFormatado(a.getCodigo()));
        tfNome.setText(a.getNome());
        tfCodigoCurso.setText(Utils.codigoFormatado(a.getCurso().getId()));
        tfNomeCurso.setText(a.getCurso().getDescricao());
        chkAtivo.setSelected(a.isAtivo());
    }
    
    private void salvar(){
        Academico acad = new Academico();
        AcademicoBD acadBD = new AcademicoBD();
        acad.setId(Integer.parseInt(tfId.getText()));
        acad.setCodigo(Integer.parseInt(tfCodigo.getText()));
        acad.setNome(tfNome.getText().trim().toUpperCase());
        acad.setAtivo(chkAtivo.isSelected());
        acad.setUnidade(Global.getUnidade());
        Integer codCurso = Integer.parseInt(tfCodigoCurso.getText());
        try {
            acad.setCurso(new CursoBD().find(codCurso));        
            if (acadBD.saveOrUpdate(acad)){
                end();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro ao gravar acadêmico");
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tfCodigo = new javax.swing.JTextField();
        chkAtivo = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        tfNome = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfCodigoCurso = new javax.swing.JTextField();
        btBusca = new javax.swing.JButton();
        tfNomeCurso = new javax.swing.JTextField();
        tfId = new javax.swing.JLabel();
        btFechar = new javax.swing.JButton();
        btGravar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações do Acadêmico"));
        jPanel2.setFocusTraversalPolicyProvider(true);

        jLabel2.setText("Código:");

        tfCodigo.setEditable(false);
        tfCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigo.setText("000000");

        chkAtivo.setSelected(true);
        chkAtivo.setText("Ativo");

        jLabel1.setText("Nome:");

        jLabel3.setText("Curso:");

        tfCodigoCurso.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigoCurso.setText("000000");
        tfCodigoCurso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfCodigoCursoFocusLost(evt);
            }
        });

        btBusca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaActionPerformed(evt);
            }
        });

        tfNomeCurso.setEditable(false);

        tfId.setText("000000");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(33, 33, 33)
                        .addComponent(tfCodigoCurso)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNomeCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkAtivo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tfId))
                            .addComponent(tfNome, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkAtivo))
                    .addComponent(tfId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfNomeCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(tfCodigoCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btGravar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btFechar))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btFechar)
                    .addComponent(btGravar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaActionPerformed
        Curso c = abreConsultaCurso();
        if (c != null) {
            fillCurso(c);
        } else {
            tfCodigoCurso.setText("000000");
            tfNomeCurso.setText("");
            tfCodigoCurso.selectAll();
        }
    }//GEN-LAST:event_btBuscaActionPerformed

    private void btGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGravarActionPerformed
        if (validateForm()){
            salvar();
        }
    }//GEN-LAST:event_btGravarActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        end();
    }//GEN-LAST:event_btFecharActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBusca;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btGravar;
    private javax.swing.JCheckBox chkAtivo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField tfCodigo;
    private javax.swing.JTextField tfCodigoCurso;
    private javax.swing.JLabel tfId;
    private javax.swing.JTextField tfNome;
    private javax.swing.JTextField tfNomeCurso;
    // End of variables declaration//GEN-END:variables
private class MyVerify extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            try {
                Integer.parseInt(tfCodigoCurso.getText());
            } catch (Exception e) {
                tfCodigoCurso.setText("000000");
                tfCodigoCurso.selectAll();
                return false;
            }
            return true;
        }
    }
}

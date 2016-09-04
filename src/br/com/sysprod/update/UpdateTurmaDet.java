package br.com.sysprod.update;

import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.AcademicoBD;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Academico;
import br.com.sysprod.vo.TurmaDet;
import java.awt.Component;
import java.sql.SQLException;
import java.util.List;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author Cristiano Bombazar
 */
public class UpdateTurmaDet extends javax.swing.JDialog implements Formulario{

    private List<TurmaDet> listaTurmaDet;
    private TurmaDet turmaDetAtual;
    private int IdTurmaDet = 0;
    
    public UpdateTurmaDet(java.awt.Frame parent, boolean modal, List<TurmaDet> listaTurmaDet) {
        super(parent, modal);
        this.listaTurmaDet = listaTurmaDet;
        initComponents();
        initForm();
        initComponent();
    }
    
        @Override
    public void initForm() {
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void initComponent() {
        tfCodigoAcademico.setToolTipText("Digite um código ou clique no ícone ao lado para abrir a consulta de acadêmicos");
        btBuscaAcademico.setToolTipText("Abre tela de consulta de acadêmicos");
        tfNomeAcademico.setToolTipText("Nome do acadêmico");
        tfCodigoAcademico.setInputVerifier(new MyVerify());
        tfCodigoAcademico.selectAll();
    }

    @Override
    public boolean validateForm() {
        Component[] c = {tfCodigoAcademico};
        return Utils.validaObrigatorios(c);
    }

    @Override
    public void end() {
        dispose();
    }
    
    private Academico abreConsultaAcademico() {
        ConsultaDialog consulta = new ConsultaDialog(null, true);
        consulta.vinculaAcademico();
        consulta.initForm();
        consulta.setVisible(true);
        Academico c = consulta.getBrowseAcademico().getAcademico();
        return c;
    }

    private void fillAcademico(Academico c) {
        tfCodigoAcademico.setText(Utils.codigoFormatado(c.getCodigo()));
        tfNomeAcademico.setText(c.getNome());
    }

    private void carregaAcademico() {
        Integer codigo = Integer.parseInt(tfCodigoAcademico.getText());
        if (codigo > 0) {
            AcademicoBD acadBD = new AcademicoBD();
            Academico acad = null;
            try {
                acad = acadBD.findByCodigo(codigo);
            } catch (SQLException e) {
                ErrorVerification.ErrDetalhe(e, "Erro ao buscar acadêmico");
            }
            if (acad != null) {
                fillAcademico(acad);
            } else {
                Academico c = abreConsultaAcademico();
                if (c != null) {
                    fillAcademico(c);
                } else {
                    tfCodigoAcademico.setText("000000");
                    tfNomeAcademico.setText("");
                    tfCodigoAcademico.selectAll();
                }
            }
        } else {
            tfCodigoAcademico.setText("000000");
            tfNomeAcademico.setText("");
            tfCodigoAcademico.selectAll();
        }
    }
    
    private void salvar(){
        Integer codigo = Integer.parseInt(tfCodigoAcademico.getText());
        Academico acad = null;
        try {
           acad = new AcademicoBD().findByCodigo(codigo);
        } catch (SQLException e) {
            ErrorVerification.ErrDetalhe(e, "Erro ao buscar acadêmico");
        }
        TurmaDet t = new TurmaDet();
        t.setId(IdTurmaDet);
        t.setAcademico(acad);
        if (listaTurmaDet.contains(t)){
            JOptionPane.showMessageDialog(null, "Acadêmico já vinculado a turma.");
            return;
        }
        if (turmaDetAtual != null && !turmaDetAtual.equals(t)){
            listaTurmaDet.remove(turmaDetAtual);
        }
        listaTurmaDet.add(t);
        end();
    }
    
    public void editar(TurmaDet turmaDet){
        this.IdTurmaDet = turmaDet.getId();
        this.turmaDetAtual = turmaDet;
        tfCodigoAcademico.setText(Utils.codigoFormatado(turmaDet.getAcademico().getCodigo()));
        tfNomeAcademico.setText(turmaDet.getAcademico().getNome());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfCodigoAcademico = new javax.swing.JTextField();
        btBuscaAcademico = new javax.swing.JButton();
        tfNomeAcademico = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações do Acadêmico"));

        jLabel1.setText("Acadêmico:");

        tfCodigoAcademico.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigoAcademico.setText("000000");
        tfCodigoAcademico.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfCodigoAcademicoFocusLost(evt);
            }
        });

        btBuscaAcademico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBuscaAcademico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaAcademicoActionPerformed(evt);
            }
        });

        tfNomeAcademico.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfCodigoAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btBuscaAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfNomeAcademico, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(tfCodigoAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tfNomeAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBuscaAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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

    private void tfCodigoAcademicoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoAcademicoFocusLost
        carregaAcademico();
    }//GEN-LAST:event_tfCodigoAcademicoFocusLost

    private void btBuscaAcademicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaAcademicoActionPerformed
        Academico c = abreConsultaAcademico();
        if (c != null) {
            fillAcademico(c);
        } else {
            tfCodigoAcademico.setText("000000");
            tfNomeAcademico.setText("");
            tfCodigoAcademico.selectAll();
        }
    }//GEN-LAST:event_btBuscaAcademicoActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (validateForm()){
            salvar();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        end();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBuscaAcademico;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField tfCodigoAcademico;
    private javax.swing.JTextField tfNomeAcademico;
    // End of variables declaration//GEN-END:variables

    private class MyVerify extends InputVerifier {

        @Override
        public boolean verify(JComponent input) {
            try {
                Integer.parseInt(tfCodigoAcademico.getText());
            } catch (Exception e) {
                tfCodigoAcademico.setText("000000");
                tfCodigoAcademico.selectAll();
                return false;
            }
                return true;
        }
    }
}

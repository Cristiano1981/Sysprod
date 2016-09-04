package br.com.sysprod.update;

import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.AcademicoBD;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Academico;
import br.com.sysprod.vo.ProvaDetAcademico;
import java.awt.Component;
import java.sql.SQLException;
import java.util.List;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * @author Cristiano Bombazar
 */
public final class UpdateProvaDetAcademico extends javax.swing.JDialog implements Formulario{
    
    private List<ProvaDetAcademico> listaProvaDetAcademico;
    private ProvaDetAcademico       provaDetAcademicoAtual;

    public UpdateProvaDetAcademico(java.awt.Frame parent, boolean modal, List<ProvaDetAcademico> listaProvaDetAcademico) {
        super(parent, modal);
        this.listaProvaDetAcademico = listaProvaDetAcademico;
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
        cbAssinalado.setToolTipText("Letra assinalada pelo acadêmico. N = Não assinalado");
        tfCodigoAcademico.selectAll();
        tfCodigoAcademico.setInputVerifier(new MyVerify());
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
    
    public void editar(ProvaDetAcademico item){
        provaDetAcademicoAtual = item;
        fillAcademico(item.getAcademico());
        tfCodigo.setText(Utils.codigoFormatado(item.getId()));
        cbAssinalado.setSelectedItem(item.getAssinalado());
        chkCorreto.setSelected(item.isCorreta());
    }
    
    private void salvar(){
        ProvaDetAcademico item = new ProvaDetAcademico();
        item.setId(Integer.parseInt(tfCodigo.getText()));
        item.setAssinalado(cbAssinalado.getSelectedItem().toString());
        item.setCorreta(chkCorreto.isSelected());
        try {
            item.setAcademico(new AcademicoBD().findByCodigo(Integer.parseInt(tfCodigoAcademico.getText())));
        } catch (SQLException e) {
            ErrorVerification.ErrDetalhe(e, "Erro ao buscar acadêmico /UpdateProvaDetAcademico.");
        }
        if (provaDetAcademicoAtual != null && !provaDetAcademicoAtual.equals(item)){
            listaProvaDetAcademico.remove(provaDetAcademicoAtual);
        }
        if (listaProvaDetAcademico.contains(item)){
            listaProvaDetAcademico.remove(provaDetAcademicoAtual);
        }
        listaProvaDetAcademico.add(item);
        end();
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
        jLabel2 = new javax.swing.JLabel();
        cbAssinalado = new javax.swing.JComboBox();
        chkCorreto = new javax.swing.JCheckBox();
        tfCodigo = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações Resposta Acadêmico"));

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

        jLabel2.setText("Assinalado:");

        cbAssinalado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A", "B", "C", "D", "E", "N" }));

        chkCorreto.setText("Correto");
        chkCorreto.setEnabled(false);
        chkCorreto.setFocusPainted(false);
        chkCorreto.setFocusable(false);

        tfCodigo.setText("000000");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbAssinalado, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfCodigoAcademico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btBuscaAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfNomeAcademico, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(chkCorreto)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tfCodigo)
                .addGap(24, 24, 24))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(tfCodigo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(tfCodigoAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tfNomeAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBuscaAcademico, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbAssinalado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(chkCorreto))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Fechar");

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
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        salvar();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBuscaAcademico;
    private javax.swing.JComboBox cbAssinalado;
    private javax.swing.JCheckBox chkCorreto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel tfCodigo;
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

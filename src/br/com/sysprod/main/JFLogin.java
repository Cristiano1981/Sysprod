package br.com.sysprod.main;

import br.com.sysprod.connection.ConnectionManager;
import br.com.sysprod.constantes.ConstanteGlobal;
import br.com.sysprod.dao.UnidadeBD;
import br.com.sysprod.dao.UsuarioBD;
import br.com.sysprod.gui.JFMain;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.vo.Usuario;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Unidade;
import com.sun.java.swing.SwingUtilities3;
import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sun.swing.SwingUtilities2;

/**
 * @author Cristiano Bombazar
 */
public final class JFLogin extends javax.swing.JFrame implements Formulario {

    List<Unidade> listaUnidades = null;

    public JFLogin() {
        initComponents();
        initForm();
        initConnection();
        initComponent();
    }

    @Override
    public void initForm() {
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().setDefaultButton(btLogin);
        setTitle("SYSPROD. Ano: " + new SimpleDateFormat("yyyy").format(new Date())+". Versão: Beta | 1.00");
    }

    @Override
    public void initComponent() {
        cbUnidade.setToolTipText("Escolha a unidade");
        tfUsuario.setToolTipText("Usuário para acesso do sistema");
        tfSenha.setToolTipText("Senha para acesso do sistema");
    }

    private void initConnection() {
        status.setText("Conectando banco de dados. Por favor, aguarde...");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                editable(false);
                if (ConnectionManager.openConnection()) {
                    tfUsuario.requestFocusInWindow();
                    status.setText("Banco de dados conectado.");
                    carregaUnidade();
                    editable(true);
                } else {
                    System.exit(0);
                }
            }
        });
    }

    @Override
    public boolean validateForm() {
        Component[] c = new Component[2];
        c[0] = tfUsuario;
        c[1] = tfSenha;
        return Utils.validaObrigatorios(c);
    }

    @Override
    public void end() {
        dispose();
    }

    private void editable(boolean bloq) {
        cbUnidade.setEnabled(bloq);
        tfUsuario.setEditable(bloq);
        tfSenha.setEditable(bloq);
    }

    private void validaLogin() {
        UsuarioBD userBD = new UsuarioBD();
        String login     = tfUsuario.getText().trim().toUpperCase();
        String senha     = tfSenha.getText().trim().toUpperCase();
        Usuario user     = null;
        Unidade unidade  = null;
        try {
            user = userBD.validaLogin(login, senha);
            if (listaUnidades != null) {
                unidade = (Unidade) cbUnidade.getSelectedItem();
            }
            if (user != null) {
                JFMain main = new JFMain(user, unidade);
                main.setVisible(true);
                end();
            } else {
                JOptionPane.showMessageDialog(null, "Login inválido. Tente novamente!", "Atenção!", JOptionPane.ERROR_MESSAGE);
                tfUsuario.setText("");
                tfSenha.setText("");
                tfUsuario.requestFocusInWindow();
            }
        } catch (SQLException | HeadlessException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro ao consultar login.");
        }
    }

    private void carregaUnidade() {
        UnidadeBD unBD = new UnidadeBD();
        String filtro = "WHERE ativo is true";
        try {
            List<Unidade> lista = unBD.queryAll(filtro, null);
            if (lista != null) {
                if (listaUnidades == null) {
                    listaUnidades = new ArrayList<Unidade>();
                }
                listaUnidades.addAll(lista);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro ao buscar unidades.");
        }
        if (listaUnidades == null || listaUnidades.isEmpty()) {
            cbUnidade.addItem("0 - Sem unidade ativa");
        } else {
            for (Unidade un : listaUnidades) {
                cbUnidade.addItem(un);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbUnidade = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        tfUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfSenha = new javax.swing.JPasswordField();
        btLogin = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        status = new javax.swing.JLabel();
        labelImagem = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Login"));

        jLabel1.setText("Unidade:");

        jLabel2.setText("Usuário:");

        jLabel3.setText("Senha:");

        btLogin.setText("Login");
        btLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLoginActionPerformed(evt);
            }
        });

        btFechar.setText("Fechar");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(21, 21, 21)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(27, 27, 27)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tfUsuario, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbUnidade, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfSenha, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(btLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btFechar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btLogin)
                    .addComponent(btFechar))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        status.setText("Status:");

        labelImagem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/logoUnibave.jpg"))); // NOI18N
        labelImagem.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(status)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(labelImagem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelImagem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(status))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLoginActionPerformed
        if (validateForm()) {
            validaLogin();
        }
    }//GEN-LAST:event_btLoginActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btFecharActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (ConstanteGlobal.LOOK_AND_FEEL_WINDOWS.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btLogin;
    private javax.swing.JComboBox cbUnidade;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelImagem;
    private javax.swing.JLabel status;
    private javax.swing.JPasswordField tfSenha;
    private javax.swing.JTextField tfUsuario;
    // End of variables declaration//GEN-END:variables

}

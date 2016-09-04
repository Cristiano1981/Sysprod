package br.com.sysprod.update;

import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.ProvaBD;
import br.com.sysprod.dao.ProvaDetBD;
import br.com.sysprod.dao.ProvaDetItemBD;
import br.com.sysprod.dao.TurmaBD;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Prova;
import br.com.sysprod.vo.ProvaDet;
import br.com.sysprod.vo.ProvaDetItem;
import br.com.sysprod.vo.Turma;
import java.awt.Component;
import java.awt.Cursor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author Cristiano Bombazar
 */
public final class DuplicaProva extends javax.swing.JDialog implements Formulario{
    
    private Integer idProva = null;

    public DuplicaProva(java.awt.Frame parent, boolean modal, Integer idProva){
        super(parent, modal);
        this.idProva = idProva;
        initComponents();
        initForm();
        initComponent();
    }
    
     @Override
    public void initForm() {
        setLocationRelativeTo(null);
        setResizable(false);
        getRootPane().setDefaultButton(btGravar);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void initComponent() {
        tfCodigoTurma.setToolTipText("Digite um código ou clique no ícone ao lado para abrir tela de busca");
        btBusca.setToolTipText("Clica para abrir tela de busca");
        tfDescricaoTurma.setToolTipText("Descrição da turma");
        tfCodigoTurma.selectAll();
        tfCodigoTurma.setInputVerifier(new MyVerify());
    }

    @Override
    public boolean validateForm() {
        Component[] c = {tfCodigoTurma};
        return Utils.validaObrigatorios(c);
    }

    @Override
    public void end() {
        dispose();
    }
    
    private Turma abreConsultaTurma() {
        ConsultaDialog consulta = new ConsultaDialog(null, true);
        consulta.vinculaTurma();
        consulta.initForm();
        consulta.setVisible(true);
        Turma t = consulta.getBrowseTurma().getTurma();
        return t;
    }
    
     private void fillTruma(Turma t) {
        if (t == null){
            tfCodigoTurma.setText("000000");
            tfDescricaoTurma.setText("");
        }else{
            tfCodigoTurma.setText(Utils.codigoFormatado(t.getId()));
            tfDescricaoTurma.setText(t.getCurso().getDescricao() + " - "+t.getFase()+"ª");
        }
    }

    private void carregaTurma() {
        Integer codigo = Integer.parseInt(tfCodigoTurma.getText());
        if (codigo > 0) {
            TurmaBD turBD = new TurmaBD();
            Turma turma = null;
            try {
                turma = turBD.find(codigo, false);
            } catch (SQLException e) {
                ErrorVerification.ErrDetalhe(e, "Erro ao buscar turma");
            }
            if (turma != null) {
                fillTruma(turma);
            } else {
                Turma c = abreConsultaTurma();
                if (c != null) {
                    fillTruma(c);
                } else {
                    fillTruma(null);
                    tfCodigoTurma.selectAll();
                }
            }
        } else {
            fillTruma(null);
            tfCodigoTurma.selectAll();
        }
    }
    
    private void duplicar(){
        getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Turma              turma             = new Turma();
        Prova              prova             = new Prova();
        List<ProvaDet>     listaProvaDet     = new ArrayList<>();
        List<ProvaDetItem> listaProvaDetItem = new ArrayList<>();
        Prova              provaAux          = new Prova();
        ProvaBD            provaBD           = new ProvaBD();
        ProvaDetBD         provaDetBD        = new ProvaDetBD();
        ProvaDetItemBD     provaDetItemBD    = new ProvaDetItemBD();
        turma.setId(Integer.parseInt(tfCodigoTurma.getText()));
        try {
            prova.setId(provaBD.nextCode());
            prova.setTurma(turma);
            provaAux = provaBD.find(idProva, false);
            if (provaAux != null){
                prova.setDtAplicacao(provaAux.getDtAplicacao());
                prova.setDtCadastro(provaAux.getDtCadastro());
                prova.setObs(provaAux.getObs());
                 List<ProvaDet> listaProvaDetAux = provaDetBD.queryAllByIdProva(idProva);
                 if (listaProvaDetAux != null && !listaProvaDetAux.isEmpty()){
                     for (ProvaDet det : listaProvaDetAux) {
                         List<ProvaDetItem> listaProvaDetItemAux = provaDetItemBD.queryAllByProvaDet(det.getId());
                         if (listaProvaDetItemAux != null && !listaProvaDetItemAux.isEmpty()){
                             listaProvaDetItem.clear();
                             for (ProvaDetItem item : listaProvaDetItemAux) {
                                 item.setId(0);
                                 listaProvaDetItem.add(item);
                             }
                              det.setId(0);
                              det.setListaProvaDetItem(listaProvaDetItem);
                              listaProvaDet.add(det);
                         }
                     }
                     prova.setListaProvaDet(listaProvaDet);
                 }
                 if (provaBD.saveOrUpdate(prova)){
                     JOptionPane.showMessageDialog(null, "Prova duplicada com sucesso!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                     end();
                 }
            }
        } catch (SQLException e) {
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro ao duplicar turma");
        }
        getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfCodigoTurma = new javax.swing.JTextField();
        btBusca = new javax.swing.JButton();
        tfDescricaoTurma = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btGravar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informação da Turma"));

        jLabel1.setText("Turma:");

        tfCodigoTurma.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigoTurma.setText("000000");
        tfCodigoTurma.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfCodigoTurmaFocusLost(evt);
            }
        });

        btBusca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaActionPerformed(evt);
            }
        });

        tfDescricaoTurma.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfCodigoTurma, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfDescricaoTurma, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfDescricaoTurma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(tfCodigoTurma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Fechar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btGravar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
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
                    .addComponent(btGravar))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfCodigoTurmaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoTurmaFocusLost
        carregaTurma();
    }//GEN-LAST:event_tfCodigoTurmaFocusLost

    private void btBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaActionPerformed
        Turma c = abreConsultaTurma();
        if (c != null) {
            fillTruma(c);
        } else {
            tfCodigoTurma.setText("000000");
            tfDescricaoTurma.setText("");
            tfCodigoTurma.selectAll();
        }
    }//GEN-LAST:event_btBuscaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        end();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGravarActionPerformed
        if (validateForm()){
            duplicar();
        }
    }//GEN-LAST:event_btGravarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBusca;
    private javax.swing.JButton btGravar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField tfCodigoTurma;
    private javax.swing.JTextField tfDescricaoTurma;
    // End of variables declaration//GEN-END:variables

    private class MyVerify extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            try {
                Integer.parseInt(tfCodigoTurma.getText());
            } catch (NumberFormatException e) {
                tfCodigoTurma.setText("000000");
                tfCodigoTurma.selectAll();
                return false;
            }
            return true;
        }
    }
}

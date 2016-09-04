package br.com.sysprod.update;

import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.PerguntaBD;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Pergunta;
import br.com.sysprod.vo.ProvaDetItem;
import java.awt.Component;
import java.sql.SQLException;
import java.util.List;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultCaret;

/**
 * @author Cristiano Bombazar
 */
public final class UpdateProvaDetItem extends javax.swing.JDialog implements Formulario{
    
    private List<ProvaDetItem> listaProvaDetItem;
    private ProvaDetItem       provaDetItemAtual;
    
    
    public UpdateProvaDetItem(java.awt.Frame parent, boolean modal, List<ProvaDetItem> listaProvaDetItem) {
        super(parent, modal);
        this.listaProvaDetItem = listaProvaDetItem;
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
        tfCodigoPergunta.setToolTipText("Digite um código da pergunta ou clique no ícone ao lado para abrir tela de busca;");
        btBusca.setToolTipText("Clique no ícone para abrir tela de consulta de perguntas;");
        tfDescricaoPergunta.setToolTipText("Descrição da pergunta;");
        tfCodigoPergunta.setInputVerifier(new MyVerify());
        tfCodigoPergunta.selectAll();
        ((DefaultCaret) tfDescricaoPergunta.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
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

   private Pergunta abreConsultaPergunta() {
        ConsultaDialog consulta = new ConsultaDialog(null, true);
        consulta.vinculaPergunta();
        consulta.getBrowsePergunta().cbTipo.setSelectedItem("Alternativa");
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
                if (pergunta.getTipo().equals("P")){
                    JOptionPane.showMessageDialog(null, "A questão está cadastrada como 'Pergunta'. Favor, selecione outra!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
                    fillPergunta(null);
                    return;
                }
                fillPergunta(pergunta);
            } else {
                Pergunta c = abreConsultaPergunta();
                if (c != null) {
                    if (pergunta.getTipo().equals("P")){
                        JOptionPane.showMessageDialog(null, "A questão está cadastrada como 'Pergunta'. Favor, selecione outra!", "Atenção", JOptionPane.INFORMATION_MESSAGE);
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
    
    public void novo(){
        setTitle("Nova Alternativa");
    }
    
    public void editar(ProvaDetItem item){
        setTitle("Editar Alternativa");
        provaDetItemAtual = item;
        tfCodigo.setText(Utils.codigoFormatado(item.getId() == null ? 000000 : item.getId()));
        fillPergunta(item.getPergunta());
        cbLetra.setSelectedItem(item.getLetra());
        chkCorreto.setSelected(item.isCorreto());
    }
    
    private void salvar(){
        ProvaDetItem provaDetItem = new ProvaDetItem();
        Pergunta pergunta = new Pergunta();
        provaDetItem.setId(Integer.parseInt(tfCodigo.getText()));
        provaDetItem.setLetra(cbLetra.getSelectedItem().toString());
        pergunta.setId(Integer.parseInt(tfCodigoPergunta.getText()));
        pergunta.setDescricao(tfDescricaoPergunta.getText());
        provaDetItem.setPergunta(pergunta);
        provaDetItem.setCorreto(chkCorreto.isSelected());
        
        if (provaDetItemAtual != null && !provaDetItemAtual.equals(provaDetItem)){
            listaProvaDetItem.remove(provaDetItemAtual);
        }
        if (listaProvaDetItem.contains(provaDetItem)){
            listaProvaDetItem.remove(provaDetItemAtual);
        }
        listaProvaDetItem.add(provaDetItem);
        end();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        tfCodigo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tfCodigoPergunta = new javax.swing.JTextField();
        btBusca = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tfDescricaoPergunta = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        cbLetra = new javax.swing.JComboBox();
        chkCorreto = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações da Alternativa"));

        tfCodigo.setText("000000");

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

        jLabel1.setText("Letra:");

        cbLetra.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A", "B", "C", "D", "E" }));

        chkCorreto.setText("Alternativa Correta");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(23, 23, 23)
                        .addComponent(tfCodigoPergunta, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(cbLetra, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chkCorreto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(tfCodigo))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfCodigo)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(tfCodigoPergunta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(cbLetra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(chkCorreto)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfCodigoPerguntaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfCodigoPerguntaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfCodigoPerguntaActionPerformed

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (validateForm()){
            salvar();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        end();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBusca;
    private javax.swing.JComboBox cbLetra;
    private javax.swing.JCheckBox chkCorreto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel tfCodigo;
    private javax.swing.JTextField tfCodigoPergunta;
    private javax.swing.JTextArea tfDescricaoPergunta;
    // End of variables declaration//GEN-END:variables

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

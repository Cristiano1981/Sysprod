package br.com.sysprod.update;

import br.com.sysprod.dao.PeriodoBD;
import br.com.sysprod.dao.UnidadeBD;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Periodo;
import br.com.sysprod.vo.Unidade;
import java.awt.Component;
import javax.swing.JFrame;

/**
 * @author Cristiano Bombazar
 */
public class UpdatePeriodo extends javax.swing.JDialog implements Formulario{

    public UpdatePeriodo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initComponent();
        initForm();
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
        tfCodigo.setToolTipText("Código do período");
        chkEncerrado.setToolTipText("Período aberto/encerrdo");
        tfDescricao.setToolTipText("Descrição do periodo");
        tfDataInicio.setToolTipText("Data de abertura do período");
        tfDataFim.setToolTipText("Data de encerramento do período");
        tfDataInicio.requestFocusInWindow();
    }

    @Override
    public boolean validateForm() {
        Component[] c = {tfDataInicio, tfDataFim, tfDescricao};
        return Utils.validaObrigatorios(c);
    }
    
    private void salvar(){
        Periodo periodo = new Periodo();
        periodo.setId(Integer.parseInt(tfCodigo.getText()));
        periodo.setDescricao(tfDescricao.getText().trim().toUpperCase());
        periodo.setEncerrado(chkEncerrado.isSelected());
        periodo.setDtInicio(Utils.converteDate(tfDataInicio.getText()));
        periodo.setDtFim(Utils.converteDate(tfDataFim.getText()));
        PeriodoBD perBD = new PeriodoBD();
        try {
            if (perBD.saveOrUpdate(periodo)){
                end();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro ao gravar periodo");
        }
    }
    
    public void novo(){
        setTitle("Novo Período");
        Integer codigo = null;
        PeriodoBD perBD = new PeriodoBD();
        try {
            codigo = perBD.nextCode();
            tfCodigo.setText(Utils.codigoFormatado(codigo));
        } catch (Exception e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro buscar próximo código do período");
        }
    }
    
    public void editar(Periodo periodo){
        setTitle("Editar Período");
        tfCodigo.setText(Utils.codigoFormatado(periodo.getId()));
        tfDescricao.setText(periodo.getDescricao());
        chkEncerrado.setSelected(periodo.isEncerrado());
        tfDataInicio.setText(Utils.converteDateParaString(periodo.getDtInicio()));
        tfDataFim.setText(Utils.converteDateParaString(periodo.getDtFim()));
    }

    @Override
    public void end() {
        dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfCodigo = new javax.swing.JTextField();
        chkEncerrado = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        tfDescricao = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfDataInicio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tfDataFim = new javax.swing.JTextField();
        btFechar = new javax.swing.JButton();
        btGravar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações do Período"));

        jLabel1.setText("Código:");

        tfCodigo.setEditable(false);
        tfCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigo.setText("000000");

        chkEncerrado.setText("Encerrado");

        jLabel2.setText("Descrição:");

        jLabel3.setText("Dt Inic:");

        tfDataInicio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfDataInicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfDataInicioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfDataInicioFocusLost(evt);
            }
        });

        jLabel4.setText("Dt Fim:");

        tfDataFim.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfDataFim.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfDataFimFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfDataFimFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfDescricao)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tfDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addGap(12, 12, 12)
                                .addComponent(tfDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkEncerrado)))
                        .addGap(0, 32, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkEncerrado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tfDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(tfDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(tfDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        end();
    }//GEN-LAST:event_btFecharActionPerformed

    private void btGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGravarActionPerformed
        if (validateForm()){
            salvar();
        }
    }//GEN-LAST:event_btGravarActionPerformed

    private void tfDataInicioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDataInicioFocusLost
        tfDataInicio.setText(Utils.formataData(tfDataInicio.getText().trim()));
    }//GEN-LAST:event_tfDataInicioFocusLost

    private void tfDataFimFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDataFimFocusLost
        tfDataFim.setText(Utils.formataData(tfDataFim.getText().trim()));
    }//GEN-LAST:event_tfDataFimFocusLost

    private void tfDataInicioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDataInicioFocusGained
        tfDataInicio.selectAll();
    }//GEN-LAST:event_tfDataInicioFocusGained

    private void tfDataFimFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDataFimFocusGained
        tfDataFim.selectAll();
    }//GEN-LAST:event_tfDataFimFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btGravar;
    private javax.swing.JCheckBox chkEncerrado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField tfCodigo;
    private javax.swing.JTextField tfDataFim;
    private javax.swing.JTextField tfDataInicio;
    private javax.swing.JTextField tfDescricao;
    // End of variables declaration//GEN-END:variables
}

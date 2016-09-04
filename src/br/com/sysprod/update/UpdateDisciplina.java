package br.com.sysprod.update;

import br.com.sysprod.Global;
import br.com.sysprod.dao.DisciplinaBD;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Disciplina;
import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JFrame;

/**
 * @author Cristiano Bombazar
 */
public class UpdateDisciplina extends javax.swing.JDialog implements Formulario{

    public UpdateDisciplina(java.awt.Frame parent, boolean modal) {
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
        tfCodigo.setToolTipText("Código da disciplina");
        chkAtivo.setToolTipText("Disciplina ativa");
        tfNome.setToolTipText("Nome da disciplina");
        tfNome.requestFocusInWindow();
    }

    @Override
    public boolean validateForm() {
        Component[] c = {tfNome, cbFase};
        return Utils.validaObrigatorios(c);
    }

    @Override
    public void end() {
        dispose();
    }
    
    private void salvar(){
        Disciplina d = new Disciplina();
        d.setId(Integer.parseInt(tfCodigo.getText()));
        d.setNome(tfNome.getText().trim().toUpperCase());
        d.setFase(Integer.parseInt(cbFase.getSelectedItem().toString()));
        d.setUnidade(Global.getUnidade());
        d.setAtivo(chkAtivo.isSelected());
        DisciplinaBD disBD = new DisciplinaBD();
        try {
            if (disBD.saveOrUpdate(d)){
                end();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro ao gravar registro");
        }
    }
    
    public void novo(){
        setTitle("Nova Disciplina");
        Integer codigo = null;
        DisciplinaBD disBD = new DisciplinaBD();
        try {
            codigo = disBD.nextCode();
            tfCodigo.setText(Utils.codigoFormatado(codigo));
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorVerification.ErrDetalhe(e, "Ocorreu um erro buscar próximo código da disciplina");
        }
    }
    
    public void editar(Disciplina d){
        setTitle("Editar Disciplina");
        tfCodigo.setText(Utils.codigoFormatado(d.getId()));
        tfNome.setText(d.getNome());
        cbFase.setSelectedItem(d.getFase().toString());
        chkAtivo.setSelected(d.isAtivo());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfCodigo = new javax.swing.JTextField();
        chkAtivo = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        tfNome = new javax.swing.JTextField();
        cbFase = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        btFechar = new javax.swing.JButton();
        btGravar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações da Disciplina"));

        jLabel1.setText("Código:");

        tfCodigo.setEditable(false);
        tfCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigo.setText("000000");

        chkAtivo.setSelected(true);
        chkAtivo.setText("Ativo");

        jLabel2.setText("Desc:");

        cbFase.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecione", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));

        jLabel3.setText("Fase:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkAtivo)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(cbFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 49, Short.MAX_VALUE))
                    .addComponent(tfNome))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkAtivo)
                    .addComponent(cbFase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(tfNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGravarActionPerformed
        if (validateForm()){
            salvar();
        }
    }//GEN-LAST:event_btGravarActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        end();
    }//GEN-LAST:event_btFecharActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btGravar;
    private javax.swing.JComboBox cbFase;
    private javax.swing.JCheckBox chkAtivo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField tfCodigo;
    private javax.swing.JTextField tfNome;
    // End of variables declaration//GEN-END:variables
}

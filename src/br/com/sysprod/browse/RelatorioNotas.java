package br.com.sysprod.browse;

import br.com.sysprod.Global;
import br.com.sysprod.consulta.ConsultaDialog;
import br.com.sysprod.dao.ProvaBD;
import br.com.sysprod.dao.RegraBD;
import br.com.sysprod.util.ErrorVerification;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Prova;
import br.com.sysprod.vo.Regra;
import br.com.sysprod.vo.RelatorioNota;
import br.com.sysprod.vo.Turma;
import java.awt.Cursor;
import java.awt.Dimension;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 * @author Cristiano Bombazar
 */
public class RelatorioNotas extends javax.swing.JInternalFrame{

    private Map<Integer, Turma> mapaTurmas = null;
    public static final int REL_MODELO_TURMA = 0;
    public static final int REL_MODELO_PROVA = 1;
    
    public RelatorioNotas() {
        initComponents();
        refresh();
    }
    
    public void posicao() {
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 4);
    }
    
    private Prova abreConsultaProva() {
        ConsultaDialog consulta = new ConsultaDialog(null, true);
        consulta.vinculaProva();
        consulta.initForm();
        consulta.setVisible(true);
        Prova p = consulta.getBrowseProva().getProva();
        return p;
    }
      
    private void fillProva(Prova p){
        if (p == null){
            tfCodigoProva.setText("000000");
            tfDescricaoProva.setText("");
        }else{
            tfCodigoProva.setText(Utils.codigoFormatado(p.getId()));
            tfDescricaoProva.setText(p.getTurma().getCurso().getDescricao() + " "+p.getTurma().getFase()+"ª");
        }
    } 
    
     private void carregaProva() {
        Integer codigo = Integer.parseInt(tfCodigoProva.getText());
        if (codigo > 0) {
            ProvaBD provaBD = new ProvaBD();
            Prova prova = null;
            try {
                prova = provaBD.find(codigo, false);
            } catch (SQLException e) {
                ErrorVerification.ErrDetalhe(e, "Erro ao buscar prova");
            }
            if (prova != null) {
                fillProva(prova);
            } else {
                Prova c = abreConsultaProva();
                System.out.println(c.getTurma().getCurso().getDescricao());
                if (c != null) {
                    fillProva(c);
                } else {
                    fillProva(null);
                    tfCodigoProva.selectAll();
                }
            }
        } else {
          fillProva(null);
          tfCodigoProva.selectAll();
        }
     }
    
     private void refresh(){
        if (cbModelo.getSelectedIndex() == 0 ){
            tfCodigoProva.setEnabled(false);
            btBuscaProva.setEnabled(false);
            btBuscaPeriodo.setEnabled(true);
        }else{
            tfCodigoProva.setEnabled(true);
            btBuscaProva.setEnabled(true);
            btBuscaPeriodo.setEnabled(false);
        }
     }
     
     private void relByProva(){
         progress.setBorderPainted(true);
         Integer              modelo       = cbModelo.getSelectedIndex();
         Integer              codProva     = null;
         String               turmas       = null;
         ProvaBD              provBD       = new ProvaBD();
         List<RelatorioNota> listaRelNotas = null;
         if (modelo == RelatorioNotas.REL_MODELO_PROVA){
             codProva = Integer.parseInt(tfCodigoProva.getText());
         }else{
             Set<Integer> keys = mapaTurmas.keySet();
             for (Integer key : keys) {
                 Turma turma = mapaTurmas.get(key);
                 if (turmas == null){
                     turmas = turma.getId().toString();
                 }else{
                     turmas += ","+turma.getId();
                 }
             }
         }
         try{ 
             if (modelo == RelatorioNotas.REL_MODELO_PROVA){
                 listaRelNotas = provBD.selectRelByIDProva(codProva);
             }else{
                 listaRelNotas = provBD.selectRelByTurma(turmas);
             }
         } catch (SQLException e) {
             ErrorVerification.ErrDetalhe(e, "Erro ao gerar relatório de notas");
             e.printStackTrace();
         }
         if (listaRelNotas != null){
             int progresso = 0;
             progress.setMaximum(listaRelNotas.size() + 10);
             for (RelatorioNota rel : listaRelNotas) {
                 progress.setValue(++progresso);
                 calculaNota(rel);
             }
             geraRelatorio(listaRelNotas, progresso);
         }
     }
     
     private void calculaNota(RelatorioNota rel){
        double acertos   = rel.getQtdAcertos().doubleValue();
        double qtdMaxima = rel.getQtdMaximaAcerto().doubleValue();
        double nota = acertos / qtdMaxima;
        if (nota > 1){
             nota = 1;
        }
        BigDecimal bd = BigDecimal.valueOf(nota);
        Regra regra = null;
         try {
             regra = new RegraBD().find(rel.getFase(), rel.getQtdAcertos());
         } catch (SQLException e) {
             ErrorVerification.ErrDetalhe(e, "erro ao buscar regra para cálculo de notas");
             e.printStackTrace();
        }
        if (regra != null && regra.getArredondar().equals(Regra.ROUND_DOWN)){
            bd = bd.setScale(1, BigDecimal.ROUND_DOWN);
        }else{
            bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        }
        rel.setNota(bd.doubleValue() == 0 ? 0.1 : bd.doubleValue());
    }
     
    private void geraRelatorio(List<RelatorioNota> listaRelatorio, int progresso){
        progress.setValue(++progresso);
        getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         try {
             progress.setValue(++progresso);
            InputStream is = getClass().getResourceAsStream("/br/com/sysprod/ireports/RelatorioNota.jasper"); //S no fim
            progress.setValue(++progresso);
            Map parameters = new HashMap();
            parameters.put("USUARIO_EMISSAO", Global.getUsuario().getNome());
            parameters.put("CAMPUS", Global.getUnidade().getFantasia());
            progress.setValue(++progresso);
            JRBeanCollectionDataSource jb = new JRBeanCollectionDataSource(listaRelatorio);
            progress.setValue(++progresso);
            JasperPrint jp = JasperFillManager.fillReport(is, parameters, jb);
            progress.setValue(++progresso);
            JasperViewer jv = new JasperViewer(jp, false);
            progress.setValue(++progresso);
            jv.setTitle("Rekatório de Notas");
            progress.setValue(++progresso);
            jv.setExtendedState(MAXIMIZED_BOTH);
            progress.setValue(++progresso);
            getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            jv.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            progress.setValue(++progresso);
            jv.setVisible(true);
        } catch(JRException e){
            ErrorVerification.ErrDetalhe(e, "Erro ao gerar relatório de notas");
            e.printStackTrace();
        }
         getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    private void abreConsultaTurmas(){
        BrowseConsultaTurma cons = new BrowseConsultaTurma(null, true);
        cons.setVisible(true);
        List<Turma> lista = cons.getTurmaSelection();
        String montaTurma = "";
        for (Turma turma : lista) {
            montaTurma += turma.getCurso().getDescricao() + " " + turma.getFase() + "ª, ";
        }
        tfTurmas.setText(montaTurma);
        if (lista != null && !lista.isEmpty()) {
            if (mapaTurmas == null) {
                mapaTurmas = new HashMap<>();
            }
            mapaTurmas.clear();
            for (Turma turma : lista) {
                mapaTurmas.put(turma.getFase(), turma);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbModelo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tfCodigoProva = new javax.swing.JTextField();
        btBuscaProva = new javax.swing.JButton();
        tfDescricaoProva = new javax.swing.JTextField();
        tfTurmas = new javax.swing.JTextField();
        btBuscaPeriodo = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        progress = new javax.swing.JProgressBar();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setText("Modelo:");

        cbModelo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Turmas", "Prova" }));
        cbModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbModeloActionPerformed(evt);
            }
        });

        jLabel2.setText("Turmas");

        jLabel3.setText("Prova:");

        tfCodigoProva.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfCodigoProva.setText("000000");
        tfCodigoProva.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfCodigoProvaFocusLost(evt);
            }
        });

        btBuscaProva.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBuscaProva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaProvaActionPerformed(evt);
            }
        });

        tfDescricaoProva.setEditable(false);

        tfTurmas.setEditable(false);

        btBuscaPeriodo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/sysprod/imagem/iconeLupa.png"))); // NOI18N
        btBuscaPeriodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBuscaPeriodoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(tfCodigoProva, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btBuscaProva, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btBuscaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfDescricaoProva, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE)
                            .addComponent(tfTurmas))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfDescricaoProva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tfCodigoProva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(btBuscaProva, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btBuscaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(tfTurmas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jButton1.setText("Fechar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Gerar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(progress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(479, 479, 479)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btBuscaPeriodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaPeriodoActionPerformed
        abreConsultaTurmas();
    }//GEN-LAST:event_btBuscaPeriodoActionPerformed

    private void tfCodigoProvaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfCodigoProvaFocusLost
        carregaProva();
    }//GEN-LAST:event_tfCodigoProvaFocusLost

    private void btBuscaProvaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBuscaProvaActionPerformed
        Prova p = abreConsultaProva();
        if (p != null){
            fillProva(p);
        }else{
           fillProva(null);
           tfCodigoProva.selectAll();
        }
    }//GEN-LAST:event_btBuscaProvaActionPerformed

    private void cbModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbModeloActionPerformed
        refresh();
    }//GEN-LAST:event_cbModeloActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                relByProva();
            }
        });
        t.start();
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBuscaPeriodo;
    private javax.swing.JButton btBuscaProva;
    private javax.swing.JComboBox cbModelo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar progress;
    private javax.swing.JTextField tfCodigoProva;
    private javax.swing.JTextField tfDescricaoProva;
    private javax.swing.JTextField tfTurmas;
    // End of variables declaration//GEN-END:variables
}

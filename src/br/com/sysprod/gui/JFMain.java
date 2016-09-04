package br.com.sysprod.gui;

import br.com.sysprod.Global;
import br.com.sysprod.browse.BrowseImportaAcademico;
import br.com.sysprod.browse.BrowseImportaGabarito;
import br.com.sysprod.browse.BrowseImportaTurma;
import br.com.sysprod.browse.BrowseRegra;
import br.com.sysprod.browse.RelatorioNotas;
import br.com.sysprod.constantes.ConstanteGlobal;
import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.consulta.ConsultaInternal;
import br.com.sysprod.vo.Usuario;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFrame;
import br.com.sysprod.util.Utils;
import br.com.sysprod.vo.Unidade;

/**
 * @author Cristiano Bombazar
 */
public class JFMain extends javax.swing.JFrame implements Formulario{

    public JFMain() {
        initComponents();
        initForm();
        initComponent();
    }
    
    public JFMain(Usuario user, Unidade un){
        Global.setUnidade(un);
        Global.setUsuario(user);
        initComponents();
        initForm();
        initComponent();
        validaUnidade();
    }

    @Override
    public void initForm() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    @Override
    public void initComponent() {
        tfInfoUsuario.setToolTipText("Informações do usuário");
        tfHoraAtual.setToolTipText("Hora atual (Computador).");
        tfTempoLogado.setToolTipText("Tempo logado (Computador).");
        tfInfoUsuario.setText(Utils.codigoFormatado(Global.getUsuario().getId()) + " | "+Global.getUsuario().getNome() + " | "+Global.getUsuario().getLogin());
        new Thread(new AtualizaHora()).start();
    }

    @Override
    public boolean validateForm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void end() {
        System.exit(0);
    }
    
    private void validaUnidade(){
        if (Global.getUnidade() == null){
            setTitle("99 - Sem unidade ativa.");
            menuSistema.setEnabled(false);
            menuCadastroAcademico.setEnabled(false);
            menuCadastroCurso.setEnabled(false);
            menuCadastroPeriodo.setEnabled(false);
        }else{
            setTitle(Global.getUnidade().getId()+ " - "+Global.getUnidade().getFantasia()+ "  |  Sistema de provas Institucionais. Versão: "+ConstanteGlobal.SYS_VERSION);
        }
    }
    
    private void cadastroUsuario(){
        ConsultaInternal consulta = new ConsultaInternal();
        desktop.add(consulta);
        consulta.vinculaUsuario();
        consulta.setTitle("Consulta de Usuários");
        consulta.posicao();
        consulta.setVisible(true);
    }
    
    private void cadastroUnidade(){
        ConsultaInternal consulta = new ConsultaInternal();
        desktop.add(consulta);
        consulta.vinculaUnidade();
        consulta.setTitle("Consulta de Unidades");
        consulta.posicao();
        consulta.setVisible(true);
    }
    
    private void cadastroCurso(){
        ConsultaInternal consulta = new ConsultaInternal();
        desktop.add(consulta);
        consulta.vinculaCurso();
        consulta.setTitle("Consulta de Cursos");
        consulta.posicao();
        consulta.setVisible(true);
    }
    
    private void cadastroAcademico(){
        ConsultaInternal consulta = new ConsultaInternal();
        desktop.add(consulta);
        consulta.vinculaAcademico();
        consulta.setTitle("Consulta de acadêmicos");
        consulta.posicao();
        consulta.setVisible(true);
    }
    
    private void cadastroPeriodo(){
        ConsultaInternal consulta = new ConsultaInternal();
        desktop.add(consulta);
        consulta.vinculaPeriodo();
        consulta.setTitle("Consulta de períodos");
        consulta.posicao();
        consulta.setVisible(true);
    }
    
    private void cadastroTurmas(){
        ConsultaInternal consulta = new ConsultaInternal();
        desktop.add(consulta);
        consulta.vinculaTurma();
        consulta.setTitle("Consulta de Turmas");
        consulta.posicao();
        consulta.setVisible(true);
    }
    
    private void cadastroDisciplinas(){
        ConsultaInternal consulta = new ConsultaInternal();
        desktop.add(consulta);
        consulta.vinculaDisciplina();
        consulta.setTitle("Consulta de Disciplinas");
        consulta.posicao();
        consulta.setVisible(true);
    }
    
    private void cadastroPerguntas(){
        ConsultaInternal consulta = new ConsultaInternal();
        desktop.add(consulta);
        consulta.vinculaPergunta();
        consulta.setTitle("Consulta de perguntas");
        consulta.posicao();
        consulta.setVisible(true);
    }
    
    private void cadastroProvas() {
        ConsultaInternal consulta = new ConsultaInternal();
        desktop.add(consulta);
        consulta.vinculaProva();
        consulta.setTitle("Consulta de Provas");
        consulta.posicao();
        consulta.setVisible(true);
   
    }

    
    private void importaAcademico(){
        BrowseImportaAcademico imp = new BrowseImportaAcademico();
        desktop.add(imp);
        imp.setTitle("Importação dos acadêmicos");
        imp.posicao();
        imp.setVisible(true);
    }
    
    private void importaTurma(){
        BrowseImportaTurma imp = new BrowseImportaTurma();
        desktop.add(imp);
        imp.setTitle("Importação de turmas");
        imp.posicao();
        imp.setVisible(true);
    }
    
    private void relatorioNotas(){
        RelatorioNotas rel = new RelatorioNotas();
        desktop.add(rel);
        rel.setTitle("Relatório de notas");
        rel.posicao();
        rel.setVisible(true);
    }
    
    private void cadastroRegras(){
        BrowseRegra regra = new BrowseRegra();
        desktop.add(regra);
        regra.setTitle("Consulta de regras");
        regra.posicao();
        regra.setVisible(true);
    }
    
    private void importaGabarito(){
        BrowseImportaGabarito imp = new BrowseImportaGabarito();
        desktop.add(imp);
        imp.setTitle("Importação de gabarito");
        imp.posicao();
        imp.setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktop = new javax.swing.JDesktopPane();
        jLabel1 = new javax.swing.JLabel();
        tfInfoUsuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tfHoraAtual = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfTempoLogado = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuSistema = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menuCadastroAcademico = new javax.swing.JMenuItem();
        menuCadastroCurso = new javax.swing.JMenuItem();
        menuCadastroPeriodo2 = new javax.swing.JMenuItem();
        menuCadastroPeriodo3 = new javax.swing.JMenuItem();
        menuCadastroPeriodo = new javax.swing.JMenuItem();
        menuCadastroPeriodo4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        menuCadastroPeriodo1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        desktop.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Usuário:");

        tfInfoUsuario.setEditable(false);
        tfInfoUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel2.setText("Hora:");

        tfHoraAtual.setEditable(false);
        tfHoraAtual.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel3.setText("Tempo logado: ");

        tfTempoLogado.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout desktopLayout = new javax.swing.GroupLayout(desktop);
        desktop.setLayout(desktopLayout);
        desktopLayout.setHorizontalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, desktopLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfInfoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(tfHoraAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(tfTempoLogado, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        desktopLayout.setVerticalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, desktopLayout.createSequentialGroup()
                .addContainerGap(628, Short.MAX_VALUE)
                .addGroup(desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfHoraAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(tfTempoLogado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(tfInfoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        desktop.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        desktop.setLayer(tfInfoUsuario, javax.swing.JLayeredPane.DEFAULT_LAYER);
        desktop.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        desktop.setLayer(tfHoraAtual, javax.swing.JLayeredPane.DEFAULT_LAYER);
        desktop.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        desktop.setLayer(tfTempoLogado, javax.swing.JLayeredPane.DEFAULT_LAYER);

        menuSistema.setText("Sistema");

        jMenu3.setText("Usuários");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Cadastro de Usuários");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        menuSistema.add(jMenu3);

        jMenu1.setText("Importar");

        jMenuItem6.setText("Acadêmico");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem7.setText("Turma");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem8.setText("Gabarito");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        menuSistema.add(jMenu1);

        jMenuItem3.setText("Sair");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        menuSistema.add(jMenuItem3);

        jMenuBar1.add(menuSistema);

        jMenu2.setText("Cadastros");

        menuCadastroAcademico.setText("Acadêmicos");
        menuCadastroAcademico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadastroAcademicoActionPerformed(evt);
            }
        });
        jMenu2.add(menuCadastroAcademico);

        menuCadastroCurso.setText("Cursos");
        menuCadastroCurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadastroCursoActionPerformed(evt);
            }
        });
        jMenu2.add(menuCadastroCurso);

        menuCadastroPeriodo2.setText("Disciplinas");
        menuCadastroPeriodo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadastroPeriodo2ActionPerformed(evt);
            }
        });
        jMenu2.add(menuCadastroPeriodo2);

        menuCadastroPeriodo3.setText("Perguntas");
        menuCadastroPeriodo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadastroPeriodo3ActionPerformed(evt);
            }
        });
        jMenu2.add(menuCadastroPeriodo3);

        menuCadastroPeriodo.setText("Períodos");
        menuCadastroPeriodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadastroPeriodoActionPerformed(evt);
            }
        });
        jMenu2.add(menuCadastroPeriodo);

        menuCadastroPeriodo4.setText("Provas");
        menuCadastroPeriodo4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadastroPeriodo4ActionPerformed(evt);
            }
        });
        jMenu2.add(menuCadastroPeriodo4);

        jMenuItem5.setText("Regras");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        menuCadastroPeriodo1.setText("Turmas");
        menuCadastroPeriodo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadastroPeriodo1ActionPerformed(evt);
            }
        });
        jMenu2.add(menuCadastroPeriodo1);

        jMenuItem2.setText("Unidades");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Relatórios");

        jMenuItem4.setText("Relatório de Notas");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktop)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktop)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        cadastroUsuario();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        cadastroUnidade();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        end();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void menuCadastroCursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadastroCursoActionPerformed
        cadastroCurso();
    }//GEN-LAST:event_menuCadastroCursoActionPerformed

    private void menuCadastroAcademicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadastroAcademicoActionPerformed
        cadastroAcademico();
    }//GEN-LAST:event_menuCadastroAcademicoActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        importaAcademico();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void menuCadastroPeriodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadastroPeriodoActionPerformed
        cadastroPeriodo();
    }//GEN-LAST:event_menuCadastroPeriodoActionPerformed

    private void menuCadastroPeriodo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadastroPeriodo1ActionPerformed
        cadastroTurmas();
    }//GEN-LAST:event_menuCadastroPeriodo1ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        importaTurma();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void menuCadastroPeriodo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadastroPeriodo2ActionPerformed
        cadastroDisciplinas();
    }//GEN-LAST:event_menuCadastroPeriodo2ActionPerformed

    private void menuCadastroPeriodo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadastroPeriodo3ActionPerformed
        cadastroPerguntas();
    }//GEN-LAST:event_menuCadastroPeriodo3ActionPerformed

    private void menuCadastroPeriodo4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadastroPeriodo4ActionPerformed
        cadastroProvas();
    }//GEN-LAST:event_menuCadastroPeriodo4ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        relatorioNotas();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        cadastroRegras();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        importaGabarito();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem menuCadastroAcademico;
    private javax.swing.JMenuItem menuCadastroCurso;
    private javax.swing.JMenuItem menuCadastroPeriodo;
    private javax.swing.JMenuItem menuCadastroPeriodo1;
    private javax.swing.JMenuItem menuCadastroPeriodo2;
    private javax.swing.JMenuItem menuCadastroPeriodo3;
    private javax.swing.JMenuItem menuCadastroPeriodo4;
    private javax.swing.JMenu menuSistema;
    private javax.swing.JTextField tfHoraAtual;
    private javax.swing.JTextField tfInfoUsuario;
    private javax.swing.JTextField tfTempoLogado;
    // End of variables declaration//GEN-END:variables


    private class AtualizaHora implements Runnable{
        
        @Override
        public void run() {
            Locale locale = Locale.getDefault();
            DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, locale);
            String dataExtenso = df.format(new Date());
            while (true){
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                tfHoraAtual.setText(dataExtenso + " às "+sdf.format(new Date())+", "+locale.getDisplayCountry());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}

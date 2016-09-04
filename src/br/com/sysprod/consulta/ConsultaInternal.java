package br.com.sysprod.consulta;

import br.com.sysprod.browse.BrowseAcademico;
import br.com.sysprod.browse.BrowseTurma;
import br.com.sysprod.browse.BrowseCurso;
import br.com.sysprod.browse.BrowseDisciplina;
import br.com.sysprod.browse.BrowsePergunta;
import br.com.sysprod.browse.BrowsePeriodo;
import br.com.sysprod.browse.BrowseProva;
import br.com.sysprod.browse.BrowseUnidade;
import br.com.sysprod.browse.BrowseUsuario;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Cristiano Bombazar
 */
public class ConsultaInternal extends javax.swing.JInternalFrame {

    private BrowseUsuario       browseUsuario;
    private BrowseUnidade       browseUnidade;
    private BrowseCurso         browseCurso;
    private BrowseAcademico     browseAcademico;
    private BrowsePeriodo       browsePeriodo;
    private BrowseDisciplina    browseDisciplina;
    private BrowseTurma         browseTurma;
    private BrowsePergunta      browsePergunta;
    private BrowseProva         browseProva;
    
    public ConsultaInternal() {
        initComponents();
        initComponent();
    }
    
    private void initComponent() {
        setResizable(false);
    }

    public void posicao() {
        Dimension d = this.getDesktopPane().getSize();
        this.setLocation((d.width - this.getSize().width) / 2, (d.height - this.getSize().height) / 4);
    }
    
    public void vinculaUsuario(){
        setBrowseUsuario(new BrowseUsuario());
        getContentPane().add(getBrowseUsuario());
        setSize(500, 515);
        getBrowseUsuario().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindow();
            }
        });
    }
    
    public void vinculaUnidade(){
        setBrowseUnidade(new BrowseUnidade());
        getContentPane().add(getBrowseUnidade());
        setSize(500, 450);
        getBrowseUnidade().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindow();
            }
        });
    }
    
    public void vinculaCurso(){
        setBrowseCurso(new BrowseCurso());
        getBrowseCurso().btSelecionar.setVisible(false);
        getContentPane().add(getBrowseCurso());
        setSize(500, 535);
        getBrowseCurso().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindow();
            }
        });
    }
    
    public void vinculaAcademico(){
        setBrowseAcademico(new BrowseAcademico());
        getBrowseAcademico().btSelecionar.setVisible(false);
        getContentPane().add(getBrowseAcademico());
        setSize(670, 590);
        getBrowseAcademico().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindow();
            }
        });
    }
    
      public void vinculaPeriodo(){
        setBrowsePeriodo(new BrowsePeriodo());
        getBrowsePeriodo().btSelecionar.setVisible(false);
        getContentPane().add(getBrowsePeriodo());
        setSize(460, 450);
        getBrowsePeriodo().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindow();
            }
        });
    }
      
      public void vinculaTurma(){
          setBrowseTurma(new BrowseTurma());
          getBrowseTurma().btSelecionar.setVisible(false);
          getContentPane().add(getBrowseTurma());
          setSize(650, 590);
          getBrowseTurma().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindow();
            }
        });
      }
      
      public void vinculaDisciplina(){
          setBrowseDisciplina(new BrowseDisciplina());
          getBrowseDisciplina().btSelecionar.setVisible(false);
          getContentPane().add(getBrowseDisciplina());
          setSize(500, 535);
          getBrowseDisciplina().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindow();
            }
        });
      }
      
     public void vinculaPergunta(){
          setBrowsePergunta(new BrowsePergunta());
          getBrowsePergunta().btSelecionar.setVisible(false);
          getContentPane().add(getBrowsePergunta());
          setSize(800, 570);
          getBrowsePergunta().btFechar.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  disposeWindow();;
              }
          });
                  
      }
              
     public void vinculaProva(){
         setBrowseProva(new BrowseProva());
         getContentPane().add(getBrowseProva());
         getBrowseProva().btSelecionar.setVisible(false);
         setSize(799, 507);
         getBrowseProva().btFechar.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  disposeWindow();;
              }
          });
     }

    private void disposeWindow(){
        dispose();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public BrowseUsuario getBrowseUsuario() {
        return browseUsuario;
    }

    public void setBrowseUsuario(BrowseUsuario browseUsuario) {
        this.browseUsuario = browseUsuario;
    }

    public BrowseUnidade getBrowseUnidade() {
        return browseUnidade;
    }

    public void setBrowseUnidade(BrowseUnidade browseUnidade) {
        this.browseUnidade = browseUnidade;
    }

    public BrowseCurso getBrowseCurso() {
        return browseCurso;
    }

    public void setBrowseCurso(BrowseCurso browseCurso) {
        this.browseCurso = browseCurso;
    }

    public BrowseAcademico getBrowseAcademico() {
        return browseAcademico;
    }

    public void setBrowseAcademico(BrowseAcademico browseAcademico) {
        this.browseAcademico = browseAcademico;
    }

    public BrowsePeriodo getBrowsePeriodo() {
        return browsePeriodo;
    }

    public void setBrowsePeriodo(BrowsePeriodo browsePeriodo) {
        this.browsePeriodo = browsePeriodo;
    }

    public BrowseTurma getBrowseTurma() {
        return browseTurma;
    }

    public void setBrowseTurma(BrowseTurma browseAcademicoFase) {
        this.browseTurma = browseAcademicoFase;
    }

    public BrowseDisciplina getBrowseDisciplina() {
        return browseDisciplina;
    }

    public void setBrowseDisciplina(BrowseDisciplina browseDisciplina) {
        this.browseDisciplina = browseDisciplina;
    }

    public BrowsePergunta getBrowsePergunta() {
        return browsePergunta;
    }

    public void setBrowsePergunta(BrowsePergunta browsePergunta) {
        this.browsePergunta = browsePergunta;
    }

    public BrowseProva getBrowseProva() {
        return browseProva;
    }

    public void setBrowseProva(BrowseProva browseProva) {
        this.browseProva = browseProva;
    }
}

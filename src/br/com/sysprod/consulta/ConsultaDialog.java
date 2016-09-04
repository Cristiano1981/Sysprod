package br.com.sysprod.consulta;

import br.com.sysprod.browse.BrowseAcademico;
import br.com.sysprod.browse.BrowseCurso;
import br.com.sysprod.browse.BrowseDisciplina;
import br.com.sysprod.browse.BrowsePergunta;
import br.com.sysprod.browse.BrowsePeriodo;
import br.com.sysprod.browse.BrowseProva;
import br.com.sysprod.browse.BrowseTurma;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Cristiano Bombazar
 */
public class ConsultaDialog extends javax.swing.JDialog {

    private BrowseCurso      browseCurso      = null;
    private BrowsePeriodo    browsePeriodo    = null;
    private BrowseAcademico  browseAcademico  = null;
    private BrowseDisciplina browseDisciplina = null;
    private BrowseTurma      browseTurma      = null;
    private BrowsePergunta   browsePergunta   = null;
    private BrowseProva      browseProva      = null;
            
    public ConsultaDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public void initForm(){
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    public void vinculaCurso(){
        setTitle("Consulta de Curso");
        setBrowseCurso(new BrowseCurso());
        getBrowseCurso().btSelecionar.setVisible(true);
        getContentPane().add(getBrowseCurso());
        setSize(500, 550);
        getBrowseCurso().btSelecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
        getBrowseCurso().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
    }
    
    public void vinculaPeriodo(){
        setTitle("Consulta de períodos");
        setBrowsePeriodo(new BrowsePeriodo());
        getBrowsePeriodo().btSelecionar.setVisible(true);
        getContentPane().add(getBrowsePeriodo());
        setSize(460, 450);
         getBrowsePeriodo().btSelecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
        getBrowsePeriodo().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
    }
    
    public void vinculaAcademico(){
        setTitle("Consulta de acadêmicos");
        setBrowseAcademico(new BrowseAcademico());
        getBrowseAcademico().btSelecionar.setVisible(true);
        getContentPane().add(getBrowseAcademico());
         setSize(670, 605);
         getBrowseAcademico().btSelecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
        getBrowseAcademico().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
    }
    
    public void vinculaDisciplina(){
        setTitle("Consulta de Disciplinas");
        setBrowseDisciplina(new BrowseDisciplina());
        getBrowseDisciplina().btSelecionar.setVisible(true);
        getContentPane().add(getBrowseDisciplina());
        setSize(500, 550);
        getBrowseDisciplina().btSelecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
        getBrowseDisciplina().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
    }
    
    public void vinculaTurma(){
        setTitle("Consulta de Turmas");
        setBrowseTurma(new BrowseTurma());
        getBrowseTurma().btSelecionar.setVisible(true);
        getContentPane().add(getBrowseTurma());
        setSize(650, 590);
        getBrowseTurma().btSelecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
        getBrowseTurma().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
    }
    
    public void vinculaPergunta(){
        setTitle("Consulta de Perguntas");
        setBrowsePergunta(new BrowsePergunta());
        getBrowsePergunta().btSelecionar.setVisible(true);
        getContentPane().add(getBrowsePergunta());
        setSize(800, 570);
        getBrowsePergunta().btSelecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
        getBrowsePergunta().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
    }
    
    public void vinculaProva(){
        setTitle("Consulta de Provas");
        setBrowseProva(new BrowseProva());
        getBrowseProva().btSelecionar.setVisible(true);
        getContentPane().add(getBrowseProva());
        setSize(799, 507);
         getBrowseProva().btSelecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
        getBrowseProva().btFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindows();
            }
        });
    }
     
    
    private void disposeWindows(){
        super.dispose();
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public BrowseCurso getBrowseCurso() {
        return browseCurso;
    }

    public void setBrowseCurso(BrowseCurso browseCurso) {
        this.browseCurso = browseCurso;
    }

    public BrowsePeriodo getBrowsePeriodo() {
        return browsePeriodo;
    }

    public void setBrowsePeriodo(BrowsePeriodo browsePeriodo) {
        this.browsePeriodo = browsePeriodo;
    }

    public BrowseAcademico getBrowseAcademico() {
        return browseAcademico;
    }

    public void setBrowseAcademico(BrowseAcademico browseAcademico) {
        this.browseAcademico = browseAcademico;
    }

    public BrowseDisciplina getBrowseDisciplina() {
        return browseDisciplina;
    }

    public void setBrowseDisciplina(BrowseDisciplina browseDisciplina) {
        this.browseDisciplina = browseDisciplina;
    }

    public BrowseTurma getBrowseTurma() {
        return browseTurma;
    }

    public void setBrowseTurma(BrowseTurma browseTurma) {
        this.browseTurma = browseTurma;
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

package br.com.sysprod.update;

import br.com.sysprod.interfaces.Formulario;
import br.com.sysprod.vo.Regra;
import javax.swing.JDialog;

public class UpdateRegra extends JDialog implements Formulario{

    
    public UpdateRegra(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }
        
    @Override
    public void initForm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initComponent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean validateForm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void end() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void novo(){
        
    }
    
    public void editar(Regra regra){
        
    }

}
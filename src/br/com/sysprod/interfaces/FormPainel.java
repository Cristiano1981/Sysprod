
package br.com.sysprod.interfaces;

/**
 * @author Cristiano Bombazar
 */
public interface FormPainel {
    
   public void initComponent();
   public void propriedadeTable();
   public void atualizaTabela();
   public void filtro();
   public void novo();
   public void editar();
   public void excluir() throws Exception;
   
}

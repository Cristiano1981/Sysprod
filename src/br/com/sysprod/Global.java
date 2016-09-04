package br.com.sysprod;

import br.com.sysprod.vo.Unidade;
import br.com.sysprod.vo.Usuario;

/**
 * @author Cristiano Bombazar
 */
public class Global {
    
    private static Unidade unidade;
    private static Usuario usuario;

    public static Unidade getUnidade() {
        return unidade;
    }

    public static void setUnidade(Unidade aUnidade) {
        unidade = aUnidade;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario aUsuario) {
        usuario = aUsuario;
    }
}

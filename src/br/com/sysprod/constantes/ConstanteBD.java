package br.com.sysprod.constantes;

/**
 *
 * @author Cristiano Bombazar
 */
public class ConstanteBD {
    
    //constantes para usar nas mensagens de erro com o banco de dados
    public static final String MESSAGE_BD_NOT_EXISTS = "DataBase n�o existe. Favor, verifique o nome do banco de dados nas configura��es. Database: ";
    public static final String MESSAGE_BD_AUTHENTICATION_FAILED = "Usu�rio/senha inv�lidos. Favor, verifique o usu�rio e senha nas configura��es.";
    public static final String MESSAGE_PG_HBA = "O pg_hba n�o est� configurado para receber conex�es. V� na pasta de instala��o do postgres e adicione as permiss�es necess�rias.";
    public static final String MESSAGE_BD_CONNECTION_REFUSED = "N�o foi poss�vel conectar ao servidor. Poss�veis causas:\n"
                                                              +"O servi�o do banco de dados n�o est� iniciado.\n"
                                                              +"Porta do banco de dados diferente do arquivo de configura��o.\n"
                                                              +"O computador n�o est� localizando o servidor.\n"
                                                              +"Favor, verificar configura��es de rede e de sistema.";


    //mensagens comuns de erros que o banco de dados gera.
    public static final String CONNECTION_DATABASE_DOES_NOT_EXIST = "does not exist";
    public static final String CONNECTION_FAILED_AUTHENTICATION = "password authentication failed for user";
    public static final String CONNECTION_REFUSED = "Connection refused";
    public static final String CONNECTION_REFUSED_PTBR = "Conexão negada. Verifique se o nome da m�quina e a porta est�o corretos e se o postmaster est� aceitando conex�es TCP/IP";
    public static final String CONNECTION_FAILED = "A tentativa de conex�o falhou";
    public static final String CONNECTION_FAILED_2 = "Tentativa de conex�o falhou";
    public static final String CONNECTION_PG_HBA = "no pg_hba.conf entry for host";
    public static final String DRIVER_ERRO = "Verificar drievr do banco de dados. Por favor, instale o driver OBDC/JDBC";
    
    public static final int DONE = 1;
    public static final int ERR = 2;
    
}

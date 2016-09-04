package br.com.sysprod.constantes;

/**
 *
 * @author Cristiano Bombazar
 */
public class ConstanteBD {
    
    //constantes para usar nas mensagens de erro com o banco de dados
    public static final String MESSAGE_BD_NOT_EXISTS = "DataBase não existe. Favor, verifique o nome do banco de dados nas configurações. Database: ";
    public static final String MESSAGE_BD_AUTHENTICATION_FAILED = "Usuário/senha inválidos. Favor, verifique o usuário e senha nas configurações.";
    public static final String MESSAGE_PG_HBA = "O pg_hba não está configurado para receber conexões. Vá na pasta de instalação do postgres e adicione as permissões necessárias.";
    public static final String MESSAGE_BD_CONNECTION_REFUSED = "Não foi possível conectar ao servidor. Possíveis causas:\n"
                                                              +"O serviço do banco de dados não está iniciado.\n"
                                                              +"Porta do banco de dados diferente do arquivo de configuração.\n"
                                                              +"O computador não está localizando o servidor.\n"
                                                              +"Favor, verificar configurações de rede e de sistema.";


    //mensagens comuns de erros que o banco de dados gera.
    public static final String CONNECTION_DATABASE_DOES_NOT_EXIST = "does not exist";
    public static final String CONNECTION_FAILED_AUTHENTICATION = "password authentication failed for user";
    public static final String CONNECTION_REFUSED = "Connection refused";
    public static final String CONNECTION_REFUSED_PTBR = "ConexÃ£o negada. Verifique se o nome da máquina e a porta estão corretos e se o postmaster está aceitando conexões TCP/IP";
    public static final String CONNECTION_FAILED = "A tentativa de conexão falhou";
    public static final String CONNECTION_FAILED_2 = "Tentativa de conexão falhou";
    public static final String CONNECTION_PG_HBA = "no pg_hba.conf entry for host";
    public static final String DRIVER_ERRO = "Verificar drievr do banco de dados. Por favor, instale o driver OBDC/JDBC";
    
    public static final int DONE = 1;
    public static final int ERR = 2;
    
}

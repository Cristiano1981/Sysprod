package br.com.sysprod.interfaces;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Cristiano Bombazar
 */
public interface Crud<E> {
    
    public boolean saveOrUpdate(E e) throws SQLException;
    public boolean delete(E e) throws SQLException;
    public E find(Integer codigo) throws SQLException;
    public String getDefaultQuery();
    public Integer nextCode() throws SQLException;
    public List<E> queryAll(String filtro, String order) throws SQLException;
    
}

package br.com.sysprod.vo;

import java.util.List;
import java.util.Objects;

/**
 * @author Cristiano Bombazar
 */

public class ProvaDet {

    private Integer                 id;
    private Pergunta                pergunta;
    private List<ProvaDetItem>      listaProvaDetItem;
    private List<ProvaDetItem>      listaProvaDetItemExcluir;
    private List<ProvaDetAcademico> listaProvaDetAcademico;
    private List<ProvaDetAcademico> listaProvaDetAcademicoExcluir;

    public ProvaDet() {
    }

    public ProvaDet(Integer id, Pergunta pergunta, List<ProvaDetItem>      listaProvaDetItem     , List<ProvaDetItem> listaProvaDetItemExcluir,
                                                   List<ProvaDetAcademico> listaProvaDetAcademico, List<ProvaDetAcademico> listaProvaDetAcademicoExcluir) {
        this.id                            = id;
        this.pergunta                      = pergunta;
        this.listaProvaDetItem             = listaProvaDetItem;
        this.listaProvaDetItemExcluir      = listaProvaDetItemExcluir;
        this.listaProvaDetAcademico        = listaProvaDetAcademico;
        this.listaProvaDetAcademicoExcluir = listaProvaDetAcademicoExcluir;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public List<ProvaDetItem> getListaProvaDetItem() {
        return listaProvaDetItem;
    }

    public void setListaProvaDetItem(List<ProvaDetItem> listaProvaDetItem) {
        this.listaProvaDetItem = listaProvaDetItem;
    }

    public List<ProvaDetItem> getListaProvaDetItemExcluir() {
        return listaProvaDetItemExcluir;
    }

    public void setListaProvaDetItemExcluir(List<ProvaDetItem> listaProvaDetItemExcluir) {
        this.listaProvaDetItemExcluir = listaProvaDetItemExcluir;
    }

    public List<ProvaDetAcademico> getListaProvaDetAcademico() {
        return listaProvaDetAcademico;
    }

    public void setListaProvaDetAcademico(List<ProvaDetAcademico> listaProvaDetAcademico) {
        this.listaProvaDetAcademico = listaProvaDetAcademico;
    }

    public List<ProvaDetAcademico> getListaProvaDetAcademicoExcluir() {
        return listaProvaDetAcademicoExcluir;
    }

    public void setListaProvaDetAcademicoExcluir(List<ProvaDetAcademico> listaProvaDetAcademicoExcluir) {
        this.listaProvaDetAcademicoExcluir = listaProvaDetAcademicoExcluir;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProvaDet other = (ProvaDet) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.pergunta, other.pergunta)) {
            return false;
        }
        if (!Objects.equals(this.listaProvaDetItem, other.listaProvaDetItem)) {
            return false;
        }
        return true;
    }

}

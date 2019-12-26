package beans;

import controladores.SistemaControlador;

import entidades.Sistema;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import util.Util;

/**
 *
 * @author Jorge Luis
 */
@ManagedBean
@ViewScoped
public class SistemaBean implements Serializable {

    private Sistema sistema;
    private Sistema cargoSelecionado;
    private List<Sistema> cargo;
    private List<Sistema> revisoes;
    private boolean ver;
    private List<String> colunas;
    private String colunaSelecionada;
    private String texto;
    private String tipoExportacao;

    @PostConstruct
    public void init() {
        listar();
        this.colunas = Util.atributosClasse(Sistema.class);
        this.colunaSelecionada = this.colunas.get(0);
        this.tipoExportacao = "pdf";
    }

    public void pesquisar() {

        String hql = 
                "SELECT vo FROM Sistema vo" + '\n' +
                " WHERE UPPER(CAST(vo." + this.colunaSelecionada + " as text)) " + '\n' +
                " LIKE '" + this.texto.toUpperCase() + "%' " + '\n' +
                " AND vo.nivel!='SUPER'" +'\n' +
                " ORDER BY vo." + this.colunaSelecionada + " ASC";

        this.cargo = new SistemaControlador().listar(hql);

    }

    public String pegarStatus(String status) {
        switch (status) {
            case "A":
                return "Ativo";
            case "I":
                return "Inativo";
            default:
                return "Bloqueado";
        }
    }

    private void listar() {
        this.ver = false;
        this.revisoes = new ArrayList<>();
        this.cargoSelecionado = new Sistema();
        this.cargo = new ArrayList<>();
        String hql = "SELECT vo FROM Sistema vo WHERE vo.nivel!='SUPER'";
        this.cargo = new SistemaControlador().listar(hql);
    }

    public void salvar() {

        if (new SistemaControlador().salvar(this.sistema)) {
            listar();
            Util.atualizarForm("sistema");
            Util.executarJavaScript("PF('dlgcriar').hide();");
        }
    }

    public void inserir() {
        this.ver = false;
        this.sistema = new Sistema();
        resetarFormulario();
    }

    public void editar() {
        this.ver = false;
        this.sistema = this.cargoSelecionado;
        resetarFormulario();
    }

    public void excluir() {
        if (new SistemaControlador().excluir(this.cargoSelecionado)) {
            listar();
        }
    }

    public void ver() {
        this.ver = true;
        this.sistema = this.cargoSelecionado;
        resetarFormulario();

    }

    public void verRevisoes() {
        this.revisoes = new ArrayList<>();
        this.sistema = this.cargoSelecionado;
        resetarFormulario();

        AuditReader reader = AuditReaderFactory.get(Util.pegarSessao());

        List<Number> rev = reader.getRevisions(Sistema.class, this.sistema.getCodSis());
        for (Number revisoe : rev) {
            Sistema o = reader.find(Sistema.class, this.sistema.getCodSis(), revisoe);
            this.revisoes.add(o);
        }
    }

    public void exportar() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        ServletContext servletContext = (ServletContext) context.getContext();
        String arquivoJasper = "/WEB-INF/relatorios/cargo.jasper";
        try {
            Util.gerar(request, response, arquivoJasper, servletContext,
                    Util.pegarConexao(), this.tipoExportacao, "cargo");
        } catch (IOException ex) {
            Logger.getLogger(SistemaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().responseComplete();

    }

    public void resetarFormulario() {
        Util.resetarFormulario("criar");
    }

    public Sistema getSistema() {
        return sistema;
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }

    public List<Sistema> getSistemas() {
        return cargo;
    }

    public void setSistemas(List<Sistema> cargo) {
        this.cargo = cargo;
    }

    public Sistema getSistemaSelecionado() {
        return cargoSelecionado;
    }

    public void setSistemaSelecionado(Sistema cargoSelecionado) {
        this.cargoSelecionado = cargoSelecionado;
    }

    public boolean isVer() {
        return ver;
    }

    public void setVer(boolean ver) {
        this.ver = ver;
    }

    public List<Sistema> getRevisoes() {
        return revisoes;
    }

    public void setRevisoes(List<Sistema> revisoes) {
        this.revisoes = revisoes;
    }

    public List<String> getColunas() {
        return colunas;
    }

    public void setColunas(List<String> colunas) {
        this.colunas = colunas;
    }

    public String getColunaSelecionada() {
        return colunaSelecionada;
    }

    public void setColunaSelecionada(String colunaSelecionada) {
        this.colunaSelecionada = colunaSelecionada;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTipoExportacao() {
        return tipoExportacao;
    }

    public void setTipoExportacao(String tipoExportacao) {
        this.tipoExportacao = tipoExportacao;
    }

}

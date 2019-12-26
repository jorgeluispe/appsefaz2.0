package beans;

import controladores.OrgaoControlador;

import entidades.Orgao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.primefaces.event.FileUploadEvent;

import util.Util;

/**
 *
 * @author Jorge Luis
 */
@ManagedBean
@ViewScoped
public class OrgaoBean implements Serializable {

    private Orgao orgao;
    private Orgao cargoSelecionado;
    private List<Orgao> cargo;
    private List<Orgao> revisoes;
    private boolean ver;
    private List<String> colunas;
    private String colunaSelecionada;
    private String texto;
    private String tipoExportacao;

    @PostConstruct
    public void init() {
        listar();
        this.colunas = Util.atributosClasse(Orgao.class);
        this.colunaSelecionada = this.colunas.get(0);
        this.tipoExportacao = "pdf";
    }

    public void pesquisar() {

        String hql = 
                "SELECT vo FROM Orgao vo" + '\n' +
                " WHERE UPPER(CAST(vo." + this.colunaSelecionada + " as text)) " + '\n' +
                " LIKE '" + this.texto.toUpperCase() + "%' " + '\n' +
                " AND vo.nivel!='SUPER'" +'\n' +
                " ORDER BY vo." + this.colunaSelecionada + " ASC";

        this.cargo = new OrgaoControlador().listar(hql);

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
        this.cargoSelecionado = new Orgao();
        this.cargo = new ArrayList<>();
        String hql = "SELECT vo FROM Orgao vo WHERE vo.nivel!='SUPER'";
        this.cargo = new OrgaoControlador().listar(hql);
    }

    public void salvar() {

        if (new OrgaoControlador().salvar(this.orgao)) {
            listar();
            Util.atualizarForm("orgao");
            Util.executarJavaScript("PF('dlgcriar').hide();");
        }
    }

    public void inserir() {
        this.ver = false;
        this.orgao = new Orgao();
        resetarFormulario();
    }

    public void editar() {
        this.ver = false;
        this.orgao = this.cargoSelecionado;
        resetarFormulario();
    }

    public void excluir() {
        if (new OrgaoControlador().excluir(this.cargoSelecionado)) {
            listar();
        }
    }

    public void ver() {
        this.ver = true;
        this.orgao = this.cargoSelecionado;
        resetarFormulario();

    }

    public void verRevisoes() {
        this.revisoes = new ArrayList<>();
        this.orgao = this.cargoSelecionado;
        resetarFormulario();

        AuditReader reader = AuditReaderFactory.get(Util.pegarSessao());

        List<Number> rev = reader.getRevisions(Orgao.class, this.orgao.getCodOrg());
        for (Number revisoe : rev) {
            Orgao o = reader.find(Orgao.class, this.orgao.getCodOrg(), revisoe);
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
            Logger.getLogger(OrgaoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().responseComplete();

    }

    public void resetarFormulario() {
        Util.resetarFormulario("criar");
    }

    public Orgao getOrgao() {
        return orgao;
    }

    public void setOrgao(Orgao orgao) {
        this.orgao = orgao;
    }

    public List<Orgao> getOrgaos() {
        return cargo;
    }

    public void setOrgaos(List<Orgao> cargo) {
        this.cargo = cargo;
    }

    public Orgao getOrgaoSelecionado() {
        return cargoSelecionado;
    }

    public void setOrgaoSelecionado(Orgao cargoSelecionado) {
        this.cargoSelecionado = cargoSelecionado;
    }

    public boolean isVer() {
        return ver;
    }

    public void setVer(boolean ver) {
        this.ver = ver;
    }

    public List<Orgao> getRevisoes() {
        return revisoes;
    }

    public void setRevisoes(List<Orgao> revisoes) {
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

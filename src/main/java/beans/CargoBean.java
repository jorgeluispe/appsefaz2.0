package beans;

import controladores.CargoControlador;

import entidades.Cargo;

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
public class CargoBean implements Serializable {

    private Cargo usuario;
    private Cargo cargoSelecionado;
    private List<Cargo> cargo;
    private List<Cargo> revisoes;
    private boolean ver;
    private List<String> colunas;
    private String colunaSelecionada;
    private String texto;
    private String tipoExportacao;

    @PostConstruct
    public void init() {
        listar();
        this.colunas = Util.atributosClasse(Cargo.class);
        this.colunaSelecionada = this.colunas.get(0);
        this.tipoExportacao = "pdf";
    }

    public void pesquisar() {

        String hql = 
                "SELECT vo FROM Cargo vo" + '\n' +
                " WHERE UPPER(CAST(vo." + this.colunaSelecionada + " as text)) " + '\n' +
                " LIKE '" + this.texto.toUpperCase() + "%' " + '\n' +
                " AND vo.nivel!='SUPER'" +'\n' +
                " ORDER BY vo." + this.colunaSelecionada + " ASC";

        this.cargo = new CargoControlador().listar(hql);

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
        this.cargoSelecionado = new Cargo();
        this.cargo = new ArrayList<>();
        String hql = "SELECT vo FROM Cargo vo WHERE vo.nivel!='SUPER'";
        this.cargo = new CargoControlador().listar(hql);
    }

    public void salvar() {

        if (new CargoControlador().salvar(this.usuario)) {
            listar();
            Util.atualizarForm("usuario");
            Util.executarJavaScript("PF('dlgcriar').hide();");
        }
    }

    public void inserir() {
        this.ver = false;
        this.usuario = new Cargo();
        resetarFormulario();
    }

    public void editar() {
        this.ver = false;
        this.usuario = this.cargoSelecionado;
        resetarFormulario();
    }

    public void excluir() {
        if (new CargoControlador().excluir(this.cargoSelecionado)) {
            listar();
        }
    }

    public void ver() {
        this.ver = true;
        this.usuario = this.cargoSelecionado;
        resetarFormulario();

    }

    public void verRevisoes() {
        this.revisoes = new ArrayList<>();
        this.usuario = this.cargoSelecionado;
        resetarFormulario();

        AuditReader reader = AuditReaderFactory.get(Util.pegarSessao());

        List<Number> rev = reader.getRevisions(Cargo.class, this.usuario.getCodCar());
        for (Number revisoe : rev) {
            Cargo o = reader.find(Cargo.class, this.usuario.getCodCar(), revisoe);
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
            Logger.getLogger(CargoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().responseComplete();

    }

    public void resetarFormulario() {
        Util.resetarFormulario("criar");
    }

    public Cargo getCargo() {
        return usuario;
    }

    public void setCargo(Cargo usuario) {
        this.usuario = usuario;
    }

    public List<Cargo> getCargos() {
        return cargo;
    }

    public void setCargos(List<Cargo> cargo) {
        this.cargo = cargo;
    }

    public Cargo getCargoSelecionado() {
        return cargoSelecionado;
    }

    public void setCargoSelecionado(Cargo cargoSelecionado) {
        this.cargoSelecionado = cargoSelecionado;
    }

    public boolean isVer() {
        return ver;
    }

    public void setVer(boolean ver) {
        this.ver = ver;
    }

    public List<Cargo> getRevisoes() {
        return revisoes;
    }

    public void setRevisoes(List<Cargo> revisoes) {
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

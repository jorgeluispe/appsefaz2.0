package controladores;

import entidades.Sistema;

import java.io.Serializable;
import java.util.List;

import util.DAOImpl;
import util.Util;

/**
 *
 * @author Jorge Luis
 */
public class SistemaControlador implements Serializable {

    private final DAOImpl<Sistema> dao;

    public SistemaControlador() {
        this.dao = new DAOImpl<>(Util.pegarSessao(), Sistema.class);
    }

    public boolean salvar(Sistema usuario) {
        boolean ok;
        try {
            if (usuario.getCodSis() == 0) {
                String hql = 
                        "SELECT u FROM Sistema u" + '\n' +
                        " WHERE u.descSis='" + usuario.getDescSis() + "'" + '\n' ;
                Sistema verifica = this.dao.carregar(hql);

                if (verifica == null) {
                    usuario.setDescSis(usuario.getDescSis());
                    this.dao.salvar(usuario);
                    Util.criarAviso("Cargo salvo com sucesso");
                    ok = true;
                } else {
                    Util.criarAvisoErro("Cargo ja existe");
                    ok = false;
                }
            } else {

                String hql = 
                        "SELECT u FROM Sistema u" + '\n' +
                        " WHERE (u.descCar='" + usuario.getDescSis()+ "')" + '\n';

                    Sistema verifica = this.dao.carregar(hql);

                if (verifica == null) {
                    usuario.setDescSis(usuario.getDescSis());
                    this.dao.atualizar(usuario);
                    Util.criarAviso("Sistema atualizado com sucesso");
                    ok = true;
                } else {
                    Util.criarAvisoErro("Sistema ja existe");
                    ok = false;
                }
            }
        } catch (Exception e) {
            Util.criarAvisoErro("erro");
            ok = false;
        }
        return ok;
    }

    public boolean excluir(Sistema usuario) {
        boolean ok;
        try {
            this.dao.excluir(usuario);
            Util.criarAviso("Sistema excluido com sucesso");
            ok = true;
        } catch (Exception e) {
            Util.criarAvisoErro("erro");
            ok = false;
        }
        return ok;
    }

    public List<Sistema> listar(String hql) {
        return this.dao.listar(hql);
    }

    public Sistema carregar(String hql) {
        return this.dao.carregar(hql);
    }

}

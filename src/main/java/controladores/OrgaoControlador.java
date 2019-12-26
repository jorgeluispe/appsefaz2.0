package controladores;

import entidades.Orgao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import util.DAOImpl;
import util.Util;

/**
 *
 * @author Jorge Luis
 */
public class OrgaoControlador implements Serializable {

    private final DAOImpl<Orgao> dao;

    public OrgaoControlador() {
        this.dao = new DAOImpl<>(Util.pegarSessao(), Orgao.class);
    }

    public boolean salvar(Orgao usuario) {
        boolean ok;
        try {
            if (usuario.getCodOrg() == 0) {
                String hql = 
                        "SELECT u FROM Orgao u" + '\n' +
                        " WHERE u.descOrg='" + usuario.getDescOrg() + "'" + '\n' ;
                Orgao verifica = this.dao.carregar(hql);

                if (verifica == null) {
                    usuario.setDescOrg(usuario.getDescOrg());
                    this.dao.salvar(usuario);
                    Util.criarAviso("Cargo salvo com sucesso");
                    ok = true;
                } else {
                    Util.criarAvisoErro("Cargo ja existe");
                    ok = false;
                }
            } else {

                String hql = 
                        "SELECT u FROM Usuario u" + '\n' +
                        " WHERE (u.usuario='" + usuario.getDescOrg()+ "')" + '\n';

                    Orgao verifica = this.dao.carregar(hql);

                if (verifica == null) {
                    usuario.setDescOrg(usuario.getDescOrg());
                    this.dao.atualizar(usuario);
                    Util.criarAviso("usuario atualizado com sucesso");
                    ok = true;
                } else {
                    Util.criarAvisoErro("usuario ja existe");
                    ok = false;
                }
            }
        } catch (Exception e) {
            Util.criarAvisoErro("erro");
            ok = false;
        }
        return ok;
    }

    public boolean excluir(Orgao usuario) {
        boolean ok;
        try {
            this.dao.excluir(usuario);
            Util.criarAviso("Orgão excluido com sucesso");
            ok = true;
        } catch (Exception e) {
            Util.criarAvisoErro("erro");
            ok = false;
        }
        return ok;
    }

    public List<Orgao> listar(String hql) {
        return this.dao.listar(hql);
    }

    public Orgao carregar(String hql) {
        return this.dao.carregar(hql);
    }

}

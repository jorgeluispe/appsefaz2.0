package controladores;

import entidades.Cargo;

import java.io.Serializable;
import java.util.List;

import util.DAOImpl;
import util.Util;

/**
 *
 * @author Jorge Luis
 */
public class CargoControlador implements Serializable {

    private final DAOImpl<Cargo> dao;

    public CargoControlador() {
        this.dao = new DAOImpl<>(Util.pegarSessao(), Cargo.class);
    }

    public boolean salvar(Cargo usuario) {
        boolean ok;
        try {
            if (usuario.getCodCar() == 0) {
                String hql = 
                        "SELECT u FROM Cargo u" + '\n' +
                        " WHERE u.descCar='" + usuario.getDescCar() + "'" + '\n' ;
                Cargo verifica = this.dao.carregar(hql);

                if (verifica == null) {
                    usuario.setDescCar(usuario.getDescCar());
                    this.dao.salvar(usuario);
                    Util.criarAviso("Cargo salvo com sucesso");
                    ok = true;
                } else {
                    Util.criarAvisoErro("Cargo ja existe");
                    ok = false;
                }
            } else {

                String hql = 
                        "SELECT u FROM Cargo u" + '\n' +
                        " WHERE (u.descCar='" + usuario.getDescCar()+ "')" + '\n';

                    Cargo verifica = this.dao.carregar(hql);

                if (verifica == null) {
                    usuario.setDescCar(usuario.getDescCar());
                    this.dao.atualizar(usuario);
                    Util.criarAviso("Cargo atualizado com sucesso");
                    ok = true;
                } else {
                    Util.criarAvisoErro("Cargo ja existe");
                    ok = false;
                }
            }
        } catch (Exception e) {
            Util.criarAvisoErro("erro");
            ok = false;
        }
        return ok;
    }

    public boolean excluir(Cargo usuario) {
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

    public List<Cargo> listar(String hql) {
        return this.dao.listar(hql);
    }

    public Cargo carregar(String hql) {
        return this.dao.carregar(hql);
    }

}

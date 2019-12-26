package util;

import java.util.List;

/**
 *
 * @author Jorge Luis
 */
public interface DAO<T> {

    public void salvar(T t);

    public void atualizar(T t);

    public void excluir(T t);

    public T carregar(String hql);

    public List<T> listar(String hql);
}

package util;

import entidades.Revisao;

import org.hibernate.envers.RevisionListener;

/**
 *
 * @author Jorge Luis
 */
public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        Revisao revision = (Revisao) revisionEntity;
    }

}

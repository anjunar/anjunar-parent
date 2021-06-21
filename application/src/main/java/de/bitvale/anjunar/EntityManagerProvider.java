package de.bitvale.anjunar;

import javax.ejb.Stateful;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
public class EntityManagerProvider {


/*
    EntityManagerFactory managerFactory;

    public EntityManagerFactory createFactory() {
        if (managerFactory == null) {
            managerFactory = Persistence
                    .createEntityManagerFactory("main");
        }
        return managerFactory;
    }

    @Produces
    public EntityManager createEntityManager() {
        return createFactory().createEntityManager();
    }
*/


    @Produces
    @PersistenceContext(unitName = "main")
    static EntityManager entityManager;


}

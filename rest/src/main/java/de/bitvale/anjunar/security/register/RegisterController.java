package de.bitvale.anjunar.security.register;

import de.bitvale.common.rest.api.Link;
import de.bitvale.common.security.Identity;
import de.bitvale.common.security.Role;
import de.bitvale.common.security.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;

@Path("security/register")
@ApplicationScoped
public class RegisterController {

    private final EntityManager entityManager;

    private final Identity identity;

    @Inject
    public RegisterController(EntityManager entityManager, Identity identity) {
        this.entityManager = entityManager;
        this.identity = identity;
    }

    public RegisterController() {
        this(null, null);
    }

    @GET
    @Produces("application/json")
    public RegisterResource register() {
        RegisterResource resource = new RegisterResource();

        resource.addAction(new Link("security/register", "POST", "register"));

        return resource;
    }

    @POST
    @Consumes("application/json")
    @Transactional
    public void register(@Valid RegisterResource resource) {

        User user = new User();

        user.setFirstName(resource.getFirstName());
        user.setLastName(resource.getLastName());
        user.setBirthDate(resource.getBirthdate());
        user.setPassword(resource.getPassword());
        user.setEnabled(true);

        entityManager.persist(user);

        Role userRole = entityManager.createQuery("select r from Role r where r.name = :role", Role.class)
                .setParameter("role", "User")
                .getSingleResult();

        user.getRoles().add(userRole);

        identity.authenticate(user);

    }

}

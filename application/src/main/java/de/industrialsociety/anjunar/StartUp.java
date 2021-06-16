package de.industrialsociety.anjunar;

import de.industrialsociety.common.security.IdentityService;
import de.industrialsociety.common.security.Relationship;
import de.industrialsociety.common.security.Role;
import de.industrialsociety.common.security.User;
import de.industrialsociety.common.security.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import java.time.LocalDate;

@ApplicationScoped
public class StartUp {

    @Transactional
    public void init(@Observes @Initialized(ApplicationScoped.class) ServletContext init, IdentityService service) {

        LocalDate birthdate = LocalDate.of(1980, 4, 1);
        User patrick = service.findUser("Patrick", "Bittner", birthdate);

        if (patrick == null) {
            Role administratorRole = new Role();
            administratorRole.setName("Administrator");
            service.saveRole(administratorRole);

            Role userRole = new Role();
            userRole.setName("User");
            service.saveRole(userRole);

            Role guestRole = new Role();
            guestRole.setName("Guest");
            service.saveRole(guestRole);


            patrick = new User();
            patrick.setEnabled(true);
            patrick.setLastName("Bittner");
            patrick.setFirstName("Patrick");
            patrick.setBirthDate(birthdate);
            patrick.setPassword("patrick");
            service.saveUser(patrick);
            Relationship relationship = new Relationship();
            relationship.setUser(patrick);
            relationship.setGroup(administratorRole);
            service.saveRelationShip(relationship);


        }

    }

}

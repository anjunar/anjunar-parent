package de.bitvale.anjunar;

import de.bitvale.common.security.IdentityService;
import de.bitvale.common.security.Role;
import de.bitvale.common.security.User;
import javax.servlet.ServletContext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Locale;

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
            patrick.setEmail("patrick.bittner@hamburg.de");
            patrick.setLanguage(Locale.forLanguageTag("en-DE"));
            patrick.getRoles().add(administratorRole);
            service.saveUser(patrick);


        }

    }

}

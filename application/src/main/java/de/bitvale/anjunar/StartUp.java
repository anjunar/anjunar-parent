package de.bitvale.anjunar;

import de.bitvale.common.security.IdentityService;
import de.bitvale.common.security.Image;
import de.bitvale.common.security.Role;
import de.bitvale.common.security.User;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletContext;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

            try {
                URL picture = getClass()
                        .getClassLoader()
                        .getResource("META-INF/resources/user.png");
                InputStream inputStream = picture.openStream();
                byte[] bytes = new byte[inputStream.available()];
                IOUtils.readFully(inputStream, bytes);
                Image image = new Image();
                image.setName("user.png");
                image.setData(bytes);
                image.setLastModified(LocalDateTime.now());
                image.setType("image");
                image.setSubType("png");
                patrick.setPicture(image);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}

package de.bitvale.common.security.enterprise;

import javax.security.enterprise.credential.AbstractClearableCredential;
import javax.security.enterprise.credential.Password;
import java.time.LocalDate;

public class CivilCredential extends AbstractClearableCredential {

    private final String firstName;

    private final String lastName;

    private final LocalDate birthdate;

    private final Password password;

    public CivilCredential(String firstName, String lastName, LocalDate birthdate, Password password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Password getPassword() {
        return password;
    }

    public String getPasswordAsString() {
        return String.valueOf(getPassword().getValue());
    }

    public void clearCredential() {
        password.clear();
    }

}

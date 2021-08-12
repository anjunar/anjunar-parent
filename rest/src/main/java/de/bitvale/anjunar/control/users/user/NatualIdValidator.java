package de.bitvale.anjunar.control.users.user;

import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class NatualIdValidator implements ConstraintValidator<NaturalId, UserForm> {

    private final Identity identity;

    @Inject
    public NatualIdValidator(Identity identity) {
        this.identity = identity;
    }

    public NatualIdValidator() {
        this(null);
    }

    @Override
    public boolean isValid(UserForm value, ConstraintValidatorContext context) {
        User user = identity.findUser(value.getFirstName(), value.getLastName(), value.getBirthDate());
        if (user == null) {
            return true;
        } else {
            return user.getId().equals(value.getId());
        }
    }

}

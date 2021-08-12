package de.bitvale.anjunar.security.register;

import de.bitvale.common.security.Identity;
import de.bitvale.common.security.User;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class NatualIdValidator implements ConstraintValidator<NaturalId, RegisterForm> {

    private final Identity identity;

    @Inject
    public NatualIdValidator(Identity identity) {
        this.identity = identity;
    }

    public NatualIdValidator() {
        this(null);
    }

    @Override
    public boolean isValid(RegisterForm value, ConstraintValidatorContext context) {
        User user = identity.findUser(value.getFirstName(), value.getLastName(), value.getBirthDate());
        return user == null;
    }

}

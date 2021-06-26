package de.bitvale.common.validators;

import de.bitvale.common.rest.api.Editor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
public class DomValidator implements ConstraintValidator<Dom, Editor> {

    @Override
    public boolean isValid(Editor value, ConstraintValidatorContext context) {
        Document document = Jsoup.parse(value.getHtml());
        Elements scripts = document.getElementsByTag("script");
        return scripts.size() == 0;
    }

}

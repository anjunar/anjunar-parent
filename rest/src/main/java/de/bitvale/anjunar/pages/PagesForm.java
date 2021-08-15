package de.bitvale.anjunar.pages;

import de.bitvale.anjunar.shared.system.Language;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.meta.Input;

public class PagesForm extends AbstractRestEntity {

    @Input(type = "text", naming = true)
    private String title;

    @Input(type = "textarea")
    private String text;

    @Input(type = "lazyselect")
    private Language language;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public static PagesForm factory(Page page) {
        PagesForm resource = new PagesForm();

        resource.setId(page.getId());
        resource.setTitle(page.getTitle());
        resource.setText(page.getText());
        resource.setLanguage(Language.factory(page.getLanguage()));

        return resource;
    }
}

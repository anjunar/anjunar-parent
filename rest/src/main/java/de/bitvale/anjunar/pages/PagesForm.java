package de.bitvale.anjunar.pages;

import de.bitvale.anjunar.shared.system.Language;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.AbstractRestEntityConverter;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;

import javax.persistence.EntityManager;

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

    private static class PagesFormConverter extends AbstractRestEntityConverter<Page, PagesForm> {

        public static PagesFormConverter INSTANCE = new PagesFormConverter();

        public PagesForm factory(Page page, PagesForm resource) {
            resource.setId(page.getId());
            resource.setTitle(page.getTitle());
            resource.setText(page.getText());
            resource.setLanguage(Language.factory(page.getLanguage()));

            return resource;
        }

        @Override
        public Page updater(PagesForm restEntity, Page entity, EntityManager entityManager, Identity identity) {
            return null;
        }
    }

    public static PagesForm factory(Page page) {
        return PagesFormConverter.INSTANCE.factory(page, new PagesForm());
    }
}

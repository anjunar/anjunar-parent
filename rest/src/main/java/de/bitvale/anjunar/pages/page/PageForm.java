package de.bitvale.anjunar.pages.page;

import de.bitvale.anjunar.shared.system.Language;
import de.bitvale.anjunar.pages.Page;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.validators.Dom;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

public class PageForm extends AbstractRestEntity {

    @Input(type = "text", naming = true)
    private String title;

    @Input(type = "editor")
    @Dom
    private Editor content = new Editor();

    @Input(type = "lazyselect")
    private Language language;

    @Input(type = "lazymultiselect")
    private Set<PageSelect> pageLinks = new HashSet<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Editor getContent() {
        return content;
    }

    public void setContent(Editor content) {
        this.content = content;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Set<PageSelect> getPageLinks() {
        return pageLinks;
    }

    public void setPageLinks(Set<PageSelect> pageLinks) {
        this.pageLinks = pageLinks;
    }

    public static PageForm factory(Page page) {
        PageForm pageForm = new PageForm();

        pageForm.setId(page.getId());
        pageForm.setTitle(page.getTitle());
        pageForm.setContent(Editor.factory(page.getContent(), page.getText()));
        pageForm.setLanguage(Language.factory(page.getLanguage()));
        for (Page link : page.getLinks()) {
            pageForm.getPageLinks().add(PageSelect.factory(link));
        }
        return pageForm;
    }

    public static Page updater(PageForm pageForm, Page page, Identity identity, EntityManager entityManager) {
        page.setTitle(pageForm.getTitle());
        page.setContent(pageForm.getContent().getHtml());
        page.setText(pageForm.getContent().getText());
        page.setModifier(identity.getUser());
        page.setLanguage(Language.updater(pageForm.getLanguage()));
        page.getLinks().clear();
        for (PageSelect pageLink : pageForm.getPageLinks()) {
            page.getLinks().add(entityManager.find(Page.class, pageLink.getId()));
        }
        return page;
    }
}

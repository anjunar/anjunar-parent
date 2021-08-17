package de.bitvale.anjunar.pages.page;

import de.bitvale.anjunar.pages.Page;
import de.bitvale.anjunar.shared.likeable.AbstractLikeableRestEntity;
import de.bitvale.anjunar.shared.likeable.AbstractLikeableRestEntityConverter;
import de.bitvale.anjunar.shared.system.Language;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.meta.Input;
import de.bitvale.common.security.Identity;
import de.bitvale.common.validators.Dom;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

public class PageForm extends AbstractLikeableRestEntity {

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

    private static class PageFormConverter extends AbstractLikeableRestEntityConverter<Page, PageForm> {

        public static PageFormConverter INSTANCE = new PageFormConverter();

        public PageForm factory(PageForm pageForm, Page page) {
            pageForm.setId(page.getId());
            pageForm.setTitle(page.getTitle());
            pageForm.setContent(Editor.factory(page.getContent(), page.getText()));
            pageForm.setLanguage(Language.factory(page.getLanguage()));
            for (Page link : page.getLinks()) {
                pageForm.getPageLinks().add(PageSelect.factory(link));
            }
            return super.factory(pageForm, page);
        }

        @Override
        public Page updater(PageForm pageForm, Page page, EntityManager entityManager, Identity identity) {
            page.setTitle(pageForm.getTitle());
            page.setContent(pageForm.getContent().getHtml());
            page.setText(pageForm.getContent().getText());
            page.setModifier(identity.getUser());
            page.setLanguage(Language.updater(pageForm.getLanguage()));
            page.getLinks().clear();
            for (PageSelect pageLink : pageForm.getPageLinks()) {
                page.getLinks().add(entityManager.find(Page.class, pageLink.getId()));
            }
            return super.updater(pageForm, page, entityManager, identity);
        }
    }

    public static PageForm factory(Page page) {
        return PageFormConverter.INSTANCE.factory(new PageForm(), page);
    }

    public static Page updater(PageForm pageForm, Page page, Identity identity, EntityManager entityManager) {
        return PageFormConverter.INSTANCE.updater(pageForm, page, entityManager, identity);
    }
}

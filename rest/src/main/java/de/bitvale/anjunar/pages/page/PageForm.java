package de.bitvale.anjunar.pages.page;

import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.meta.Input;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class PageForm extends AbstractRestEntity {

    @Input(placeholder = "de.bitvale.anjunar.pages.page.PageForm.title.message", type = "text")
    private String title;

    @Input(placeholder = "de.bitvale.anjunar.pages.page.PageForm.content.message", type = "editor")
    private Editor content;

    @Input(placeholder = "de.bitvale.anjunar.pages.page.PageForm.language.message", type = "lazyselect")
    private Locale language;

    @Input(placeholder = "de.bitvale.anjunar.pages.page.PageForm.language.message", type = "lazymultiselect")
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

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public Set<PageSelect> getPageLinks() {
        return pageLinks;
    }

    public void setPageLinks(Set<PageSelect> pageLinks) {
        this.pageLinks = pageLinks;
    }
}

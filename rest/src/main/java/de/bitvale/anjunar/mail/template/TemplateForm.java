package de.bitvale.anjunar.mail.template;

import de.bitvale.anjunar.shared.system.Language;
import de.bitvale.common.mail.Template;
import de.bitvale.common.rest.api.AbstractRestEntity;
import de.bitvale.common.rest.api.Editor;
import de.bitvale.common.rest.api.meta.Input;

public class TemplateForm extends AbstractRestEntity {

    @Input(type = "text")
    private String name;

    @Input(type = "lazyselect")
    private Language language;

    @Input(type = "editor")
    private Editor content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Editor getContent() {
        return content;
    }

    public void setContent(Editor content) {
        this.content = content;
    }

    public static TemplateForm factory(Template template) {
        TemplateForm resource = new TemplateForm();
        resource.setId(template.getId());
        resource.setName(template.getName());
        resource.setLanguage(Language.factory(template.getLanguage()));
        resource.setContent(Editor.factory(template.getHtml(), null));
        return resource;
    }

    public static void updater(TemplateForm form, Template template) {
        template.setName(form.getName());
        template.setLanguage(Language.updater(form.getLanguage()));
        template.setHtml(form.getContent().getHtml());
    }

}

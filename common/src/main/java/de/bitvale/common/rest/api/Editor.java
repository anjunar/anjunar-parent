package de.bitvale.common.rest.api;

public class Editor {

    private String html;

    private String text;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static Editor factory(String content, String text) {
        Editor editor = new Editor();
        editor.setHtml(content);
        editor.setText(text);
        return editor;
    }

}

import {builder, customViews, i18nFactory} from "../../library/simplicity/simplicity.js";

export default class Text extends HTMLElement {

    render() {

        let page = this.queryUpwards("pages-page");
        let html = page.html;

        document.addEventListener("language", (event) => {
            for (const pageLink of html.form.pageLinks) {
                if (pageLink.language === event.detail) {
                    window.location.hash = `#/anjunar/pages/page?id=${pageLink.id}#/anjunar/pages/text`;
                    break;
                }
            }
        })

        builder(this, [{
                element: "div",
                style: {
                    marginTop: "12px"
                },
                initialize : (element) => {
                    element.innerHTML = html.form.content.html;
                }
            },
                {
                    element: "button",
                    type: "button",
                    className: "button",
                    text: i18n("History"),
                    onClick : () => {
                        window.location.hash = `#/anjunar/pages/page?id=${html.form.id}#/anjunar/pages/page/history?id=${html.form.id}`
                    }
                },
                {
                    element: "button",
                    type: "button",
                    className: "button",
                    text: i18n("Edit"),
                    onClick : () => {
                        window.location.hash = `#/anjunar/pages/editor?id=${html.form.id}`
                    }
                }
            ]
        )
    }
}

customViews.define({
    name: "page-text",
    class: Text
})

const i18n = i18nFactory({
    History : {
        en : "History",
        de : "Historie"
    },
    Edit : {
        en : "Edit",
        de : "Bearbeiten"
    }
});

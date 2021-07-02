import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";

export default class Text extends HTMLWindow {

    render() {

        let page = this.queryUpwards((element) => {
            return element.localName === "pages-page"
        });
        let html = page.html;

        document.addEventListener("language", (event) => {
            for (const pageLink of html.pageLinks) {
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
                    element.innerHTML = html.content.html;
                }
            },
                {
                    element: "button",
                    type: "button",
                    className: "button",
                    text: i18n("History"),
                    onClick : () => {
                        window.location.hash = `#/anjunar/pages/page?id=${html.id}#/anjunar/pages/page/history?id=${html.id}`
                    }
                },
                {
                    element: "button",
                    type: "button",
                    className: "button",
                    text: i18n("Edit"),
                    onClick : () => {
                        window.location.hash = `#/anjunar/pages/editor?id=${html.id}`
                    }
                }
            ]
        )
    }
}

customViews.define({
    name: "page-text",
    class: Text,
    header : "Text"
})

const i18n = i18nFactory({
    History : {
        "en-DE" : "History",
        "de-DE" : "Historie"
    },
    Edit : {
        "en-DE" : "Edit",
        "de-DE" : "Bearbeiten"
    }
});

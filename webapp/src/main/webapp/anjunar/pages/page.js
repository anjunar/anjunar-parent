import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";

export default class Page extends HTMLWindow {

    #html

    get html() {
        return this.#html;
    }

    set html(value) {
        this.#html = value;
    }

    render() {

        this.style.display = "block";

        builder(this, {
            element: "div",
            children: [
                {
                    element: "div",
                    children: [
                        {
                            element: "h3",
                            text: this.#html.title
                        },
                        {
                            element: "hr"
                        },
                        {
                            element: "div",
                            children: [
                                {
                                    element: "div",
                                    style: {
                                        marginTop: "12px"
                                    },
                                    update: (element) => {
                                        element.innerHTML = this.#html.content.html;
                                    }
                                },
                                {
                                    element: "button",
                                    type: "button",
                                    className: "button",
                                    text: i18n("History"),
                                    onClick: (event) => {
                                        event.stopPropagation();
                                        window.location.hash = `#/anjunar/pages/page/history?id=${this.#html.id}`
                                        return false;
                                    }
                                },
                                {
                                    element: "button",
                                    type: "button",
                                    className: "button",
                                    text: i18n("Edit"),
                                    onClick: (event) => {
                                        event.stopPropagation();
                                        window.location.hash = `#/anjunar/pages/editor?id=${this.#html.id}`
                                        return false;
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        })
    }
}

const i18n = i18nFactory({
    "Page" : {
        "en-DE" : "Page",
        "de-DE" : "Seite"
    },
    "History" : {
        "en-DE": "History",
        "de-DE": "Historie"
    },
    "Edit" : {
        "en-DE": "Edit",
        "de-DE": "Bearbeiten"
    }
});

customViews.define({
    name: "pages-page",
    class: Page,
    header : i18n("Page"),
    resizable : true,
    width : 800,
    height : 600,
    guard(activeRoute) {
        let url = `service/pages/page?id=${activeRoute.queryParams.id}`;
        if (activeRoute.queryParams.revision) {
            url += `&revision=${activeRoute.queryParams.revision}`
        }
        return {
            html: jsonClient.get(url),
            topics: jsonClient.get(`service/pages/page/topics?page=${activeRoute.queryParams.id}`)
        }
    }
})
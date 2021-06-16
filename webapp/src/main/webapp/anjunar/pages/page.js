import {builder, customViews, i18nFactory} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import DomRouter from "../../library/simplicity/directives/dom-router.js";
import HateoasTable from "../../library/simplicity/hateoas/hateoas-table.js";

export default class Page extends HTMLElement {

    #html
    #topics

    get html() {
        return this.#html;
    }

    set html(value) {
        this.#html = value;
    }

    get topics() {
        return this.#topics;
    }

    set topics(value) {
        this.#topics = value;
    }

    render() {
        builder(this, {
            element: "div",
            style: {
                display: "flex",
                width: "1200px",
                margin: "auto"
            },
            children: [
                {
                    element: "div",
                    style: {
                        width: "800px"
                    },
                    children: [
                        {
                            element: "h3",
                            text: this.#html.form.title
                        },
                        {
                            element: "hr"
                        },
                        {
                            element: DomRouter,
                            level: 1
                        }
                    ]
                },
                {
                    element: "div",
                    style: {
                        flex: "1",
                        marginLeft: "12px"
                    },
                    children: [
                        {
                            element: "h3",
                            text: i18n("Questions")
                        },
                        {
                            element: "hr"
                        },
                        {
                            element: "div",
                            className: "question",
                            style : {
                                padding : "5px"
                            },
                            children: [
                                {
                                    element: HateoasTable,
                                    header: false,
                                    model: this.#topics,
                                    onCreate : (event) => {
                                        window.location.hash = `#/anjunar/pages/page?id=${this.#html.form.id}#/anjunar/pages/page/topic?page=${this.#html.form.id}`
                                    },
                                    onRow : (event) => {
                                        window.location.hash = `#/anjunar/pages/page?id=${this.#html.form.id}#/anjunar/pages/page/topic/replies?id=${event.detail.id}`
                                    },
                                    meta: {
                                        body : this.#topics.columns.map((column) => {
                                            return {
                                                element(topic) {
                                                    switch (column.name) {
                                                        case "owner" : return {
                                                            element: "img",
                                                            src: topic.owner.image.data,
                                                            style: {
                                                                marginRight: "5px",
                                                                height: "80px",
                                                                width: "80px",
                                                                objectFit: "cover"
                                                            }
                                                        }
                                                        case "topic" : return {
                                                            element: "div",
                                                            children: [
                                                                {
                                                                    element: "h3",
                                                                    style: {
                                                                        color: "var(--main-blue-color)"
                                                                    },
                                                                    text: topic.topic
                                                                }, {
                                                                    element: "div",
                                                                    innerHTML: topic.editor.text,
                                                                    style: {
                                                                        height: "48px",
                                                                        width: "300px",
                                                                        overflow: "hidden"
                                                                    }
                                                                }
                                                            ]
                                                        }
                                                        default : return {
                                                            element : "div",
                                                            text : topic[column.name]
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                    }
                                }
                            ]
                        }
                    ]
                },
            ]
        })
    }
}

customViews.define({
    name: "pages-page",
    class: Page,
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

const i18n = i18nFactory({
    "Questions" : {
        en : "Questions",
        de : "Fragen"
    }
});

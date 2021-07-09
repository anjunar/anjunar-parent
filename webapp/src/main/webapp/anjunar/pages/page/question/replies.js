import {builder, customViews, HTMLWindow} from "../../../../library/simplicity/simplicity.js";
import {jsonClient} from "../../../../library/simplicity/services/client.js";
import HateoasTable from "../../../../library/simplicity/hateoas/hateoas-table.js";
import {dateFormat} from "../../../../library/simplicity/services/tools.js";
import HateoasForm from "../../../../library/simplicity/hateoas/hateoas-form.js";
import {i18nFactory} from "../../../../library/simplicity/services/i18nResolver.js";

export default class Replies extends HTMLWindow {

    #topic;
    #replies;

    get topic() {
        return this.#topic;
    }

    set topic(value) {
        this.#topic = value;
    }

    get replies() {
        return this.#replies;
    }

    set replies(value) {
        this.#replies = value;
    }

    render() {
        builder(this, {
            element: HateoasForm,
            model: this.#topic,
            children: [
                {
                    element: "div",
                    style: {
                        backgroundColor: "var(--main-dimmed-color)",
                        marginTop: "5px",
                        padding: "5px",
                        display: "flex"
                    },
                    children: [
                        {
                            element: "div",
                            style: {
                                marginLeft: "14px",
                                height: "80px",
                                width: "80px",
                                marginRight: "5px"
                            },
                            children: [
                                {
                                    style: {
                                        height: "80px",
                                        width: "80px",
                                        objectFit: "cover"
                                    },
                                    element: "img",
                                    src: this.#topic.owner.image.data
                                }
                            ]
                        },
                        {
                            element: "div",
                            children: [
                                {
                                    element: "h3",
                                    style: {
                                        color: "var(--main-blue-color)"
                                    },
                                    text: () => {
                                        return this.#topic.topic
                                    }
                                },
                                {
                                    element: "div",
                                    innerHTML: () => {
                                        return this.#topic.editor.html
                                    }
                                },
                                {
                                    element: "div",
                                    style: {
                                        display: "flex",
                                        alignItems: "center"
                                    },
                                    children: [
                                        {
                                            element: "span",
                                            style: {
                                                fontSize: "12px",
                                                width: "64px"
                                            },
                                            text: `${i18n("Views")}: ${this.#topic.views}`
                                        },
                                        {
                                            element: "button",
                                            type: "button",
                                            className: "button",
                                            text: i18n("Edit"),
                                            onClick: (event) => {
                                                event.stopPropagation();
                                                window.location.hash = `#/anjunar/pages/page/question?id=${this.#topic.id}`
                                                return false;
                                            }
                                        },
                                        {
                                            element: "div",
                                            style: {
                                                flex: "1"
                                            }
                                        },
                                        {
                                            element: "span",
                                            style: {
                                                fontSize: "12px"
                                            },
                                            text: dateFormat(this.#topic.created)
                                        },
                                    ]
                                },
                            ]
                        }
                    ]
                },
                {
                    element: "h3",
                    text: i18n("Replies")
                },
                {
                    element: "hr"
                },
                {
                    element: "div",
                    style: {
                        backgroundColor: "var(--main-dimmed-color)",
                        marginTop: "5px",
                        padding: "5px"
                    },
                    children: [
                        {
                            element: HateoasTable,
                            header: false,
                            model: this.#replies,
                            style: {
                                marginTop: "5px"
                            },
                            className: "replies",
                            onRow: (event) => {
                                window.location.hash = `#/anjunar/pages/page/question/reply-dialog?id=${event.detail.id}`
                            },
                            onCreate: (event) => {
                                window.location.hash = `#/anjunar/pages/page/question/reply-dialog?topic=${this.#topic.id}`
                            },
                            meta: {
                                body: this.#replies.columns.map((column) => {
                                    return {
                                        element: (reply) => {
                                            switch (column.name) {
                                                case "owner" :
                                                    return {
                                                        element: "img",
                                                        src: reply.owner.image.data,
                                                        style: {
                                                            height: "80px",
                                                            width: "80px",
                                                            marginRight: "5px",
                                                            objectFit: "cover"
                                                        }
                                                    }
                                                case "editor" :
                                                    return {
                                                        element: "div",
                                                        children: [
                                                            {
                                                                element: "div",
                                                                innerHTML: reply.editor.html,
                                                            },
                                                            {
                                                                element: "div",
                                                                style: {
                                                                    display: "flex"
                                                                },
                                                                children: [
                                                                    {
                                                                        element: "div",
                                                                        style: {
                                                                            flex: "1"
                                                                        }
                                                                    },
                                                                    {
                                                                        element: "span",
                                                                        style: {
                                                                            fontSize: "12px",
                                                                            lineHeight: "32px"
                                                                        },
                                                                        text: dateFormat(reply.created)
                                                                    }
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                default :
                                                    return {
                                                        element: "div",
                                                        text: reply[column.name]
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
        })
    }

}

customViews.define({
    name: "topic-replies",
    class: Replies,
    header: "Replies",
    resizable: true,
    guard(activeRoute) {
        return {
            topic: jsonClient.get(`service/pages/page/topics/topic?id=${activeRoute.queryParams.id}`),
            replies: jsonClient.get(`service/pages/page/topics/topic/replies?topic=${activeRoute.queryParams.id}`)
        }
    }
})

const i18n = i18nFactory({
    Replies: {
        "en-DE": "Replies",
        "de-DE": "Antworten"
    },
    Views: {
        "en-DE": "Views",
        "de-DE": "Sichten"
    },
    "Edit": {
        "en-DE": "Edit",
        "de-DE": "Bearbeiten"
    }
});

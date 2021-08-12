import {builder, customViews, HTMLWindow} from "../../../../library/simplicity/simplicity.js";
import {jsonClient} from "../../../../library/simplicity/services/client.js";
import HateoasTable from "../../../../library/simplicity/hateoas/hateoas-table.js";
import {dateFormat, hateoas} from "../../../../library/simplicity/services/tools.js";
import HateoasForm from "../../../../library/simplicity/hateoas/hateoas-form.js";
import {i18nFactory} from "../../../../library/simplicity/services/i18nResolver.js";
import Likes from "../../../shared/likes.js";

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
        this.style.display = "block";

        builder(this, {
            element: HateoasForm,
            model: this.#topic,
            children: [
                {
                    element: "div",
                    style: {
                        backgroundColor: "var(--main-dark2-color)",
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
                            style : {
                                width : "100%"
                            },
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
                                            element: "div",
                                            style: {
                                                flex: "1"
                                            }
                                        },
                                        {
                                            element: "button",
                                            type: "button",
                                            className: "button",
                                            text: i18n("Edit"),
                                            style: {
                                                fontSize: "12px",
                                                display : () => {
                                                    return hateoas(this.#topic.actions, "update") ? "block" : "none"
                                                }
                                            },
                                            onClick: (event) => {
                                                event.stopPropagation();
                                                window.location.hash = `#/anjunar/pages/page/question?id=${this.#topic.id}`
                                                return false;
                                            }
                                        },
                                        {
                                            element: "span",
                                            style: {
                                                fontSize: "12px"
                                            },
                                            text: dateFormat(this.#topic.created)
                                        },
                                        {
                                            element: "span",
                                            style: {
                                                marginLeft : "5px",
                                                fontSize: "12px"
                                            },
                                            text: `${i18n("Views")}: ${this.#topic.views}`
                                        },
                                        {
                                            element: Likes,
                                            likeable: this.#topic,
                                            onLike : () => {
                                                jsonClient.put(`service/pages/page/topics/topic?id=${this.#topic.id}`, {body : this.#topic})
                                            }
                                        }
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
                    element: "div",
                    style: {
                        backgroundColor: "var(--main-dark2-color)",
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
                                                                    display: "flex",
                                                                    alignItems: "center"
                                                                },
                                                                children: [
                                                                    {
                                                                        element: "div",
                                                                        style: {
                                                                            flex: "1"
                                                                        }
                                                                    },
                                                                    {
                                                                        element: "button",
                                                                        type: "button",
                                                                        className: "button",
                                                                        text: i18n("Edit"),
                                                                        style: {
                                                                            fontSize: "12px",
                                                                            display : () => {
                                                                                return hateoas(reply.actions, "update") ? "block" : "none"
                                                                            }
                                                                        },
                                                                        onClick: (event) => {
                                                                            event.stopPropagation();
                                                                            window.location.hash = `#/anjunar/pages/page/question/reply-dialog?id=${reply.id}`
                                                                            return false;
                                                                        }
                                                                    },
                                                                    {
                                                                        element: "span",
                                                                        style: {
                                                                            fontSize: "12px"
                                                                        },
                                                                        text: dateFormat(reply.created)
                                                                    },
                                                                    {
                                                                        element: "span",
                                                                        style: {
                                                                            marginLeft : "5px",
                                                                            fontSize: "12px"
                                                                        },
                                                                        text: `${i18n("Views")}: ${reply.views}`
                                                                    },
                                                                    {
                                                                        element: Likes,
                                                                        likeable : reply,
                                                                        onLike : () => {
                                                                            jsonClient.put(`service/pages/page/topics/topic/replies/reply?id=${reply.id}`, {body : reply})
                                                                        }
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

const i18n = i18nFactory({
    "Replies": {
        "en-DE": "Replies",
        "de-DE": "Antworten"
    },
    "Views": {
        "en-DE": "Views",
        "de-DE": "Sichten"
    },
    "Edit": {
        "en-DE": "Edit",
        "de-DE": "Bearbeiten"
    }
});

customViews.define({
    name: "topic-replies",
    class: Replies,
    header: i18n("Replies"),
    resizable: true,
    guard(activeRoute) {
        return {
            topic: jsonClient.get(`service/pages/page/topics/topic?id=${activeRoute.queryParams.id}`),
            replies: jsonClient.get(`service/pages/page/topics/topic/replies?topic=${activeRoute.queryParams.id}`)
        }
    }
})
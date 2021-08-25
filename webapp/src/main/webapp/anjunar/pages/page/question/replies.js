import {builder, customViews, HTMLWindow} from "../../../../library/simplicity/simplicity.js";
import {jsonClient} from "../../../../library/simplicity/services/client.js";
import HateoasTable from "../../../../library/simplicity/hateoas/hateoas-table.js";
import {dateFormat, hateoas} from "../../../../library/simplicity/services/tools.js";
import HateoasForm from "../../../../library/simplicity/hateoas/hateoas-form.js";
import {i18nFactory} from "../../../../library/simplicity/services/i18nResolver.js";
import Likes from "../../../shared/likes.js";
import MatList from "../../../../library/simplicity/components/table/mat-list.js";

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
                    element: "h3",
                    text : this.#topic.topic,
                    style: {
                        color: "var(--main-blue-color)"
                    }
                },
                {
                    element: "div",
                    style: {
                        display : "flex"
                    },
                    children: [
                        {
                            element: "div",
                            text : `${i18n("Views")}: ${this.#topic.views}`,
                            style : {
                                color: "var(--main-grey-color)",
                                width : "200px",
                                lineHeight : "24px",
                                fontSize : "14px"
                            }
                        },
                        {
                            element: "div",
                            text : () => {
                                return `${i18n("Likes")}: ${this.#topic.likes.length}`
                            },
                            style : {
                                color: "var(--main-grey-color)",
                                width : "200px",
                                lineHeight : "24px",
                                fontSize : "14px"
                            }
                        }
                    ]
                },
                {
                    element: "hr",
                    style : {
                        marginTop: "12px",
                        backgroundColor : "var(--main-dark1-color)"
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
                        display : "flex",
                        fontSize : "14px"
                    },
                    children : [
                        {
                            element: "div",
                            style: {
                                position : "relative"
                            },
                            children: [
                                {
                                    element: "div",
                                    style : {
                                        display : "flex",
                                        alignItems: "center"
                                    },
                                    children: [
                                        {
                                            element: Likes,
                                            likeable: this.#topic,
                                            onLike : () => {
                                                jsonClient.put(`service/pages/page/topics/topic?id=${this.#topic.id}`, {body : this.#topic})
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
                                        }
                                    ]
                                },
                                {
                                    element: "div",
                                    text : dateFormat(this.#topic.created),
                                    style : {
                                        position : "absolute",
                                        bottom : "0",
                                        left : "0",
                                        width : "400px",
                                        color: "var(--main-grey-color)"
                                    }
                                }
                            ]
                        },
                        {
                            element: "div",
                            style : {
                                flex : "1"
                            }
                        },
                        {
                            element: "div",
                            children : [
                                {
                                    element: "div",
                                    children : [
                                        {
                                            element: "img",
                                            src: this.#topic.owner.image.data,
                                            style: {
                                                marginRight: "5px",
                                                height: "80px",
                                                width: "80px",
                                                objectFit: "cover"
                                            }
                                        }, {
                                            element : "div",
                                            text : this.#topic.owner.firstName + " " + this.#topic.owner.lastName,
                                            style : {
                                                color: "var(--main-grey-color)"
                                            }
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                },
                {
                    element: "hr",
                    style : {
                        marginTop: "12px",
                        backgroundColor : "var(--main-dark1-color)"
                    }
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
                            element: MatList,
                            items : {
                                direct : (query, callback) => {
                                    let link = hateoas(this.#replies.sources, "list")

                                    link.body = link.body || {};

                                    if (query.search) {
                                        link.body[query.search.property] = query.search.value
                                    }

                                    link.body.sort = query.sort;
                                    link.body.index = query.index;
                                    link.body.limit = query.limit;

                                    jsonClient.post(link.url, {body: link.body})
                                        .then(response => {
                                            callback(response.rows, response.size, response.links)
                                        })
                                }
                            },
                            onCreate: (event) => {
                                window.location.hash = `#/anjunar/pages/page/question/reply-dialog?topic=${this.#topic.id}`
                            },
                            meta : {
                                element : (reply) => {
                                    return {
                                        element : "div",
                                        children : [
                                            {
                                                element: "div",
                                                style: {
                                                    display : "flex"
                                                },
                                                children: [
                                                    {
                                                        element: "div",
                                                        text : `${i18n("Views")}: ${reply.views}`,
                                                        style : {
                                                            color: "var(--main-grey-color)",
                                                            width : "200px",
                                                            lineHeight : "24px",
                                                            fontSize : "14px"
                                                        }
                                                    },
                                                    {
                                                        element: "div",
                                                        text : () => {
                                                            return `${i18n("Likes")}: ${reply.likes.length}`
                                                        },
                                                        style : {
                                                            color: "var(--main-grey-color)",
                                                            width : "200px",
                                                            lineHeight : "24px",
                                                            fontSize : "14px"
                                                        }
                                                    }
                                                ]
                                            },
                                            {
                                                element: "hr",
                                                style : {
                                                    marginTop: "12px",
                                                    backgroundColor : "var(--main-dark1-color)"
                                                }
                                            },
                                            {
                                                element: "div",
                                                innerHTML: () => {
                                                    return reply.editor.html
                                                }
                                            },
                                            {
                                                element: "div",
                                                style: {
                                                    display : "flex",
                                                    fontSize : "14px"
                                                },
                                                children : [
                                                    {
                                                        element: "div",
                                                        style: {
                                                            position : "relative"
                                                        },
                                                        children: [
                                                            {
                                                                element: "div",
                                                                style : {
                                                                    display : "flex",
                                                                    alignItems: "center"
                                                                },
                                                                children: [
                                                                    {
                                                                        element: Likes,
                                                                        likeable : reply,
                                                                        onLike : () => {
                                                                            jsonClient.put(`service/pages/page/topics/topic/replies/reply?id=${reply.id}`, {body : reply})
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
                                                                ]
                                                            },
                                                            {
                                                                element: "div",
                                                                text : dateFormat(reply.created),
                                                                style : {
                                                                    position : "absolute",
                                                                    bottom : "0",
                                                                    left : "0",
                                                                    width : "400px",
                                                                    color: "var(--main-grey-color)"
                                                                }
                                                            }
                                                        ]
                                                    },
                                                    {
                                                        element: "div",
                                                        style : {
                                                            flex : "1"
                                                        }
                                                    },
                                                    {
                                                        element: "div",
                                                        children : [
                                                            {
                                                                element: "div",
                                                                children : [
                                                                    {
                                                                        element: "img",
                                                                        src: reply.owner.image.data,
                                                                        style: {
                                                                            marginRight: "5px",
                                                                            height: "80px",
                                                                            width: "80px",
                                                                            objectFit: "cover"
                                                                        }
                                                                    }, {
                                                                        element : "div",
                                                                        text : reply.owner.firstName + " " + reply.owner.lastName,
                                                                        style : {
                                                                            color: "var(--main-grey-color)"
                                                                        }
                                                                    }
                                                                ]
                                                            }
                                                        ]
                                                    }
                                                ]
                                            },
                                        ]
                                    }
                                }
                            }
                        },
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
    },
    "Likes" : {
        "en-DE" : "Likes",
        "de-DE" : "Mag ich"
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
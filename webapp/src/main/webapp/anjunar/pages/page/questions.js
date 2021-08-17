import {builder, customViews, HTMLWindow} from "../../../library/simplicity/simplicity.js";
import {jsonClient} from "../../../library/simplicity/services/client.js";
import {i18nFactory} from "../../../library/simplicity/services/i18nResolver.js";
import MatList from "../../../library/simplicity/components/table/mat-list.js";
import {dateFormat, hateoas} from "../../../library/simplicity/services/tools.js";

export default class Questions extends HTMLWindow {

    #topics;

    get topics() {
        return this.#topics;
    }

    set topics(value) {
        this.#topics = value;
    }

    render() {

        this.style.display = "block";

        builder(this, {
                element: MatList,
                items: {
                    direct : (query, callback) => {
                        let link = hateoas(this.#topics.sources, "list")

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
                onCreate : () => {
                    window.location.hash = `#/anjunar/pages/page/question?page=${this.queryParams.page}`
                },
                onItem : (event) => {
                    window.location.hash = `#/anjunar/pages/page/question/replies?id=${event.detail.id}`
                },
                meta: {
                    element: (topic) => {
                        return {
                            element : "div",
                            children : [
                                {
                                    element: "div",
                                    style: {
                                        display: "flex",
                                        fontSize : "14px",
                                        width : "100%",
                                        marginTop : "5px"
                                    },
                                    children: [
                                        {
                                            element: "div",
                                            children: [
                                                {
                                                    element: "div",
                                                    text : i18n("Likes") + " " + topic.likes.length,
                                                    style : {
                                                        height: "80px",
                                                        width : "80px",
                                                        lineHeight: "80px",
                                                        textAlign : "center",
                                                        color: "var(--main-grey-color)"
                                                    }
                                                },
                                                {
                                                    element: "div",
                                                    text : i18n("Views") + " " + topic.views,
                                                    style : {
                                                        height: "80px",
                                                        width : "80px",
                                                        lineHeight: "80px",
                                                        textAlign : "center",
                                                        color: "var(--main-grey-color)"
                                                    }
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
                                                    element: "div",
                                                    style: {
                                                        height: "80px"
                                                    },
                                                    children : [
                                                        {
                                                            element: "h3",
                                                            style: {
                                                                color: "var(--main-blue-color)",
                                                                margin: "0px"
                                                            },
                                                            text: topic.topic
                                                        }, {
                                                            element: "div",
                                                            innerHTML: topic.editor.text,
                                                            style: {
                                                                maxHeight: "calc(3 * 14px)",
                                                                lineHeight : "14px",
                                                                overflow: "hidden"
                                                            }
                                                        }
                                                    ]
                                                },
                                                {
                                                    element: "div",
                                                    style: {
                                                        display : "flex"
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
                                                                    text : dateFormat(topic.created),
                                                                    style : {
                                                                        position : "absolute",
                                                                        bottom : "0",
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
                                                                            src: topic.owner.image.data,
                                                                            style: {
                                                                                marginRight: "5px",
                                                                                height: "80px",
                                                                                width: "80px",
                                                                                objectFit: "cover"
                                                                            }
                                                                        }, {
                                                                            element : "div",
                                                                            text : topic.owner.firstName + " " + topic.owner.lastName,
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
                                    ]
                                },
                                {
                                    element : "hr",
                                    style : {
                                        marginTop: "12px",
                                        backgroundColor : "var(--main-dark1-color)"
                                    }
                                }
                            ]
                        }
                    }
                }
            }
        )
    }
}

let i18n = i18nFactory({
    "Questions": {
        "en-DE": "Questions",
        "de-DE": "Fragen"
    },
    "Likes" : {
        "en-DE" : "Likes",
        "de-DE" : "Mag ich"
    },
    "Views" : {
        "en-DE" : "Views",
        "de-DE" : "Sichten"
    }
});

customViews.define({
    name: "page-questions",
    class: Questions,
    header: i18n("Questions"),
    resizable: true,
    guard(activeRoute) {
        return {
            topics: jsonClient.get(`service/pages/page/topics?page=${activeRoute.queryParams.page}`)
        }
    }
})
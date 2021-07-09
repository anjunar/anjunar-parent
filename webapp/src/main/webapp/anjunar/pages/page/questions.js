import {builder, customViews, HTMLWindow} from "../../../library/simplicity/simplicity.js";
import HateoasTable from "../../../library/simplicity/hateoas/hateoas-table.js";
import {jsonClient} from "../../../library/simplicity/services/client.js";

export default class Questions extends HTMLWindow {

    #topics;

    get topics() {
        return this.#topics;
    }

    set topics(value) {
        this.#topics = value;
    }

    render() {
        builder(this, {
                element: HateoasTable,
                header: false,
                model: this.#topics,
                onCreate: (event) => {
                    window.location.hash = `#/anjunar/pages/page/question?page=${this.queryParams.page}`
                },
                onRow: (event) => {
                    window.location.hash = `#/anjunar/pages/page/question/replies?id=${event.detail.id}`
                },
                meta: {
                    body: this.#topics.columns.map((column) => {
                        return {
                            element(topic) {
                                switch (column.name) {
                                    case "owner" :
                                        return {
                                            element: "img",
                                            src: topic.owner.image.data,
                                            style: {
                                                marginRight: "5px",
                                                height: "80px",
                                                width: "80px",
                                                objectFit: "cover"
                                            }
                                        }
                                    case "topic" :
                                        return {
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
                                    default :
                                        return {
                                            element: "div",
                                            text: topic[column.name]
                                        }
                                }
                            }
                        }
                    })
                }
            }
        )
    }
}

customViews.define({
    name: "page-questions",
    class: Questions,
    header: "Questions",
    resizable: true,
    guard(activeRoute) {
        return {
            topics: jsonClient.get(`service/pages/page/topics?page=${activeRoute.queryParams.page}`)
        }
    }
})
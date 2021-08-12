import {builder, customComponents} from "../simplicity.js";
import {jsonClient} from "../services/client.js";
import {hateoas} from "../services/tools.js";
import MatGrid from "../components/table/mat-grid.js";

export default class HateoasGrid extends HTMLElement {

    #model;
    #meta;
    #limit = 14;
    #emptyItem;

    get model() {
        return this.#model;
    }

    set model(value) {
        this.#model = value;
    }

    get meta() {
        return this.#meta;
    }

    set meta(value) {
        this.#meta = value;
    }

    get limit() {
        return this.#limit;
    }

    set limit(value) {
        this.#limit = value;
    }

    get emptyItem() {
        return this.#emptyItem;
    }

    set emptyItem(value) {
        this.#emptyItem = value;
    }

    render() {

        this.style.display = "block";

        let link = hateoas(this.#model.sources, "list")

        if (link) {
            builder(this, {
                element: MatGrid,
                limit: () => {
                    return this.#limit
                },
                onItems : (event) => {
                    this.dispatchEvent(new CustomEvent("items"));
                },
                items: {
                    direct : (query, callback) => {
                        let sort = [];
                        if (query.sort) {
                            sort = query.sort.map((sort => "&sort=" + sort));
                        }
                        let search = "";
                        if (query.search) {
                            search = "&" + query.search.property + "=" + query.search.value
                        }

                        let url;
                        if (link.url.indexOf("?") > -1) {
                            url = `${link.url}&index=${query.index}&limit=${query.limit}${sort.join("")}${search}`
                        } else {
                            url = `${link.url}?index=${query.index}&limit=${query.limit}${sort.join("")}${search}`
                        }
                        jsonClient.post(url)
                            .then(response => {
                                callback(response.rows, response.size, response.links)
                            })
                    }
                },
                emptyItem : this.#emptyItem,
                onItem : (event) => {
                    this.dispatchEvent(new CustomEvent("item", {detail : event.detail}))
                },
                onCreate : (event) => {
                    this.dispatchEvent(new CustomEvent("create", {detail : event.detail}))
                },
                meta: this.#meta || {
                    colgroup : this.#model.sortable.map((segment) => {
                        return {
                            element : () => {
                                return {
                                    element: "div",
                                    attributes: {
                                        path() {
                                            return segment.sortable ? segment.property : ""
                                        }
                                    }
                                }
                            }
                        }
                    }),
                    header: this.#model.columns.map((column) => {
                        return {
                            element : () => {
                                return {
                                    element: "strong",
                                    text: column.name
                                }
                            }
                        }
                    }),
                    body: this.#model.columns.map((column) => {
                        return {
                            element : (c) => {
                                switch (column.type) {
                                    case "image" : {
                                        return {
                                            element: "img",
                                            style: {
                                                height: "100px"
                                            },
                                            src : () => {
                                                if (c.picture) {
                                                    return c.picture.data
                                                }
                                            }
                                        }
                                    }
                                    default :
                                        return {
                                            element: "div",
                                            text: c[column.name]
                                        }
                                }
                            }
                        }
                    })
                }
            })
        }
    }

}

customComponents.define("hateoas-grid", HateoasGrid)
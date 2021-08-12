import {builder, customComponents} from "../simplicity.js";
import MatTable from "../components/table/mat-table.js";
import {jsonClient} from "../services/client.js";
import {hateoas} from "../services/tools.js";
import DomInput from "../directives/dom-input.js";
import DomLazySelect from "../directives/dom-lazy-select.js";
import {QueryBuilder} from "../services/querybuilder.js";

export default class HateoasTable extends HTMLElement {

    #model;
    #meta = {};
    #limit = 10;
    #header = true;


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
        this.#limit = value
    }

    get header() {
        return this.#header;
    }

    set header(value) {
        this.#header = value;
    }

    reload() {
        let table = this.querySelector("table");
        if (table) {
            table.load();
        }
    }

    render() {
        let link = hateoas(this.#model.sources, "list")

        if (link) {
            builder(this, {
                element: MatTable,
                limit: this.#limit,
                header: this.#header,
                items: {
                    direct : (query, callback) => {
                        let sort = query.sort.map((sort => "&sort=" + sort));
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
                onRow : (event) => {
                    this.dispatchEvent(new CustomEvent("row", {detail: event.detail}))
                },
                onCreate : (event) => {
                    this.dispatchEvent(new CustomEvent("create", {detail: event.detail}))
                },
                meta: {
                    filter: this.#meta.filter || this.#model.columns.map((column) => {
                        return {
                            element : (tr) => {
                                switch (column.type) {
                                    case "lazyselect" :
                                        return {
                                            element: DomLazySelect,
                                            placeholder: "search",
                                            style: {
                                                marginLeft: "5px"
                                            },
                                            onChange : (event) => {
                                                let value = event.target.value;
                                                let table = this.querySelector("table");
                                                table.search({property: tr.search, value: value.id});
                                            },
                                            items: {
                                                direct : (query, callback) => {
                                                    let link = hateoas(column.links, "list");
                                                    let queryBuilder = new QueryBuilder(link.url)
                                                        .queryParam("index", query.index)
                                                        .queryParam("limit", query.limit)

                                                    jsonClient.action(link.method, queryBuilder.build())
                                                        .then((response) => {
                                                            callback(response.rows, response.size)
                                                        })
                                                }
                                            },
                                            meta: {
                                                option: {
                                                    element : (element) => {
                                                        let namingProperties = element.meta.properties
                                                            .filter((element) => element.naming)
                                                            .map((elem) => element[elem.name])
                                                            .join(" ")
                                                        return {
                                                            element: "div",
                                                            text: namingProperties
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    default :
                                        return {
                                            element: DomInput,
                                            style: {
                                                marginLeft: "5px",
                                                // width : "80px"
                                            },
                                            placeholder: "search",
                                            type: column.type,
                                            onKeyup : (event) => {
                                                let table = this.querySelector("table");
                                                let value = event.target.value;
                                                table.search({property: tr.search, value: value});
                                            },
                                            onChange : (event) => {
                                                let table = this.querySelector("table");
                                                let value = event.target.value;
                                                table.search({property: tr.search, value: value});
                                            }
                                        }
                                }
                            }
                        }
                    }),
                    colgroup: this.#meta.colgroup || this.#model.sortable.map((segment) => {
                        return {
                            element : () => {
                                return {
                                    element: "div",
                                    attributes: {
                                        path() {
                                            return segment.property;
                                        },
                                        visible() {
                                            return segment.visible
                                        }
                                    }
                                }
                            }
                        }
                    }),
                    header: this.#meta.header || this.#model.columns.map((column) => {
                        return {
                            element : () => {
                                return {
                                    element: "strong",
                                    text: column.name
                                }
                            }
                        }
                    }),
                    body: this.#meta.body || this.#model.columns.map((column) => {
                        return {
                            element : (c) => {
                                switch (column.type) {
                                    case "lazyselect" :
                                        return {
                                            element: DomLazySelect,
                                            placeholder: "search",
                                            value : {
                                                input : () => {
                                                    if (c.form) {
                                                        return c.form[column.name]
                                                    }
                                                    return c[column.name]
                                                },
                                                output : (value) => {
                                                    c[column.name] = value
                                                }
                                            },
                                            items: {
                                                direct : (query, callback) => {
                                                    let link = hateoas(column.links, "list");
                                                    let queryBuilder = new QueryBuilder(link.url)
                                                        .queryParam("index", query.index)
                                                        .queryParam("limit", query.limit)

                                                    jsonClient.action(link.method, queryBuilder.build())
                                                        .then((response) => {
                                                            callback(response.rows, response.size)
                                                        })
                                                }
                                            },
                                            meta: {
                                                option: {
                                                    element : (element) => {
                                                        let namingProperties = element.meta.properties
                                                            .filter((element) => element.naming)
                                                            .map((elem) => element[elem.name])
                                                            .join(" ")
                                                        return {
                                                            element: "div",
                                                            text: namingProperties
                                                        }
                                                    }
                                                }
                                            }
                                        }
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
                                    case "editor" : {
                                        return {
                                            element: "div",
                                            text : c[column.name].text
                                        }
                                    }
                                    default :
                                        return {
                                            element: "div",
                                            text: () => {
                                                if (c.form) {
                                                    return c.form[column.name]
                                                }
                                                return c[column.name]
                                            }
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

customComponents.define("hateoas-table", HateoasTable)
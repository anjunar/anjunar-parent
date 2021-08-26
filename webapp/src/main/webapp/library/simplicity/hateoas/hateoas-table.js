import {builder, customComponents} from "../simplicity.js";
import MatTable from "../components/table/mat-table.js";
import {jsonClient} from "../services/client.js";
import {hateoas} from "../services/tools.js";
import DomInput from "../directives/dom-input.js";
import DomLazySelect from "../directives/dom-lazy-select.js";
import {QueryBuilder} from "../services/querybuilder.js";
import MatInputContainer from "../components/form/containers/mat-input-container.js";
import DomForm from "../directives/dom-form.js";
import MatSelect from "../components/form/mat-select.js";
import MatMultiSelect from "../components/form/mat-multi-select.js";

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
                    direct: (query, callback) => {

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
                onRow: (event) => {
                    this.dispatchEvent(new CustomEvent("row", {detail: event.detail}))
                },
                onCreate: (event) => {
                    this.dispatchEvent(new CustomEvent("create", {detail: event.detail}))
                },
                meta: {
                    filter: this.#meta.filter || this.#model.columns.map((column) => {
                        return (tr) => {
                            switch (column.type) {
                                case "lazyselect" :
                                    return {
                                        element: MatSelect,
                                        placeholder: "search",
                                        onChange: (event) => {
                                            let value = event.target.value;
                                            let table = this.querySelector("table");
                                            table.search({property: tr.search, value: value});
                                        },
                                        items: {
                                            direct: (query, callback) => {
                                                let link = hateoas(column.links, "list");

                                                link.body = link.body || {};
                                                link.body.index = query.index;
                                                link.body.limit = query.limit;

                                                jsonClient.action(link.method, link.url, {body : link.body})
                                                    .then((response) => {
                                                        callback(response.rows, response.size)
                                                    })
                                            }
                                        },
                                        meta: {
                                            option: {
                                                element: (element) => {
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
                                case "lazymultiselect" :
                                    return {
                                        element: MatMultiSelect,
                                        placeholder: "search",
                                        onChange: (event) => {
                                            let value = event.target.value;
                                            let table = this.querySelector("table");
                                            table.search({property: tr.search, value: value});
                                        },
                                        items: {
                                            direct: (query, callback) => {
                                                let link = hateoas(column.links, "list");
                                                link.body = link.body || {};
                                                link.body.index = query.index;
                                                link.body.limit = query.limit;

                                                jsonClient.action(link.method, link.url, {body : link.body})
                                                    .then((response) => {
                                                        callback(response.rows, response.size)
                                                    })
                                            }
                                        },
                                        meta: {
                                            option: {
                                                element: (element) => {
                                                    let namingProperties = element.meta.properties
                                                        .filter((element) => element.naming)
                                                        .map((elem) => element[elem.name])
                                                        .join(" ")
                                                    return {
                                                        element: "div",
                                                        text: namingProperties
                                                    }
                                                }
                                            },
                                            selection: {
                                                element: (element) => {
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
                                case "datetime-local" :
                                case "date" : {
                                    let date = {
                                        start : null,
                                        end : null
                                    };

                                    return {
                                        element: DomForm,
                                        value : {
                                            input : () => {
                                                return date;
                                            },
                                            output : (value) => {
                                                date = value;
                                            }
                                        },
                                        onChange: (event) => {
                                            let table = this.querySelector("table");
                                            table.search({property: tr.search, value: date});
                                        },
                                        children : [
                                            {
                                                element: MatInputContainer,
                                                placeholder: "start",
                                                content: {
                                                    element: DomInput,
                                                    type: column.type,
                                                    name : "start",
                                                    attributes: {
                                                        disabled: () => {
                                                            return (!tr.sortable);
                                                        }
                                                    }
                                                }
                                            },
                                            {
                                                element: MatInputContainer,
                                                placeholder: "end",
                                                content: {
                                                    element: DomInput,
                                                    type: column.type,
                                                    name : "end",
                                                    attributes: {
                                                        disabled: () => {
                                                            return (!tr.sortable);
                                                        }
                                                    }
                                                }
                                            }
                                        ]
                                    }
                                }
                                default :
                                    return {
                                        element: MatInputContainer,
                                        placeholder: "search",
                                        content: {
                                            element: DomInput,
                                            type: column.type,
                                            attributes: {
                                                disabled: () => {
                                                    return (!tr.sortable);
                                                }
                                            },
                                            onKeyup: (event) => {
                                                let table = this.querySelector("table");
                                                let value = event.target.value;
                                                table.search({property: tr.search, value: value});
                                            },
                                            onChange: (event) => {
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
                        return () => {
                            return {
                                element: "div",
                                attributes: {
                                    path: () => {
                                        return segment.property;
                                    },
                                    visible: () => {
                                        return segment.visible
                                    },
                                    sortable: () => {
                                        return segment.sortable;
                                    }
                                }
                            }
                        }
                    }),
                    header: this.#meta.header || this.#model.columns.map((column) => {
                        return () => {
                            return {
                                element: "strong",
                                text: column.name
                            }
                        }
                    }),
                    body: this.#meta.body || this.#model.columns.map((column) => {
                        return (c) => {
                            switch (column.type) {
                                case "lazymultiselect" : {
                                    let elements = c[column.name];
                                    let names = [];
                                    for (const element of elements) {
                                        let namingProperties = element.meta.properties
                                            .filter((element) => element.naming)
                                            .map((elem) => element[elem.name])
                                            .join(" ")
                                        names.push(namingProperties);
                                    }
                                    return {
                                        element: "div",
                                        text : names.join(", ")
                                    }
                                }
                                case "lazyselect" : {
                                    let element = c[column.name];
                                    let namingProperties = element.meta.properties
                                        .filter((element) => element.naming)
                                        .map((elem) => element[elem.name])
                                        .join(" ")
                                    return {
                                        element: "div",
                                        text : namingProperties
                                    }
                                }
                                case "image" : {
                                    return {
                                        element: "img",
                                        style: {
                                            height: "100px"
                                        },
                                        src: () => {
                                            if (c.picture) {
                                                return c.picture.data
                                            }
                                        }
                                    }
                                }
                                case "textarea" : {
                                    return {
                                        element: "div",
                                        style : {
                                            height: "14px",
                                            textOverflow: "clip",
                                            overflow : "hidden"
                                        },
                                        text: c[column.name]
                                    }
                                }
                                case "editor" : {
                                    return {
                                        element: "div",
                                        style : {
                                            height: "24px",
                                            textOverflow: "clip"
                                        },
                                        text: c[column.name].text
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
                    })
                }
            })
        }
    }

}

customComponents.define("hateoas-table", HateoasTable)
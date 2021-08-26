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
                emptyItem : this.#emptyItem,
                onItem : (event) => {
                    this.dispatchEvent(new CustomEvent("item", {detail : event.detail}))
                },
                onCreate : (event) => {
                    this.dispatchEvent(new CustomEvent("create", {detail : event.detail}))
                },
                meta : this.#meta
            })
        }
    }

}

customComponents.define("hateoas-grid", HateoasGrid)
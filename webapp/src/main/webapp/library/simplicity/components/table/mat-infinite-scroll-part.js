import {builder, customComponents} from "../../simplicity.js";

export default class MatInfiniteScrollPart extends HTMLElement {

    #items;

    #index;
    #limit;

    #size;
    #data;
    #links;

    #meta;

    get index() {
        return this.#index;
    }

    set index(value) {
        this.#index = value;
    }

    get limit() {
        return this.#limit;
    }

    set limit(value) {
        this.#limit = value;
    }

    get items() {
        return this.#items;
    }

    set items(value) {
        this.#items = value;
    }

    get data() {
        return this.#data;
    }

    get meta() {
        return this.#meta;
    }

    set meta(value) {
        this.#meta = value;
    }

    reload() {
        this.load();
    }

    load() {
        this.#items({index: this.#index, limit: this.#limit}, (_items, _size, _links) => {
            this.#size = _size;
            this.#data = _items;
            this.#links = _links;
            this.dispatchEvent(new CustomEvent("loadend", {detail : {size : this.#size, data : this.#data, links : this.#links}}))
        });
    }

    render() {

        this.load();

        builder(this, {
            element : "div",
            onItems: (event) => {
                this.dispatchEvent(new CustomEvent("items"))
            },
            children : {
                items : () => {
                    return this.#data;
                },
                item : (item) => {
                    return {
                        element : "div",
                        initialize : (element) => {
                            let m = this.#meta.part;
                            builder(element, m.element(item));
                        }
                    }
                }
            }
        })
    }

}

customComponents.define("mat-infinite-scroll-part", MatInfiniteScrollPart)
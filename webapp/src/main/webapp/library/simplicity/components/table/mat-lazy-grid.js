import {builder, customComponents} from "../../simplicity.js";

export default class MatLazyGrid extends HTMLElement {

    #meta;
    #items;

    get meta() {
        return this.#meta;
    }

    set meta(value) {
        this.#meta = value;
    }

    get items() {
        return this.#items;
    }

    set items(value) {
        this.#items = value;
    }

    render() {
        this.style.display = "block";
        this.style.height = "100%";
        this.style.width = "100%";

        builder(this, {

        })
    }

}

customComponents.define("mat-lazy-grid", MatLazyGrid);

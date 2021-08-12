import {builder, customComponents} from "../../simplicity.js";

export default class MatPage extends HTMLElement {

    #page = 0;
    #contents = [];

    get page() {
        return this.#page;
    }

    set page(value) {
        this.#page = value;
    }

    get contents() {
        return this.#contents;
    }

    set contents(value) {
        this.#contents = value;
    }

    render() {
        builder(this, {
            element : "div",
            initialize : (element) => {
                for (const content of this.#contents) {
                    element.appendChild(content);
                }
            }
        })
    }

}

customComponents.define("mat-page", MatPage);
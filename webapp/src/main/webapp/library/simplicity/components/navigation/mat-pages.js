import {builder, customComponents} from "../../simplicity.js";

export default class MatPages extends HTMLElement {

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
            update : (element) => {
                let temp = Array.from(this.#contents);
                let newChild = temp.splice(this.#page, 1);
                for (const tempElement of temp) {
                    tempElement.remove();
                }

                if (newChild[0].parentElement === null) {
                    element.appendChild(newChild[0]);
                }
            }
        })
    }

}

customComponents.define("mat-pages", MatPages);
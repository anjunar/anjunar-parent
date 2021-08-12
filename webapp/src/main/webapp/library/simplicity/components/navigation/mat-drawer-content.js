import {builder, customComponents} from "../../simplicity.js";

export default class MatDrawerContent extends HTMLElement {

    #content;

    get content() {
        return this.#content;
    }

    set content(value) {
        this.#content = value;
    }

    render() {
        this.style.width = "100%"

        builder(this, {
            element : "div",
            initialize : (element) => {
                element.appendChild(this.#content);
            }
        })
    }

}

customComponents.define("mat-drawer-content", MatDrawerContent)
import {builder, customComponents} from "../../simplicity.js";

export default class MatMenuItem extends HTMLElement {

    #content;

    get content() {
        return this.#content;
    }

    set content(value) {
        this.#content = value;
    }

    render() {
        builder(this, {
            element : "div",
            style : {
                padding : "5px"
            },
            initialize : (element) => {
                element.appendChild(this.#content);
            }
        })
    }

}

customComponents.define("mat-menu-item", MatMenuItem)
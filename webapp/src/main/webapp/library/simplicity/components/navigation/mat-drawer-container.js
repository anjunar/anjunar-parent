import {builder, customComponents} from "../../simplicity.js";

export default class MatDrawerContainer extends HTMLElement {

    #drawer;
    #content;

    get drawer() {
        return this.#drawer;
    }

    set drawer(value) {
        this.#drawer = value;
    }

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
            style : {
                display : "flex"
            },
            initialize : (element) => {
                element.appendChild(this.#drawer);
                element.appendChild(this.#content);
            }
        })
    }

}

customComponents.define("mat-drawer-container", MatDrawerContainer)
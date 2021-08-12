import {builder, customComponents} from "../../simplicity.js";

export default class MatDrawer extends HTMLElement {

    #open = true;
    #content = [];

    get open() {
        return this.#open;
    }

    set open(value) {
        this.#open = value;
    }

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
                padding : "1px",
                display : () => {
                    return this.#open ? "block" : "none"
                },
                top : "0",
                backgroundColor : "var(--main-dark1-color)",
                borderRight : "1px var(--main-dark1-color) solid",
                width : "300px",
                zIndex : "2"
            },
            initialize : (element) => {
                for (const contentElement of this.#content) {
                    element.appendChild(contentElement);
                }
            }
        })
    }

}

customComponents.define("mat-drawer", MatDrawer)
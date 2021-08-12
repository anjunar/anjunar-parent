import {builder, customComponents} from "../../simplicity.js";

export default class MatTab extends HTMLElement {

    #content;
    #selected = false;

    get content() {
        return this.#content;
    }

    set content(value) {
        this.#content = value;
    }

    get selected() {
        return this.#selected;
    }

    set selected(value) {
        this.#selected = value;
    }

    render() {

        this.style.display = "block"

        builder(this, {
            element : "div",
            style : {
                textAlign : "center",
                width : "100px",
                lineHeight : "48px",
                borderBottom : () => {
                    if (this.#selected) {
                        return "2px solid var(--main-selected-color)"
                    } else {
                        return "2px solid var(--main-dark1-color)"
                    }
                }
            },
            initialize : (element) => {
                element.appendChild(this.#content);
            }
        })
    }


}

customComponents.define("mat-tab", MatTab);
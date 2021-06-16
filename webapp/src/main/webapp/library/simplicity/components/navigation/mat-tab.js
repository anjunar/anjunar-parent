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
                width : "100%",
                lineHeight : "48px",
                borderBottom : () => {
                    if (this.#selected) {
                        return "1px solid var(--main-selected-color)"
                    } else {
                        return "1px solid var(--main-grey-color)"
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
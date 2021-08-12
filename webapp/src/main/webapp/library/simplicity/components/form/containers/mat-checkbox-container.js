import {builder, customComponents} from "../../../simplicity.js";

export default class MatCheckboxContainer extends HTMLElement {

    #content;
    #placeholder;

    get content() {
        return this.#content
    }

    set content(value) {
        this.#content = value;
    }

    get placeholder() {
        return this.#placeholder;
    }

    set placeholder(value) {
        this.#placeholder = value;
    }

    render() {
        builder(this, {
            element : "div",
            style : {
                display : "flex"
            },
            children : [
                {
                    element : "div",
                    initialize : (element) => {
                        element.appendChild(this.#content);
                    }
                },
                {
                    element: "div",
                    style : {
                        lineHeight : "18px"
                    },
                    text : () => {
                        return this.#placeholder;
                    }
                }
            ]
        })
    }

}

customComponents.define("mat-checkbox-container", MatCheckboxContainer);
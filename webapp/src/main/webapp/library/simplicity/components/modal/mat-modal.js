import {builder, customComponents} from "../../simplicity.js";

export default class MatModal extends HTMLElement {

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
                top : "0",
                left : "0",
                position : "absolute",
                display : "flex",
                width : "100%",
                height : "100vh",
                alignItems : "center",
                justifyContent : "center",
                backgroundColor : "rgba(1, 1, 1, 0.5)"
            },
            initialize : (element) => {
                element.appendChild(this.#content);
            }
        })
    }


}

customComponents.define("mat-modal", MatModal)
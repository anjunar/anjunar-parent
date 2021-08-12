import {builder, customComponents} from "../../simplicity.js";
import MatModal from "./mat-modal.js";
import MatPanel from "./mat-panel.js";

export default class MatDialog extends HTMLElement {

    #content;
    #header;
    #footer;
    #enclosing;

    get content() {
        return this.#content;
    }

    set content(value) {
        this.#content = value;
    }

    get header() {
        return this.#header;
    }

    set header(value) {
        this.#header = value;
    }

    get footer() {
        return this.#footer;
    }

    set footer(value) {
        this.#footer = value;
    }

    get enclosing() {
        return this.#enclosing;
    }

    set enclosing(value) {
        this.#enclosing = value;
    }

    close() {
        this.#enclosing.remove();
    }

    render() {
        builder(this, {
            element: MatModal,
            content: {
                element: MatPanel,
                header: this.#header,
                content: {
                    element: "div",
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
                                display : "flex"
                            },
                            children : [
                                {
                                    element: "button",
                                    type : "button",
                                    text : "Close",
                                    onClick : () => {
                                        this.close();
                                    }
                                }
                            ],
                            initialize : (element) => {
                                if (this.#footer) {
                                    element.appendChild(this.#footer);
                                }
                            }
                        }
                    ]
                }
            }
        })
    }

}

customComponents.define("mat-dialog", MatDialog)
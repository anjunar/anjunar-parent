import {builder, customComponents} from "../../simplicity.js";

export default class MatPanel extends HTMLElement {

    #header = ""
    #open = true;
    #content;

    get header() {
        return this.#header;
    }

    set header(value) {
        this.#header = value;
    }

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

    toggle() {
        this.#open = ! this.#open;
    }

    render() {
        builder(this, [{
            element : "div",
            style : {
                display : "flex",
                backgroundColor : "var(--main-grey-color)"
            },
            children : [
                {
                    element : "div",
                    style : {
                        fontSize : "12px",
                        padding : "5px",
                        color : "var(--main-background-color)",
                        lineHeight : "24px"
                    },
                    text : () => {
                        return this.#header;
                    }
                },
                {
                    element : "div",
                    style : {
                        flex : "1"
                    }
                },
                {
                    element : "button",
                    type : "button",
                    className : "material-icons",
                    onClick : () => {
                        this.toggle();
                    },
                    style : {
                        display : () => {
                            return ! this.#open ? "block" : "none";
                        }
                    },
                    text : "keyboard_arrow_up"
                },
                {
                    element : "button",
                    type : "button",
                    className : "material-icons",
                    onClick : () => {
                        this.toggle();
                    },
                    style : {
                        display : () => {
                            return this.#open ? "block" : "none";
                        }
                    },
                    text : "keyboard_arrow_down"
                }
            ]
        }, {
            element: "div",
            style : {
                backgroundColor : "var(--main-background-color)",
                padding : "5px",
                display : () => {
                    return this.#open ? "block" : "none";
                }
            },
            initialize : (element) => {
                element.appendChild(this.#content)
            }
        }])
    }

}

customComponents.define("mat-panel", MatPanel)
import {builder, customComponents} from "../../simplicity.js"

export default class MatToolbar extends HTMLElement {

    #left = [];
    #middle = [];
    #right = [];

    get left() {
        return this.#left;
    }

    set left(value) {
        this.#left = value;
    }

    get middle() {
        return this.#middle;
    }

    set middle(value) {
        this.#middle = value;
    }

    get right() {
        return this.#right;
    }

    set right(value) {
        this.#right = value;
    }

    render() {

        this.style.display = "block";
        this.style.position = "sticky";
        this.style.top = "0";
        this.style.left = "0"
        this.style.height = "50px";

        builder(this, {
            element : "table",
            style : {
                backgroundColor : "var(--main-dark1-color)"
            },
            children : [
                {
                    element : "tr",
                    children : [
                        {
                            element : "td",
                            style : {
                                width : "33%",
                                verticalAlign : "middle"
                            },
                            initialize : (element) => {
                                for (const item of this.#left) {
                                    element.appendChild(item);
                                }
                            }
                        },
                        {
                            element : "td",
                            style : {
                                width : "33%",
                                textAlign : "center",
                                verticalAlign : "middle"
                            },
                            initialize : (element) => {
                                for (const item of this.#middle) {
                                    element.appendChild(item);
                                }
                            }
                        },
                        {
                            element : "td",
                            style : {
                                width : "33%",
                                textAlign : "right",
                                verticalAlign : "middle"
                            },
                            initialize : (element) => {
                                for (const item of this.#right) {
                                    element.appendChild(item);
                                }
                            }
                        }
                    ]
                }
            ]
        })

    }

}

customComponents.define("mat-toolbar", MatToolbar);
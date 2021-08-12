import {builder, customComponents} from "../../simplicity.js";

export default class MatMenu extends HTMLElement {

    #open = false;
    #content;
    #subMenu = [];

    get content() {
        return this.#content;
    }

    set content(value) {
        this.#content = value;
    }

    get subMenu() {
        return this.#subMenu;
    }

    set subMenu(value) {
        this.#subMenu = value;
    }

    render() {

        this.style.lineHeight = "32px"

        builder(this, {
            element: "div",
            style : {
                position: "relative"
            },
            onMouseenter : () => {
                this.#open = true;
            },
            onMouseleave : () => {
                this.#open = false;
            },
            children: [
                {
                    element: "div",
                    className: "item",
                    style: {
                        padding: "5px"
                    },
                    initialize : (element) => {
                        element.appendChild(this.#content);
                    }
                },
                {
                    element: "div",
                    style: {
                        position: "absolute",
                        width: "100%",
                        top: "36px",
                        left: "0",
                        backgroundColor: "var(--main-dark1-color)"
                    },
                    if : () => {
                        return this.#open
                    },
                    initialize : (element) => {
                        for (const sub of this.#subMenu) {
                            element.appendChild(sub);
                        }
                    }
                }
            ]
        })
    }

}

customComponents.define("mat-menu", MatMenu)
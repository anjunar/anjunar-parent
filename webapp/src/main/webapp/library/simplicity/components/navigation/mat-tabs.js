import {builder, customComponents} from "../../simplicity.js";

export default class MatTabs extends HTMLElement {

    #contents = [];

    get contents() {
        return this.#contents;
    }

    set contents(value) {
        this.#contents = value;
    }

    render() {
        let deSelectAll = () => {
            for (const content of this.#contents) {
                content.selected = false;
            }
        }

        builder(this, {
            element : "div",
            style : {
                display : "flex"
            },
            children : [
                {
                    element : "div",
                    style : {
                        display : "flex"
                    },
                    initialize : (element) => {
                        for (const content of this.#contents) {
                            element.appendChild(content);
                            content.addEventListener("click", () => {
                                deSelectAll();

                                content.selected = true;

                                let indexOf = this.#contents.indexOf(content);
                                this.dispatchEvent(new CustomEvent("page", {detail : {page : indexOf}}))
                            })
                        }
                        this.#contents[0].selected = true;
                    }
                }, {
                    element: "div",
                    style: {
                        flex : "1",
                        borderBottom: "1px solid var(--main-grey-color)"
                    }
                }
            ]
        })
    }

}

customComponents.define("mat-tabs", MatTabs);
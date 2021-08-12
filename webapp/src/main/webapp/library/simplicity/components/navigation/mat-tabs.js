import {builder, customComponents} from "../../simplicity.js";

export default class MatTabs extends HTMLElement {

    #meta;
    #items;

    get meta() {
        return this.#meta;
    }

    set meta(value) {
        this.#meta = value;
    }

    get items() {
        return this.#items;
    }

    set items(value) {
        this.#items = value;
    }

    render() {
        let deSelectAll = () => {
            let tabs = this.querySelectorAll("mat-tab");
            for (const content of tabs) {
                content.selected = false;
            }
        }

        builder(this, {
            element: "div",
            style: {
                display: "flex"
            },
            children: [
                {
                    element: "div",
                    style: {
                        display: "flex"
                    },
                    children: {
                        items: () => {
                            return this.#items();
                        },
                        item: (item, index, array) => {
                            return {
                                element: "div",
                                initialize: (element) => {
                                    element.addEventListener("click", () => {
                                        deSelectAll();
                                        let matTab = element.querySelector("mat-tab");
                                        matTab.selected = true
                                        this.dispatchEvent(new CustomEvent("page", {detail: {page: index}}))
                                    })

                                    let tree = this.#meta.item.element;
                                    builder(element, tree(item))

                                    if (index === 0) {
                                        let matTab = element.querySelector("mat-tab");
                                        matTab.selected = true
                                    }
                                }
                            }
                        }
                    }
                }, {
                    element: "div",
                    style: {
                        flex: "1",
                        borderBottom: "2px solid var(--main-dark1-color)"
                    }
                }
            ]
        })
    }

}

customComponents.define("mat-tabs", MatTabs);
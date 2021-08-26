import {builder, customComponents} from "../../simplicity.js";
import {hateoas} from "../../services/tools.js";
import {i18nFactory} from "../../services/i18nResolver.js";

export default class MatGrid extends HTMLElement {

    #window = []
    #meta;
    #items;
    #emptyItem;

    #index = 0;
    #limit = 20;
    #size = 0;

    #links;

    get emptyItem() {
        return this.#emptyItem;
    }

    set emptyItem(value) {
        this.#emptyItem = value;
    }

    get meta() {
        return this.#meta;
    }

    set meta(value) {
        this.#meta = value
    }

    get items() {
        return this.#items;
    }

    set items(value) {
        this.#items = value;
    }

    get limit() {
        return this.#limit;
    }

    set limit(value) {
        this.#limit = value
    }

    load() {
        this.#items({index: this.#index, limit: this.#limit}, (_items, _size, _links) => {
            this.#size = _size;
            this.#window = _items;

            if (this.#emptyItem) {
                for (let i = this.#window.length; i < this.#limit; i++) {
                    this.#window.push(this.#emptyItem)
                }
            }
            this.#links = _links;
        });
    }

    reload() {
        this.load();
    }

    render() {

        let skipPrevious = () => {
            this.#index = 0;
            this.load();
        }

        let arrowLeft = () => {
            this.#index -= this.#limit;
            this.load();
        }

        let canArrowLeft = () => {
            return this.#index > 0;
        }

        let arrowRight = () => {
            this.#index += this.#limit;
            this.load();
        }

        let canArrowRight = () => {
            return this.#index + this.#limit < this.#size;
        }

        let skipNext = () => {
            let number = Math.round(this.#size / this.#limit);
            this.#index = (number - 1) * this.#limit;
            this.load();
        }

        this.load();

        this.style.display = "block";
        this.style.position = "relative";

        builder(this, [
            {
                element: "div",
                style: {
                    display: "flex",
                    flexWrap: "wrap",
                    justifyContent : "space-between"
                },
                onItems: (event) => {
                    this.dispatchEvent(new CustomEvent("items"))
                },
                children:
                    {
                        items: () => {
                            return this.#window;
                        },
                        item: (item) => {
                            return {
                                element: "div",
                                onClick: (event) => {
                                    event.stopPropagation();
                                    this.dispatchEvent(new CustomEvent("item", {detail: {item: item}}))
                                    return false;
                                },
                                initialize: (element) => {
                                    builder(element, this.#meta(item));
                                }
                            }
                        }
                    }
            },
            {
                element: "div",
                style: {
                    display: "flex",
                    width : "100%"
                },
                children: [
                    {
                        element: "div",
                        style: {
                            marginLeft : "5px",
                            lineHeight: "42px"
                        },
                        text: () => {
                            return `${this.#index} - ${this.#index + this.#limit} ${i18n("of")} ${this.#size}`
                        }
                    },
                    {
                        element: "button",
                        type: "button",
                        className: "material-icons",
                        onClick: () => {
                            skipPrevious();
                        },
                        text: "skip_previous"
                    },
                    {
                        element: "button",
                        type: "button",
                        className: "material-icons",
                        onClick: () => {
                            arrowLeft();
                        },
                        disabled: () => {
                            return !canArrowLeft();
                        },
                        text: "keyboard_arrow_left"
                    },
                    {
                        element: "button",
                        type: "button",
                        className: "material-icons",
                        onClick: () => {
                            arrowRight();
                        },
                        disabled: () => {
                            return !canArrowRight();
                        },
                        text: "keyboard_arrow_right"
                    },
                    {
                        element: "button",
                        type: "button",
                        className: "material-icons",
                        onClick: () => {
                            skipNext();
                        },
                        text: "skip_next"
                    },
                    {
                        element: "div",
                        style: {
                            flex: "1"
                        }
                    },
                    {
                        element: "button",
                        type: "button",
                        text: i18n("Create"),
                        style: {
                            display: () => {
                                let link = hateoas(this.#links, "create");
                                return link ? "block" : "none"
                            }
                        },
                        onClick: (event) => {
                            event.stopPropagation();
                            let link = this.#links.find((link) => link.rel === "create")
                            this.dispatchEvent(new CustomEvent("create", {detail: link}))
                            return false;
                        }
                    }
                ]
            }
        ])
    }
}

const i18n = i18nFactory({
    "of" : {
        "en-DE" : "of",
        "de-DE" : "von"
    },
    "Create": {
        "en-DE": "Create",
        "de-DE": "Erstellen"
    },
    "Configuration": {
        "en-DE": "Configuration",
        "de-DE": "Konfiguration"
    }
});

customComponents.define("mat-grid", MatGrid);
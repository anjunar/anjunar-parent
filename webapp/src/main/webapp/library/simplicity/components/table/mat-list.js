import {builder, customComponents} from "../../simplicity.js";
import {i18nFactory} from "../../services/i18nResolver.js";

export default class MatList extends HTMLElement {

    #index = 0;
    #limit = 5;
    #size;


    #data = [];
    #links = [];
    #items;
    #meta;

    get index() {
        return this.#index;
    }

    set index(value) {
        this.#index = value;
    }

    get limit() {
        return this.#limit;
    }

    set limit(value) {
        this.#limit = value;
    }

    get items() {
        return this.#items;
    }

    set items(value) {
        this.#items = value;
    }

    get meta() {
        return this.#meta;
    }

    set meta(value) {
        this.#meta = value;
    }

    search(search) {
        this.load(search);
    }

    load(search) {

        let query = {
            index: this.#index,
            limit: this.#limit,
            search: search
        };

        this.#items(query, (_items, _size, _links) => {
            this.#size = _size;
            this.#data = _items;
            this.#links = _links;
        });
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

        this.style.display = "block"

        builder(this, [
            {
                element: "div",
                children: {
                    items: () => {
                        return this.#data;
                    },
                    item: (item) => {
                        return {
                            element: "div",
                            initialize: (element) => {
                                let tree = this.#meta;
                                builder(element, tree(item));
                                element.addEventListener("click", () => {
                                    this.dispatchEvent(new CustomEvent("item", {detail : item}))
                                })
                            }
                        }
                    }
                }
            },
            {
                element: "div",
                style: {
                    display: "flex"
                },
                children: [
                    {
                        element: "div",
                        style: {
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
                    }, {
                        element: "div",
                        style: {
                            flex: "1"
                        }
                    },
                    {
                        element: "div",
                        style : {
                            display : "flex"
                        },
                        children : {
                            items : () => {
                                return this.#links.filter(link => link.rel === "create")
                            },
                            item : (link) => {
                                return {
                                    element: "button",
                                    type: "button",
                                    style : {
                                        display : "block"
                                    },
                                    text: i18n("Create"),
                                    onClick: (event) => {
                                        event.stopPropagation();
                                        this.dispatchEvent(new CustomEvent("create", {detail: link}));
                                        return false;
                                    }
                                }
                            }
                        }
                    },
                ]
            }
        ])
    }

}

const i18n = i18nFactory({
    "of": {
        "en-DE": "of",
        "de-DE": "von"
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

customComponents.define("mat-list", MatList)
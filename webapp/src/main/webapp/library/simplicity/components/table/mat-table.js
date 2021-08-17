import {builder, customComponents} from "../../simplicity.js";
import {hateoas} from "../../services/tools.js";
import {i18nFactory} from "../../services/i18nResolver.js";
import MatTableDialog from "./mat-table-dialog.js"
import {windowManager} from "../../services/window-manager.js";

export default class MatTable extends HTMLTableElement {

    #meta;
    #items;

    #columns = [];

    #index = 0;
    #limit = 5;

    #data = [];
    #size;
    #links = [];

    #header = true;
    #configuration = true;

    get limit() {
        return this.#limit;
    }

    set limit(value) {
        this.#limit = value;
    }

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

    get columns() {
        return this.#columns;
    }

    set columns(value) {
        this.#columns = value;
    }

    get configuration() {
        return this.#configuration;
    }

    set configuration(value) {
        this.#configuration = value;
    }

    left(index) {
        let element = this.#columns[index];
        let other = this.#columns[index - 1];

        this.#columns[index] = other;
        this.#columns[index - 1] = element;

        this.#columns = Array.from(this.#columns);

        this.load();
    }

    right(index) {
        let element = this.#columns[index];
        let other = this.#columns[index + 1];

        this.#columns[index] = other;
        this.#columns[index + 1] = element;

        this.#columns = Array.from(this.#columns);

        this.load();
    }

    load(search) {

        let sort = this.#columns
            .filter((column) => column.sort !== undefined && column.sort !== "disabled")
            .map((column) => {
                return column.search + ":" + column.sort;
            })

        let query = {
            index: this.#index,
            limit: this.#limit,
            sort: sort,
            search: search
        };

        this.#items(query, (_items, _size, _links) => {
            this.#size = _size;
            this.#data = _items;
            this.#links = _links;
        });
    }

    get header() {
        return this.#header;
    }

    set header(value) {
        this.#header = value;
    }

    search(search) {
        this.load(search);
    }

    render() {

        let open = () => {
            let matTableDialog = new MatTableDialog();
            matTableDialog.table = this;

            let url = `/library/simplicity/components/table/mat-table-dialog`;

            windowManager.openWindow(matTableDialog, url);
        }

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

        let desc = (td) => {
            let column = this.#columns[td.index];
            column.sort = "desc";
            this.load();
        }

        let asc = (td) => {
            let column = this.#columns[td.index];
            column.sort = "asc";
            this.load();
        }

        let none = (td) => {
            let column = this.#columns[td.index];
            column.sort = undefined;
            this.load();
        }

        let sort = (index) => {
            if (this.#meta.colgroup) {
                if (this.#meta.colgroup.length > index) {
                    let element = this.#meta.colgroup[index].element();
                    let path = element.attributes.path;
                    return path();
                }
                return false;
            } else {
                return false;
            }
        }

        let visible = (index) => {
            if (this.#meta.colgroup) {
                if (this.#meta.colgroup.length > index) {
                    let element = this.#meta.colgroup[index].element();
                    let visible = element.attributes.visible;
                    return visible();
                }
                return false;
            } else {
                return false;
            }
        }

        let sortable = (index) => {
            if (this.#meta.colgroup) {
                if (this.#meta.colgroup.length > index) {
                    let element = this.#meta.colgroup[index].element();
                    let sortable = element.attributes.sortable;
                    return sortable();
                }
                return false;
            } else {
                return false;
            }
        }

        for (let i = 0; i < this.#meta.body.length; i++) {
            this.#columns.push({
                index: i,
                visible: visible(i),
                sort: sort(i) ? undefined : "disabled",
                sortable : sortable(i),
                search: sort(i)
            });
        }

        this.load();

        builder(this, [
                {
                    element: "thead",
                    children: [
                        {
                            element: "tr",
                            if: () => {
                                return this.#meta.header && this.#header;
                            },
                            children: {
                                type: "json",
                                items: () => {
                                    return this.#columns.filter((column) => column.visible);
                                },
                                item: (item, index) => {
                                    return {
                                        element: "td",
                                        children: [
                                            {
                                                element: "div",
                                                style: {
                                                    display: "flex",
                                                    alignItems: "center"
                                                },
                                                children: [
                                                    {
                                                        element: "div",
                                                        onClick: (event) => {
                                                            event.stopPropagation();
                                                            open();
                                                            return false;
                                                        },
                                                        initialize: (element) => {
                                                            if (this.#meta.header) {
                                                                let m = this.#meta.header[item.index];
                                                                builder(element, m.element());
                                                            }
                                                        }
                                                    },
                                                    {
                                                        element: "div",
                                                        style: {
                                                            marginLeft: "5px",
                                                        }
                                                    },
                                                    {
                                                        element: "button",
                                                        type: "button",
                                                        className: "icon",
                                                        text: "sort",
                                                        style: {
                                                            display: () => {
                                                                return item.sort === undefined ? "block" : "none";
                                                            }
                                                        },
                                                        onClick: () => {
                                                            asc(item);
                                                        }
                                                    },
                                                    {
                                                        element: "button",
                                                        type: "button",
                                                        className: "icon",
                                                        text: "expand_more",
                                                        style: {
                                                            display: () => {
                                                                return item.sort === "asc" ? "block" : "none";
                                                            }
                                                        },
                                                        onClick: () => {
                                                            desc(item);
                                                        }
                                                    },
                                                    {
                                                        element: "button",
                                                        type: "button",
                                                        className: "icon",
                                                        text: "expand_less",
                                                        style: {
                                                            display: () => {
                                                                return item.sort === "desc" ? "block" : "none";
                                                            }
                                                        },
                                                        onClick: () => {
                                                            none(item);
                                                        }
                                                    }
                                                ]
                                            }
                                        ]
                                    }
                                }
                            }
                        }
                    ]
                },
                {
                    element: "tbody",
                    children: {
                        items: () => {
                            return this.#data;
                        },
                        item: (tr) => {
                            return {
                                element: "tr",
                                onClick: (event) => {
                                    event.stopPropagation();
                                    this.dispatchEvent(new CustomEvent("row", {detail: tr}))
                                    return false;
                                },
                                children: {
                                    type: "json",
                                    items: () => {
                                        return this.#columns.filter((column) => column.visible);
                                    },
                                    item: (td, index) => {
                                        return {
                                            element: "td",
                                            className: "col-" + index,
                                            children: [
                                                {
                                                    element: "div",
                                                    initialize: (element) => {
                                                        let m = this.#meta.body[td.index];
                                                        builder(element, m.element(tr));
                                                    }
                                                }
                                            ]
                                        }
                                    }
                                }
                            }
                        }
                    }

                },
                {
                    element: "tfoot",
                    children: [
                        {
                            element: "tr",
                            children: [
                                {
                                    element: "td",
                                    colSpan: String(this.#meta.body.length),
                                    children: [
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
                                                    element: "button",
                                                    type: "button",
                                                    style: {
                                                        display: () => {
                                                            return this.#configuration ? "block" : "none"
                                                        }
                                                    },
                                                    text: i18n("Configuration"),
                                                    onClick: () => {
                                                        open();
                                                    }
                                                }
                                            ]
                                        }
                                    ]
                                }
                            ]
                        }
                    ]
                }
            ]
        )

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

customComponents.define("mat-table", MatTable, {extends: "table"})


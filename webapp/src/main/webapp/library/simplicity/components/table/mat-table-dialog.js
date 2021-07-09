import {builder, customViews, HTMLWindow} from "../../simplicity.js";
import MatCheckboxContainer from "../form/containers/mat-checkbox-container.js";
import DomInput from "../../directives/dom-input.js";
import {windowManager} from "../../services/window-manager.js";

export default class MatTableDialog extends HTMLWindow {

    #table;

    get table() {
        return this.#table;
    }

    set table(value) {
        this.#table = value;
    }

    render() {
        builder(this, {
            element: "table",
            children: {
                items: () => {
                    return this.table.columns;
                },
                item: (tr, index, array) => {
                    return {
                        element: "tr",
                        children: [
                            {
                                element: "td",
                                children: [
                                    {
                                        element: "div",
                                        initialize: (element) => {
                                            let meta = this.table.meta.header[tr.index];
                                            builder(element, meta.element());
                                        }
                                    }
                                ]
                            },
                            {
                                element: "td",
                                children: [
                                    {
                                        element: MatCheckboxContainer,
                                        placeholder: "Visible",
                                        content: {
                                            element: DomInput,
                                            type: "checkbox",
                                            value: {
                                                input: () => {
                                                    return tr.visible;
                                                },
                                                output: (value) => {
                                                    tr.visible = value;
                                                }
                                            }
                                        }
                                    }
                                ]
                            },
                            {
                                element: "td",
                                initialize: (element) => {
                                    if (this.table.meta.filter) {
                                        let meta = this.table.meta.filter[tr.index].element(tr);
                                        builder(element, meta);
                                    } else {
                                        builder(element, {
                                            element: DomInput,
                                            style: {
                                                marginLeft: "5px",
                                                width: "80px"
                                            },
                                            placeholder: "search",
                                            type: "text",
                                            onKeyup: (event) => {
                                                let value = event.target.value;
                                                this.table.search({property: tr.search, value: value});
                                            }
                                        })
                                    }
                                }
                            },
                            {
                                element: "td",
                                children: [
                                    {
                                        element: "button",
                                        type: "button",
                                        onClick: () => {
                                            this.table.left(index);
                                        },
                                        disabled: () => {
                                            return index === 0;
                                        },
                                        className: "material-icons",
                                        text: "arrow_drop_up"
                                    },
                                    {
                                        element: "button",
                                        type: "button",
                                        onClick: () => {
                                            this.table.right(index);
                                        },
                                        disabled: () => {
                                            return index === array.length - 1;
                                        },
                                        className: "material-icons",
                                        text: "arrow_drop_down"
                                    }
                                ]
                            }
                        ]
                    }
                }
            }
        })
    }

}

customViews.define({
    name: "mat-table-dialog",
    header: "Table Setup",
    resizable: false,
    class: MatTableDialog
})
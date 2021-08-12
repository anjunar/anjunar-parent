import {builder, customViews, HTMLWindow} from "../../simplicity.js";
import MatCheckboxContainer from "../form/containers/mat-checkbox-container.js";
import DomInput from "../../directives/dom-input.js";
import MatTabs from "../navigation/mat-tabs.js";
import MatTab from "../navigation/mat-tab.js";
import MatPages from "../navigation/mat-pages.js";
import MatPage from "../navigation/mat-page.js";

export default class MatTableDialog extends HTMLWindow {

    #table;
    #page = 0;

    get table() {
        return this.#table;
    }

    set table(value) {
        this.#table = value;
    }

    render() {
        builder(this, [{
            element : "div",
            children : [
                {
                    element: MatTabs,
                    items : {
                        direct : () => {
                            return this.table.columns;
                        }
                    },
                    meta : {
                        item : {
                            element: (tr) => {
                                return {
                                    element: MatTab,
                                    content: {
                                        element: "div",
                                        initialize: (element) => {
                                            let meta = this.table.meta.header[tr.index];
                                            builder(element, meta.element());
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onPage: (event) => {
                        let page = event.detail.page;
                        let column = this.table.columns[page];
                        this.#page = column.index
                    }
                },
                {
                    element: MatPages,
                    page: {
                        input: () => {
                            return this.#page;
                        }
                    },
                    contents: this.#table.columns.map((tr, index, array) => {
                        return {
                            element: MatPage,
                            contents: [
                                {
                                    element: "div",
                                    style : {
                                        display : "flex"
                                    },
                                    children : [
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
                                        }, {
                                            element: "div",
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
                                        }, {
                                            element: "div",
                                            children: [
                                                {
                                                    element: "button",
                                                    type: "button",
                                                    onClick: () => {
                                                        let column = this.table.columns.find(col => col.index === index)
                                                        let newIndex = this.table.columns.indexOf(column);
                                                        this.table.left(newIndex);
                                                    },
                                                    disabled: () => {
                                                        let column = this.table.columns.find(col => col.index === index)
                                                        let newIndex = this.table.columns.indexOf(column);
                                                        return newIndex === 0;
                                                    },
                                                    className: "material-icons",
                                                    text: "chevron_left"
                                                },
                                                {
                                                    element: "button",
                                                    type: "button",
                                                    onClick: () => {
                                                        let column = this.table.columns.find(col => col.index === index)
                                                        let newIndex = this.table.columns.indexOf(column);
                                                        this.table.right(newIndex);
                                                    },
                                                    disabled: () => {
                                                        let column = this.table.columns.find(col => col.index === index)
                                                        let newIndex = this.table.columns.indexOf(column);
                                                        return newIndex === array.length - 1;
                                                    },
                                                    className: "material-icons",
                                                    text: "chevron_right"
                                                }
                                            ]
                                        }

                                    ]
                                }
                            ]
                        }
                    })
                }
            ]
        }])
    }

}

customViews.define({
    name: "mat-table-dialog",
    header: "Table Setup",
    resizable: false,
    class: MatTableDialog
})
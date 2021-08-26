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
                    page : {
                        input: () => {
                            return this.#page;
                        }
                    },
                    onPage: (event) => {
                        let index = event.detail.page;
                        let column = this.table.columns[index];
                        let newIndex = column.index
                        this.#page = newIndex
                    },
                    meta : (tr) => {
                        return {
                            element: MatTab,
                            content: {
                                element: "div",
                                initialize: (element) => {
                                    let meta = this.table.meta.header[tr.index];
                                    builder(element, meta());
                                }
                            }
                        }
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
                                        position : "relative",
                                        height : "300px",
                                        width:  "100%"
                                    },
                                    children : [
                                        {
                                            element : "div",
                                            style : {
                                                position: "absolute",
                                                top: "50%",
                                                left : "50%",
                                                transform: "translate(-50%, -50%)",
                                            },
                                            children : [
                                                {
                                                    element : "div",
                                                    style : {
                                                        width : "200px"
                                                    },
                                                    children : [
                                                        {
                                                            element: "div",
                                                            initialize: (element) => {
                                                                if (this.table.meta.filter) {
                                                                    let meta = this.table.meta.filter[tr.index](tr);
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
                                                            element: "div",
                                                            style : {
                                                                display : "flex"
                                                            },
                                                            children : [
                                                                {
                                                                    element: "div",
                                                                    children: [
                                                                        {
                                                                            element: "button",
                                                                            type: "button",
                                                                            onClick: () => {
                                                                                let column = this.table.columns.find(col => col.index === index)
                                                                                let newIndex = this.table.columns.indexOf(column);
                                                                                this.table.left(newIndex);
                                                                                // this.#page = newIndex - 1
                                                                                window.setTimeout(() => {
                                                                                    let element = this.querySelector("mat-tabs")
                                                                                    element.dispatchEvent(new CustomEvent("page", {detail : {page : newIndex - 1}}))
                                                                                }, 100)
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
                                                                                // this.#page = newIndex + 1;
                                                                                window.setTimeout(() => {
                                                                                    let element = this.querySelector("mat-tabs")
                                                                                    element.dispatchEvent(new CustomEvent("page", {detail : {page : newIndex +1 }}))
                                                                                }, 100)
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
                                                                },
                                                                {
                                                                    element: "div",
                                                                    style : {
                                                                        flex : "1"
                                                                    }
                                                                },
                                                                {
                                                                    element: MatCheckboxContainer,
                                                                    placeholder: "Visible",
                                                                    style : {
                                                                        marginTop : "5px"
                                                                    },
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
                                                        }

                                                    ]
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
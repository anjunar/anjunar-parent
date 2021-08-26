import {builder, customViews, HTMLWindow} from "../../../../simplicity.js";
import EditorNodeInspector from "./contextmenu/editor-node-inspector.js";
import MatTabs from "../../../navigation/mat-tabs.js";
import MatTab from "../../../navigation/mat-tab.js";
import MatPages from "../../../navigation/mat-pages.js";
import MatPage from "../../../navigation/mat-page.js";

export default class EditorContextmenuDialog extends HTMLWindow {

    #path;
    #page = 0;

    get path() {
        return this.#path;
    }

    set path(value) {
        this.#path = value;
    }

    render() {
        builder(this, {
            element: "div",
            children: [
                {
                    element: MatTabs,
                    items : {
                        direct : () => {
                            return this.#path;
                        }
                    },
                    meta : (segment) => {
                        return {
                            element: MatTab,
                            content: {
                                element: "div",
                                text: segment.localName
                            }
                        }
                    },
                    onPage: (event) => {
                        let index = event.detail.page;
                        let column = this.table.columns.find(col => col.index === index)
                        let newIndex = this.table.columns.indexOf(column);
                        this.#page = newIndex
                    }
                },
                {
                    element: MatPages,
                    page: {
                        input: () => {
                            return this.#page;
                        }
                    },
                    contents: this.#path.map((segment) => {
                        return {
                            element: MatPage,
                            contents: [{
                                element: "div",
                                initialize: (element) => {
                                    let inspector = new EditorNodeInspector();
                                    inspector.node = segment;
                                    element.appendChild(inspector);
                                }
                            }]
                        }
                    })
                }
            ]
        })
    }

}

customViews.define({
    name : "editor-contextmenu-dialog",
    class : EditorContextmenuDialog,
    header : "Context"
})
import {builder, customComponents} from "../../../../simplicity.js";
import MatDialog from "../../../modal/mat-dialog.js";
import EditorNodeInspector from "./contextmenu/editor-node-inspector.js";
import MatTabs from "../../../navigation/mat-tabs.js";
import MatTab from "../../../navigation/mat-tab.js";
import MatPages from "../../../navigation/mat-pages.js";
import MatPage from "../../../navigation/mat-page.js";

export default class EditorContextmenuDialog extends HTMLElement {

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
            element : MatDialog,
            enclosing : this,
            header : "Inspector",
            content : {
                element : "div",
                children : [
                    {
                        element: MatTabs,
                        contents : this.#path.map((segment) => {
                            return {
                                element : MatTab,
                                content : {
                                    element : "div",
                                    text : segment.localName
                                }
                            }
                        }),
                        onPage : (event) => {
                            this.#page = event.detail.page;
                        }
                    },
                    {
                        element : MatPages,
                        page : {
                            input : () => {
                                return this.#page;
                            }
                        },
                        contents : this.#path.map((segment) => {
                            return {
                                element : MatPage,
                                contents : [{
                                    element : "div",
                                    initialize : (element) => {
                                        let inspector = new EditorNodeInspector();
                                        inspector.node = segment;
                                        element.appendChild(inspector);
                                    }
                                }]
                            }
                        })
                    }
                ]
            }
        })
    }

}

customComponents.define("editor-contextmenu-dialog", EditorContextmenuDialog)
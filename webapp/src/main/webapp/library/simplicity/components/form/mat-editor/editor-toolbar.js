import {builder, customComponents} from "../../../simplicity.js";
import ToolbarColors from "./toolbar/toolbar-colors.js";
import ToolbarFont from "./toolbar/toolbar-font.js";
import ToolbarJustify from "./toolbar/toolbar-justify.js";
import ToolbarTools from "./toolbar/toolbar-tools.js";
import ToolbarInserts from "./toolbar/toolbar-inserts.js";

export default class EditorToolbar extends HTMLElement {

    #editor;

    get editor() {
        return this.#editor;
    }

    set editor(value) {
        this.#editor = value;
    }

    render() {

        this.style.display = "block";

        builder(this, {
            element : "div",
            style : {
                display : "flex",
                alignItems : "stretch"
            },
            children : [
                {
                    element: "div",
                    className: "row",
                    children: [
                        {
                            element: ToolbarFont,
                            editor : this.#editor
                        },
                    ]
                },
                {
                    element : "div",
                    className : "row",
                    children : [
                        {
                            element : ToolbarColors,
                            editor : this.#editor
                        }
                    ]
                },
                {
                    element : "div",
                    className : "row",
                    children : [
                        {
                            element : ToolbarJustify,
                            editor : this.#editor
                        }
                    ]
                },
                {
                    element : "div",
                    className : "row",
                    children : [
                        {
                            element : ToolbarTools,
                            editor : this.#editor
                        }
                    ]
                },
                {
                    element : "div",
                    className : "row",
                    children : [
                        {
                            element : ToolbarInserts,
                            editor : this.#editor
                        }
                    ]
                }
            ]
        })
    }

}

customComponents.define("editor-toolbar", EditorToolbar)
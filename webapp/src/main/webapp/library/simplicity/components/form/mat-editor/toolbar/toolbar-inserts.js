import {builder, customComponents} from "../../../../simplicity.js";
import EditorTextDialog from "../dialog/editor-text-dialog.js";
import EditorTableDialog from "../dialog/editor-table-dialog.js";
import EditorTableElement from "../components/editor-table.js";
import {windowManager} from "../../../../services/window-manager.js";

export default class ToolbarInserts extends HTMLElement {

    #editor;

    get editor() {
        return this.#editor;
    }

    set editor(value) {
        this.#editor = value;
    }

    render() {
        builder(this, [
            {
                element: "div",
                style: {
                    display: "flex"
                },
                children: [
                    {
                        element: "button",
                        type: "button",
                        className: "iconBig",
                        title: "Link",
                        text: "insert_link",
                        onClick : () => {
                            let dialog = this.#editor.linkDialog();

                            let selection = document.getSelection();
                            let rangeAt = selection.getRangeAt(0);

                            dialog.addEventListener("load", (event) => {
                                document.getSelection().removeAllRanges();
                                document.getSelection().addRange(rangeAt);
                                document.execCommand("createLink", false, event.detail);
                            })

                            windowManager.openWindow(dialog, "/library/simplicity/components/form/mat-editor/dialog/editor-link-dialog")
                        }
                    },
                    {
                        element: "button",
                        type: "button",
                        className: "iconBig",
                        title: "unLink",
                        text: "link_off",
                        onClick : () => {
                            document.execCommand("unlink")
                        }
                    },
                    {
                        element: "button",
                        type: "button",
                        className: "iconBig",
                        title: "Image",
                        text: "image",
                        onClick : () => {
                            let dialog = this.#editor.imageDialog();

                            let selection = document.getSelection();
                            let rangeAt = selection.getRangeAt(0);

                            dialog.addEventListener("load", (event) => {
                                document.getSelection().removeAllRanges();
                                document.getSelection().addRange(rangeAt);
                                document.execCommand("insertImage", false, event.detail);
                            })

                            windowManager.openWindow(dialog, "/library/simplicity/components/form/mat-editor/dialog/editor-image-dialog")
                        }
                    },
                    {
                        element: "button",
                        type: "button",
                        className: "iconBig",
                        title: "Insert Hr",
                        text: "code",
                        onClick : () => {
                            document.execCommand("insertHorizontalRule")
                        }
                    },
                    {
                        element: "button",
                        type: "button",
                        className: "iconBig",
                        title: "Insert Text",
                        text: "short_text",
                        onClick : () => {
                            let dialog = new EditorTextDialog();

                            let selection = document.getSelection();
                            let rangeAt = selection.getRangeAt(0);

                            dialog.addEventListener("ok", (event) => {
                                document.getSelection().removeAllRanges();
                                document.getSelection().addRange(rangeAt);
                                document.execCommand("insertText", false, event.detail);
                                dialog.close();
                            })

                            windowManager.openWindow(dialog, "/library/simplicity/components/form/mat-editor/dialog/editor-text-dialog")
                        }
                    }
                ]
            },
            {
                element: "div",
                style: {
                    display: "flex"
                },
                children: [
                    {
                        element: "button",
                        type: "button",
                        className: "iconBig",
                        title: "Table",
                        text: "table_view",
                        onClick : () => {
                            let dialog = new EditorTableDialog();

                            let selection = document.getSelection();
                            let rangeAt = selection.getRangeAt(0);

                            dialog.addEventListener("ok", (event) => {
                                document.getSelection().removeAllRanges();
                                document.getSelection().addRange(rangeAt);

                                let table = new EditorTableElement();
                                let row = document.createElement("tr");
                                table.appendChild(row)

                                for (let i = 0; i <= event.detail; i++) {
                                    let td = document.createElement("td");
                                    row.appendChild(td);
                                }


                                document.execCommand("insertHTML", false, table.outerHTML)
                            })

                            windowManager.openWindow(dialog, "/library/simplicity/components/form/mat-editor/dialog/editor-table-dialog")
                        }
                    },
                    {
                        element: "button",
                        type: "button",
                        className: "iconBig",
                        title: "Insert ordered List",
                        text: "format_list_numbered",
                        onClick : () => {
                            document.execCommand("insertOrderedList")
                        }
                    },
                    {
                        element: "button",
                        type: "button",
                        className: "iconBig",
                        title: "Insert Unordered List",
                        text: "format_list_bulleted",
                        onClick : () => {
                            document.execCommand("insertUnorderedList")
                        }
                    },
                    {
                        element: "button",
                        type: "button",
                        className: "iconBig",
                        title: "Div FlexBox",
                        text: "table_chart",
                        onClick : () => {
                            let html = `<div is="editor-flexbox" style="display: flex">
                                                    <div style="flex: 1">Insert here...</div>
                                                </div>`;

                            document.execCommand("insertHTML", false, html)
                        }
                    },
                    {
                        element: "button",
                        type: "button",
                        className: "iconBig",
                        title: "Paragraph",
                        text: "notes",
                        onClick : () => {
                            document.execCommand("insertParagraph")
                        }
                    }
                ]
            }
        ])
    }

}

customComponents.define("toolbar-inserts", ToolbarInserts)

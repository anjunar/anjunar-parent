import {builder, customComponents} from "../../simplicity.js";
import EditorToolbar from "./mat-editor/editor-toolbar.js";
import EditorContextmenuDialog from "./mat-editor/dialog/editor-contextmenu-dialog.js";
import EditorLinkDialog from "./mat-editor/dialog/editor-link-dialog.js";
import EditorImageDialog from "./mat-editor/dialog/editor-image-dialog.js";
import {windowManager} from "../../services/window-manager.js";

export default class MatEditor extends HTMLElement {

    #value = "";
    #name;
    #disabled;

    #imageDialog = function () {
        return new EditorImageDialog();
    }
    #linkDialog = function () {
        return new EditorLinkDialog();
    }

    get isInput() {
        return true;
    }

    get value() {
        return this.#value;
    }

    set value(v) {
        this.#value = v;
    }

    get name() {
        return this.#name;
    }

    set name(value) {
        this.#name = value;
    }

    get disabled() {
        return this.#disabled;
    }

    set disabled(value) {
        this.#disabled = value;
    }

    get imageDialog() {
        return this.#imageDialog;
    }

    set imageDialog(value) {
        this.#imageDialog = value;
    }

    get linkDialog() {
        return this.#linkDialog;
    }

    set linkDialog(value) {
        this.#linkDialog = value;
    }

    render() {

        this.style.display = "block"

        builder(this, [
            {
                element: EditorToolbar,
                editor: this
            }, {
                element: "div",
                style : {
                    outline: "0 solid transparent",
                    height : "calc(100% - 70px)",
                    border : "var(--main-dark1-color) solid 1px",
                    margin : "2px",
                    overflow : "auto"
                },
                attributes : {
                    contentEditable : this.#disabled ? "false" : "true"
                },
                update : (element) => {
                    if (element.innerHTML !== this.#value.html) {
                        element.innerHTML = this.#value.html;
                    }
                },
                onContextmenu : (event) => {
                    if (! event.ctrlKey) {
                        let dialog = new EditorContextmenuDialog();

                        let content = this.querySelector("div[contenteditable=true]");

                        let path = Array.from(event.path);
                        let indexOf = path.indexOf(content);
                        path = path.slice(0, indexOf)


                        dialog.path = path

                        windowManager.openWindow(dialog, "/library/simplicity/components/form/mat-editor/dialog/editor-contextmenu-dialog")

                        event.preventDefault();
                    }
                }
            }
        ])

        let content = this.querySelector("div[contenteditable=true]");

        let observer = new MutationObserver(() => {
            this.#value = {
                html : content.innerHTML,
                text : content.innerText
            };
            this.dispatchEvent(new Event("change"));
        })

        observer.observe(content, {subtree : true, childList  : true, characterData : true, attributes : true})

    }

}

customComponents.define("mat-editor", MatEditor)
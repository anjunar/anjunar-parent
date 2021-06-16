import {builder, customComponents} from "../../../../simplicity.js";
import MatDialog from "../../../modal/mat-dialog.js";
import DomInput from "../../../../directives/dom-input.js";

export default class EditorLinkDialog extends HTMLElement {

    #value = ""

    get value() {
        return this.#value;
    }

    set value(v) {
        this.#value = v
    }

    render() {
        builder(this, {
            element : MatDialog,
            enclosing : this,
            header : "Table Setup",
            content : {
                element : "div",
                children : [
                    {
                        element : DomInput,
                        type : "text",
                        value : {
                            input : () => {
                                return this.#value;
                            },
                            output : (v) => {
                                this.#value = v;
                            }
                        }
                    }
                ]
            },
            footer : {
                element: "button",
                type : "button",
                text : "Ok",
                onClick : () => {
                    this.dispatchEvent(new CustomEvent("load", {detail : this.#value}))
                }
            }
        })
    }

}

customComponents.define("editor-link-dialog", EditorLinkDialog)
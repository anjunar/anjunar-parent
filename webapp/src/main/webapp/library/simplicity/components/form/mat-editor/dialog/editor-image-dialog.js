import {builder, customComponents} from "../../../../simplicity.js";
import MatDialog from "../../../modal/mat-dialog.js";
import MatImageUpload from "../../mat-image-upload.js";

export default class EditorImageDialog extends HTMLElement {

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
            header : "Image",
            content : {
                element : "div",
                style: {
                    minWidth: "200px",
                    maxWidth: "300px",
                    minHeight: "200px",
                    maxHeight: "300px"
                },
                children : [
                    {
                        element : MatImageUpload,
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
                    this.dispatchEvent(new CustomEvent("load", {detail : this.#value.data}))
                }
            }
        })
    }

}

customComponents.define("editor-image-dialog", EditorImageDialog)
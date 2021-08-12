import {builder, customComponents, customViews, HTMLWindow} from "../../../../simplicity.js";
import MatImageUpload from "../../mat-image-upload.js";

export default class EditorImageDialog extends HTMLWindow {

    #value = ""

    get value() {
        return this.#value;
    }

    set value(v) {
        this.#value = v
    }

    render() {
        builder(this, [{
                element: "div",
                style: {
                    minWidth: "200px",
                    maxWidth: "300px",
                    minHeight: "200px",
                    maxHeight: "300px"
                },
                children: [
                    {
                        element: MatImageUpload,
                        value: {
                            input: () => {
                                return this.#value;
                            },
                            output: (v) => {
                                this.#value = v;
                            }
                        }
                    }
                ]
            },
                {
                    element: "button",
                    type: "button",
                    text: "Ok",
                    onClick: () => {
                        this.dispatchEvent(new CustomEvent("load", {detail: this.#value.data}))
                    }
                }
            ]
        )
    }

}

customViews.define({
    name : "editor-image-dialog",
    class : EditorImageDialog,
    header : "Image"
})
import {builder, customComponents, customViews, HTMLWindow} from "../../../../simplicity.js";

export default class EditorTextDialog extends HTMLWindow {

    #value = "";

    render() {
        builder(this, [
            {
                element: "textarea",
                rows: "10",
                cols: "20",
                style: {
                    width: "400px",
                    height: "200px"
                },
                value: {
                    input: () => {
                        return this.#value;
                    },
                    output: (v) => {
                        this.#value = v;
                    }
                }
            },
            {
                element: "button",
                type: "button",
                text: "Ok",
                style: {
                    display: "block"
                },
                onClick: () => {
                    this.dispatchEvent(new CustomEvent("ok", {detail: this.#value}))
                }
            }]
        )
    }


}

customViews.define({
    name : "editor-text-dialog",
    class : EditorTextDialog,
    header : "Text"
})
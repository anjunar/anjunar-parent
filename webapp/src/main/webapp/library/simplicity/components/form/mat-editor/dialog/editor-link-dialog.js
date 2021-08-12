import {builder, customComponents, customViews, HTMLWindow} from "../../../../simplicity.js";
import DomInput from "../../../../directives/dom-input.js";

export default class EditorLinkDialog extends HTMLWindow {

    #value = ""

    get value() {
        return this.#value;
    }

    set value(v) {
        this.#value = v
    }

    render() {
        builder(this, [
                {
                    element: DomInput,
                    type: "text",
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
                    onClick: () => {
                        this.dispatchEvent(new CustomEvent("load", {detail: this.#value}))
                    }
                }
            ]
        )
    }

}

customViews.define({
    name : "editor-link-dialog",
    class : EditorLinkDialog,
    header : "Link"
})
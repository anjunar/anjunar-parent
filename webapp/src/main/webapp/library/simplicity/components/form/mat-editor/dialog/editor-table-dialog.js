import {builder, customComponents, customViews, HTMLWindow} from "../../../../simplicity.js";
import DomInput from "../../../../directives/dom-input.js";
import MatInputContainer from "../../containers/mat-input-container.js";

export default class EditorTableDialog extends HTMLWindow {

    #value;

    render() {
        builder(this, [
            {
                element: MatInputContainer,
                placeholder: "Columns",
                content: {
                    element: DomInput,
                    type: "number",
                    value: {
                        input: () => {
                            return this.#value;
                        },
                        output: (v) => {
                            this.#value = v;
                        }
                    }
                }
            },
            {
                element: "button",
                type: "button",
                text: "Ok",
                onClick: () => {
                    this.dispatchEvent(new CustomEvent("ok", {detail: this.#value}))
                }
            }])
    }

}

customViews.define({
    name : "editor-table-dialog",
    class : EditorTableDialog,
    header : "Table"
})
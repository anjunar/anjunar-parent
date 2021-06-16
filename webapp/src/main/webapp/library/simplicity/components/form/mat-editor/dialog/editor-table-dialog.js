import {builder, customComponents} from "../../../../simplicity.js";
import MatDialog from "../../../modal/mat-dialog.js";
import DomInput from "../../../../directives/dom-input.js";
import MatInputContainer from "../../containers/mat-input-container.js";

export default class EditorTableDialog extends HTMLElement {

    #value;

    render() {
        builder(this, {
            element : MatDialog,
            enclosing : this,
            header : "Table Setup",
            content : {
                element : "div",
                style : {
                    display : "flex"
                },
                children : [
                    {
                        element: MatInputContainer,
                        placeholder : "Columns",
                        content :                             {
                            element : DomInput,
                            type : "number",
                            value : {
                                input : () => {
                                    return this.#value;
                                },
                                output : (v) => {
                                    this.#value = v;
                                }
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
                    this.dispatchEvent(new CustomEvent("ok", {detail : this.#value}))
                }
            }
        })
    }

}

customComponents.define("editor-table-dialog", EditorTableDialog)
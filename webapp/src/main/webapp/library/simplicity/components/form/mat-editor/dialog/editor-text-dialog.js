import {builder, customComponents} from "../../../../simplicity.js";
import MatDialog from "../../../modal/mat-dialog.js";

export default class EditorTextDialog extends HTMLElement {

    #value = "";

    render() {
        builder(this, {
            element : MatDialog,
            enclosing : this,
            header : "Table Setup",
            content : {
                element : "div",
                children : [
                    {
                        element : "textarea",
                        rows : "10",
                        cols : "20",
                        style : {
                            width : "400px",
                            height : "200px"
                        },
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
            footer :  {
                element: "button",
                type : "button",
                text : "Ok",
                style : {
                    display : "block"
                },
                onClick : () => {
                    this.dispatchEvent(new CustomEvent("ok", {detail : this.#value}))
                }
            }
        })
    }


}

customComponents.define("editor-text-dialog", EditorTextDialog)
import {builder, customComponents} from "../../../../library/simplicity/simplicity.js";
import MatEditor from "../../../../library/simplicity/components/form/mat-editor.js";
import MatDialog from "../../../../library/simplicity/components/modal/mat-dialog.js";
import HateoasButton from "../../../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../../../library/simplicity/hateoas/hateoas-form.js";

export default class ReplyDialog extends HTMLElement {

    #reply;

    get reply() {
        return this.#reply;
    }

    set reply(value) {
        this.#reply = value;
    }

    render() {
        builder(this, {
            element : HateoasForm,
            model : this.#reply,
            children : [
                {
                    element : MatDialog,
                    enclosing : this,
                    header : "Reply",
                    content : {
                        element : MatEditor,
                        name : "editor",
                        style : {
                            height : "300px",
                            width : "800px"
                        }
                    },
                    footer : {
                        element: "div",
                        children: [
                            {
                                element: HateoasButton,
                                hateoas : "save",
                                text : "Save",
                                onAfterSubmit : () => {
                                    this.dispatchEvent(new CustomEvent("afterSubmit"));
                                    this.remove();
                                }
                            },
                            {
                                element: HateoasButton,
                                hateoas : "update",
                                text : "Update",
                                onAfterSubmit : () => {
                                    this.dispatchEvent(new CustomEvent("afterSubmit"))
                                    this.remove();
                                }
                            },
                            {
                                element: HateoasButton,
                                hateoas : "delete",
                                text : "Delete",
                                onAfterSubmit : () => {
                                    this.dispatchEvent(new CustomEvent("afterSubmit"))
                                    this.remove();
                                }
                            }
                        ]
                    }
                }
            ]
        })
    }

}

customComponents.define("topic-reply-dialog", ReplyDialog);

import {builder, customComponents} from "../../../../../library/simplicity/simplicity.js";
import MatDialog from "../../../../../library/simplicity/components/modal/mat-dialog.js";
import HateoasButton from "../../../../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../../../../library/simplicity/hateoas/hateoas-form.js";
import DomTextarea from "../../../../../library/simplicity/directives/dom-textarea.js";

export default class CommentDialog extends HTMLElement {

    #comment;

    get comment() {
        return this.#comment;
    }

    set comment(value) {
        this.#comment = value;
    }

    render() {
        builder(this, {
            element : HateoasForm,
            model : this.#comment,
            children : [{
                element : MatDialog,
                enclosing: this,
                header : "Comment",
                content : {
                    element : "div",
                    children : [
                        {
                            element : DomTextarea,
                            cols : "10",
                            rows : "10",
                            name : "text",
                            style : {
                                width : "400px"
                            }
                        }
                    ]
                },
                footer : {
                    element : "div",
                    children : [
                        {
                            element: HateoasButton,
                            hateoas : "update",
                            text : "Send",
                            onAfterSubmit : () => {
                                this.dispatchEvent(new CustomEvent("update"))
                                this.remove();
                            }
                        },
                        {
                            element: HateoasButton,
                            hateoas : "delete",
                            text : "Delete",
                            onAfterSubmit : () => {
                                this.dispatchEvent(new CustomEvent("update"))
                                this.remove();
                            }
                        }
                    ]
                }
            }]
        })
    }
}

customComponents.define("post-comment-dialog", CommentDialog)
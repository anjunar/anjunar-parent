import {builder, customComponents} from "../../../../library/simplicity/simplicity.js";
import MatDialog from "../../../../library/simplicity/components/modal/mat-dialog.js";
import MatImageUpload from "../../../../library/simplicity/components/form/mat-image-upload.js";
import HateoasButton from "../../../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../../../library/simplicity/hateoas/hateoas-form.js";
import DomTextarea from "../../../../library/simplicity/directives/dom-textarea.js";

export default class PostDialog extends HTMLElement {

    #post;

    get post() {
        return this.#post;
    }

    set post(value) {
        this.#post = value;
    }

    render() {
        builder(this, {
            element : HateoasForm,
            model : this.#post,
            children : [
                {
                    element : MatDialog,
                    enclosing : this,
                    header : "Post",
                    content : {
                        element : "div",
                        children : [
                            {
                                element : DomTextarea,
                                cols : "10",
                                rows : "10",
                                placeholder : "Write here...",
                                name : "text",
                                style : {
                                    width : "400px"
                                },
                            },{
                                element : "div",
                                style : {
                                    maxHeight : "200px",
                                    maxWidth : "200px",
                                    margin : "auto"
                                },
                                children : [
                                    {
                                        element : MatImageUpload,
                                        showButton : false,
                                        name : "image"
                                    }
                                ]
                            },{
                                element : "div",
                                style : {
                                    display : "flex",
                                    alignItems : "center",
                                    backgroundColor : "var(--main-dimmed-color)",
                                    padding : "5px"
                                },
                                children : [
                                    {
                                        element : "div",
                                        text : "Insert additional Content"
                                    },
                                    {
                                        element : "button",
                                        type : "button",
                                        className : "button material-icons",
                                        text : "photo",
                                        onClick : () => {
                                            let imageUpload = this.querySelector("mat-image-upload");
                                            imageUpload.click();
                                        }
                                    }
                                ]
                            }
                        ]
                    },
                    footer : {
                        element : "div",
                        style : {
                            display: "flex"
                        },
                        children : [
                            {
                                element: HateoasButton,
                                hateoas : "save",
                                text : "Save",
                                onAfterSubmit : () => {
                                    this.dispatchEvent(new CustomEvent("afterSubmit"))
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
                                hateoas : "update",
                                text : "Update",
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

customComponents.define("timeline-post-dialog", PostDialog)
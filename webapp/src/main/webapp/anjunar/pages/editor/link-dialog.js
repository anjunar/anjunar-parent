import {builder, customComponents} from "../../../library/simplicity/simplicity.js";
import MatDialog from "../../../library/simplicity/components/modal/mat-dialog.js";
import MatSelect from "../../../library/simplicity/components/form/mat-select.js";
import {jsonClient} from "../../../library/simplicity/services/client.js";

export default class LinkDialog extends HTMLElement {

    #value;

    render() {
        builder(this, {
            element : MatDialog,
            enclosing : this,
            header : "links",
            content : {
                element : "div",
                children : [
                    {
                        element: MatSelect,
                        placeholder: "Link",
                        label : "title",
                        value: {
                            input : () => {
                                return this.#value;
                            },
                            output : (v) => {
                                this.#value = v;
                            }
                        },
                        items: {
                            direct : (query, callback) => {
                                jsonClient.post(`service/pages/like?title=${query.value}&index=${query.index}&limit=${query.limit}`)
                                    .then((response) => {
                                        callback(response.rows, response.size);
                                    })
                            }
                        },
                        meta : {
                            option : {
                                element : (link) => {
                                    return {
                                        element : "div",
                                        text : link.title
                                    }
                                }
                            }
                        }
                    }
                ]
            },
            footer : {
                element : "button",
                type : "button",
                text : "Ok",
                onClick : () => {
                    this.dispatchEvent(new CustomEvent("load", {detail : this.#value}))
                }
            }
        })
    }
}

customComponents.define("editor-link-dialog2", LinkDialog)
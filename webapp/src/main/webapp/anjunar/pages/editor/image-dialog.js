import {builder, customComponents} from "../../../library/simplicity/simplicity.js";
import MatDialog from "../../../library/simplicity/components/modal/mat-dialog.js";
import MatGrid from "../../../library/simplicity/components/table/mat-grid.js";
import {jsonClient} from "../../../library/simplicity/services/client.js";
import DomInputFile from "../../../library/simplicity/directives/dom-input[file].js";

export default class ImageDialog extends HTMLElement {

    #page;

    get page() {
        return this.#page;
    }

    set page(value) {
        this.#page = value;
    }

    render() {
        builder(this, {
            element : MatDialog,
            enclosing : this,
            header : "Images",
            content : {
                element: "div",
                children : [
                    {
                        element : MatGrid,
                        items : {
                            direct : (query, callback) => {
                                let link = this.#page.links.find((link) => link.rel === "images");
                                jsonClient.get(`${link.url}?page=${this.#page.id}&index=${query.index}&limit=${query.limit}`)
                                    .then((response) => {
                                        callback(response.rows, response.size);
                                    });
                            }
                        },
                        meta : {
                            item : {
                                element : (picture) => {
                                    return {
                                        element : "div",
                                        children: [
                                            {
                                                element : "a",
                                                onClick : () => {
                                                    let link = picture.links.find((link) => link.rel === "read")
                                                    this.dispatchEvent(new CustomEvent("load", {detail : link.url}))
                                                    let dialog = this.querySelector("mat-dialog");
                                                    dialog.close();
                                                },
                                                children: [
                                                    {
                                                        element: "img",
                                                        src: picture.data,
                                                        style: {
                                                            height: "100px"
                                                        }
                                                    }
                                                ]
                                            },
                                            {
                                                element: "button",
                                                type : "button",
                                                text : "Delete",
                                                className : "button",
                                                style : {
                                                    display : "block",
                                                    margin : "auto"
                                                },
                                                onClick : () => {
                                                    let link = picture.links.find((link) => link.rel === "delete")
                                                    jsonClient.delete(`${link.url}?id=${picture.id}`)
                                                        .then(() => {
                                                            let grid = this.querySelector("mat-grid");
                                                            grid.reload();
                                                        })
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        }
                    },
                    {
                        element : "div",
                        children : [
                            {
                                element : DomInputFile,
                                style : {
                                    margin : "auto"
                                },
                                onLoadend : (event) => {
                                    let body = event.detail.load;
                                    body.page = this.#page;

                                    let link = this.#page.links.find((link) => link.rel === "upload");

                                    jsonClient.post(link.url, {body : body})
                                        .then(() => {
                                            let grid = this.querySelector("mat-grid");
                                            grid.reload();
                                        })
                                }
                            }
                        ]
                    }
                ]
            }
        })
    }
}

customComponents.define("editor-image-dialog2", ImageDialog)
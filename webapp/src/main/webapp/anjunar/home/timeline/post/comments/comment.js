import {builder, customComponents} from "../../../../../library/simplicity/simplicity.js";
import CommentEditDialog from "./comment-dialog.js";
import CommentLikes from "./comment-likes.js";

export default class Comment extends HTMLElement {

    #comment;

    get comment() {
        return this.#comment;
    }

    set comment(value) {
        this.#comment = value;
    }

    render() {

        builder(this, {
            element : "div",
            style : {
                margin : "2px"
            },
            children : [
                {
                    element : "div",
                    style : {
                        display : "flex"
                    },
                    children : [
                        {
                            element : "img",
                            src : this.#comment.form.owner.image.data,
                            style : {
                                height: "40px",
                                width: "40px",
                                borderRadius: "50%",
                                objectFit: "cover",
                                marginRight : "5px"
                            }
                        },
                        {
                            element : "div",
                            style : {
                                width : "100%",
                                padding : "5px"
                            },
                            children : [
                                {
                                    element : "div",
                                    style : {
                                        fontSize : "small",
                                        display : "flex"
                                    },
                                    children : [
                                        {
                                            element : "strong",
                                            text : `${this.#comment.form.owner.firstName} ${this.#comment.form.owner.lastName}`
                                        },
                                        {
                                            element : "div",
                                            style : {
                                                flex : "1"
                                            }
                                        },
                                        {
                                            element : "button",
                                            type : "button",
                                            className : "icon",
                                            text : "more_horiz",
                                            style : {
                                                backgroundColor : "var(--main-dimmed-color)"
                                            },
                                            onClick : () => {
                                                let dialog = new CommentEditDialog();
                                                dialog.comment = this.#comment;
                                                dialog.addEventListener("update", () => {
                                                    this.dispatchEvent(new CustomEvent("update"))
                                                })
                                                dialog.addEventListener("delete", () => {
                                                    this.dispatchEvent(new CustomEvent("delete"));
                                                })
                                                document.body.appendChild(dialog);
                                            }
                                        }
                                    ]
                                },
                                {
                                    element : "div",
                                    children : [
                                        {
                                            element : "div",
                                            text : this.#comment.form.text
                                        },
                                    ]
                                }, {
                                    element : CommentLikes,
                                    comment : this.#comment
                                }
                            ]
                        }
                    ]
                }
            ]
        })
    }

}

customComponents.define("post-comment", Comment)
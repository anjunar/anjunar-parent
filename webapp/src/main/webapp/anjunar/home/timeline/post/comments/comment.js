import {builder, customComponents} from "../../../../../library/simplicity/simplicity.js";
import CommentEditDialog from "./comment-dialog.js";
import {windowManager} from "../../../../../library/simplicity/services/window-manager.js";
import Likes from "../../../../shared/likes.js";
import {jsonClient} from "../../../../../library/simplicity/services/client.js";
import {hateoas} from "../../../../../library/simplicity/services/tools.js";

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
                            src : this.#comment.owner.image.data,
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
                                            text : `${this.#comment.owner.firstName} ${this.#comment.owner.lastName}`
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
                                                backgroundColor : "var(--main-dark2-color)",
                                                display : () => {
                                                    return hateoas(this.#comment.actions, "update") ? "block" : "none"
                                                }
                                            },
                                            onClick : (event) => {
                                                event.stopPropagation();
                                                let commentDialog = new CommentEditDialog();
                                                let url = `#/anjunar/home/timeline/post/comments/comment-dialog?id=${this.#comment.id}`;
                                                windowManager.openWindow(commentDialog, url, {id : this.#comment.id});
                                                return false;
                                            }
                                        }
                                    ]
                                },
                                {
                                    element : "div",
                                    children : [
                                        {
                                            element : "div",
                                            text : this.#comment.text
                                        },
                                    ]
                                }, {
                                    element : Likes,
                                    likeable : this.#comment,
                                    onLike : () => {
                                        let link = this.#comment.actions.find(link => link.rel === "update");
                                        jsonClient.put(link.url, {body : this.#comment})
                                    }
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
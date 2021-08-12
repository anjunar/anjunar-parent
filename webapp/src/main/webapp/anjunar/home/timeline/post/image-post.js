import {builder, customComponents} from "../../../../library/simplicity/simplicity.js";
import {jsonClient} from "../../../../library/simplicity/services/client.js";
import Comments from "./comments/comments.js";
import ImagePostDialog from "./image-post-dialog.js";
import {dateFormat, hateoas} from "../../../../library/simplicity/services/tools.js";
import {windowManager} from "../../../../library/simplicity/services/window-manager.js";
import Likes from "../../../shared/likes.js";

export default class ImagePost extends HTMLElement {

    #post;

    get post() {
        return this.#post;
    }

    set post(value) {
        this.#post = value;
    }

    render() {

        builder(this, {
            element: "div",
            style: {
                backgroundColor: "var(--main-dark2-color)",
                margin: "15px",
                padding: "15px",
                border: "1px solid var(--main-dark1-color)"
            },
            children : [
                {
                    element : "div",
                    style : {
                        display: "flex",
                        alignItems: "center"
                    },
                    children : [
                        {
                            element : "img",
                            src : this.#post.owner.image.data,
                            style : {
                                height: "50px",
                                width: "50px",
                                borderRadius: "50%",
                                objectFit: "cover"
                            },
                            onClick : () => {
                                window.location.hash = `#/anjunar/control/user?id=${this.#post.owner.id}`
                            }
                        },
                        {
                            element : "div",
                            style : {
                                marginLeft : "5px"
                            },
                            children : [
                                {
                                    element : "div",
                                    text : `${this.#post.owner.firstName} ${this.#post.owner.lastName}`
                                },
                                {
                                    element : "div",
                                    style : {
                                        fontSize : "small"
                                    },
                                    text : dateFormat(this.#post.created)
                                }
                            ]
                        },{
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
                                    return hateoas(this.#post.actions, "update") ? "block" : "none"
                                }
                            },
                            onClick : (event) => {
                                event.stopPropagation();
                                let postDialog = new ImagePostDialog();
                                let url = `/anjunar/home/timeline/post/image-post-dialog?id=${this.#post.id}`;
                                windowManager.openWindow(postDialog, url, {id : this.#post.id})
                                return false;
                            }
                        }
                    ]
                },
                {
                    element : "div",
                    style : {
                        padding : "5px"
                    },
                    text : this.#post.text
                },
                {
                    element : "img",
                    style : {
                        width : "500px",
                        margin : "auto"
                    },
                    src : this.#post.image.data
                },
                {
                    element : "hr",
                    style: {
                        color : "var(--main-dark1-color)",
                        backgroundColor : "var(--main-dark1-color)"
                    }
                },
                {
                    element : Likes,
                    likeable : this.#post,
                    onLike : () => {
                        jsonClient.put(`service/home/timeline/post?id=${this.#post.id}`, {body : this.#post})
                    }
                },
                {
                    element : "hr",
                    style: {
                        color : "var(--main-dark1-color)",
                        backgroundColor : "var(--main-dark1-color)"
                    }
                },{
                    element : Comments,
                    post: this.#post,
                    items: {
                        direct : (query, callback) => {
                            jsonClient.post(`service/home/timeline/post/comments?post=${this.#post.id}&index=${query.index}&limit=${query.limit}`)
                                .then((response) => {
                                    callback(response.rows, response.size, response.links)
                                })
                        }
                    }
                }
            ]
        })
    }

}

customComponents.define("image-post", ImagePost)
import {builder, customComponents} from "../../../../library/simplicity/simplicity.js";
import {jsonClient} from "../../../../library/simplicity/services/client.js";
import Comments from "./comments/comments.js";
import PostLikes from "./post-likes.js";
import PostCreateDialog from "./post-dialog.js";
import {dateFormat} from "../../../../library/simplicity/services/tools.js";

export default class Post extends HTMLElement {

    #post;

    get post() {
        return this.#post;
    }

    set post(value) {
        this.#post = value;
    }

    render() {

        let matTimeline = this.queryUpwards("home-timeline")

        builder(this, {
            element: "div",
            style: {
                backgroundColor: "var(--main-dimmed-color)",
                margin: "15px",
                padding: "15px",
                border: "1px solid var(--main-normal-color)"
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
                                backgroundColor : "var(--main-dimmed-color)"
                            },
                            onClick : () => {
                                let dialog = new PostCreateDialog();
                                dialog.post = this.#post
                                dialog.addEventListener("afterSubmit", () => {
                                    matTimeline.dispatchEvent(new CustomEvent("afterSubmit"));
                                })
                                document.body.appendChild(dialog);
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
                    if :() => {
                        return this.#post.image?.data;
                    },
                    style : {
                        width : "500px",
                        margin : "auto"
                    },
                    src : this.#post.image?.data
                },
                {
                    element : "hr",
                    style: {
                        color : "var(--main-normal-color)",
                        backgroundColor : "var(--main-normal-color)"
                    }
                },
                {
                    element : PostLikes,
                    post : this.#post
                },
                {
                    element : "hr",
                    style: {
                        color : "var(--main-normal-color)",
                        backgroundColor : "var(--main-normal-color)"
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

customComponents.define("timeline-post", Post)
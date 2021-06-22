import {builder, customViews} from "../../library/simplicity/simplicity.js";
import MatInfiniteScroll from "../../library/simplicity/components/table/mat-infinite-scroll.js";
import MatInputContainer from "../../library/simplicity/components/form/containers/mat-input-container.js";
import DomInput from "../../library/simplicity/directives/dom-input.js";
import PostDialog from "./timeline/post/post-dialog.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import Post from "./timeline/post/post.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";

export default class Timeline extends HTMLElement {

    #user;

    constructor() {
        super();

        this.addEventListener("afterSubmit",() => {
            let scroll = this.querySelector("mat-infinite-scroll");
            scroll.reload();
        });

    }

    get user() {
        return this.#user;
    }

    set user(value) {
        this.#user = value;
    }

    render() {
        builder(this, {
            element : "div",
            style : {
                width : "600px",
                margin : "auto"
            },
            children : [
                {
                    element : MatInputContainer,
                    placeholder : i18n("Post here..."),
                    content : {
                        element : DomInput,
                        type : "text",
                        onFocus : () => {
                            jsonClient.get("service/home/timeline/post/create")
                                .then((response) => {
                                    let dialog = new PostDialog();
                                    dialog.matTimeline = this;
                                    dialog.post = response;
                                    document.body.appendChild(dialog);
                                })
                        }
                    }
                },
                {
                    element : MatInfiniteScroll,
                    items : {
                        direct : (query, callback) => {
                            jsonClient.post(`service/home/timeline?user=${this.#user.id}&index=${query.index}&limit=${query.limit}&sort=created:desc`)
                                .then((response) => {
                                    callback(response.rows, response.size, response.links);
                                });

                        }
                    },
                    meta : {
                        part : {
                            element : (item) => {
                                return {
                                    element : Post,
                                    post : item
                                }
                            }
                        }
                    }
                }
            ]
        })
    }

}

customViews.define({
    name : "home-timeline",
    class : Timeline,
    guard() {
        return {
            user : jsonClient.get("service/control/users/user/current")
        }
    }
})

const i18n = i18nFactory({
    "Post here..." : {
        "en-DE" : "Post here...",
        "de-DE" : "Hier posten..."
    }
});

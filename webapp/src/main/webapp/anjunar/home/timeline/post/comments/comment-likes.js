import {builder, customComponents} from "../../../../../library/simplicity/simplicity.js";
import {jsonClient} from "../../../../../library/simplicity/services/client.js";
import {i18nFactory} from "../../../../../library/simplicity/services/i18nResolver.js";

export default class CommentLikes extends HTMLElement {

    #comment;

    get comment() {
        return this.#comment;
    }

    set comment(value) {
        this.#comment = value;
    }

    render() {
        let app = document.querySelector("app-anjunar");
        let user = app.service.user;

        builder(this, {
            element : "div",
            style : {
                display : "flex",
                alignItems : "center"
            },
            children : [
                {
                    element : "a",
                    style : {
                        fontSize : "small",
                        color : () => {
                            let like = this.#comment.likes.find((like) => like.id === user.id);
                            if (like) {
                                return "var(--main-selected-color)"
                            } else {
                                return "var(--main-grey-color)"
                            }
                        }
                    },
                    text : i18n("I like it"),
                    onClick : () => {
                        let like = this.#comment.likes.find((like) => like.id === user.id);

                        if (like) {
                            let indexOf = this.#comment.likes.indexOf(like);
                            this.#comment.likes.splice(indexOf);
                        } else {
                            this.#comment.likes.push(user);
                        }

                        let link = this.#comment.actions.find(link => link.rel === "update");
                        jsonClient.put(link.url, {body : this.#comment})
                    }
                },
                {
                    element : "div",
                    style : {
                        flex : "1"
                    }
                },
                {
                    element : "div",
                    style : {
                        fontSize : "small",
                        color : "var(--main-grey-color)"
                    },
                    text :() => {
                        return `${i18n("Likes")} ${this.#comment.likes.length}`
                    }
                }
            ]
        })
    }

}

customComponents.define("post-comment-likes", CommentLikes)

const i18n = i18nFactory({
    "Likes" : {
        "en-DE" : "Likes",
        "de-DE" : "Mag ich"
    },
    "I like it" : {
        "en-DE" : "I like it",
        "de-DE" : "Das mag ich"
    }
});

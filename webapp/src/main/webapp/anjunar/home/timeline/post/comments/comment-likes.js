import {builder, customComponents, i18nFactory} from "../../../../../library/simplicity/simplicity.js";
import {jsonClient} from "../../../../../library/simplicity/services/client.js";

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
                            let like = this.#comment.form.likes.find((like) => like.id === user.id);
                            if (like) {
                                return "var(--main-selected-color)"
                            } else {
                                return "var(--main-grey-color)"
                            }
                        }
                    },
                    text : i18n("I like it"),
                    onClick : () => {
                        let like = this.#comment.form.likes.find((like) => like.id === user.id);

                        if (like) {
                            let indexOf = this.#comment.form.likes.indexOf(like);
                            this.#comment.form.likes.splice(indexOf);
                        } else {
                            this.#comment.form.likes.push(user);
                        }

                        let link = this.#comment.form.actions.find(link => link.rel === "update");
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
                        return `${i18n("Likes")} ${this.#comment.form.likes.length}`
                    }
                }
            ]
        })
    }

}

customComponents.define("post-comment-likes", CommentLikes)

const i18n = i18nFactory({
    "Likes" : {
        en : "Likes",
        de : "Mag ich"
    },
    "I like it" : {
        en : "I like it",
        de : "Das mag ich"
    }
});

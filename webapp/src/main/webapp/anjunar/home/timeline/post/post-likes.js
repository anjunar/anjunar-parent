import {builder, customComponents} from "../../../../library/simplicity/simplicity.js";
import {jsonClient} from "../../../../library/simplicity/services/client.js";
import {i18nFactory} from "../../../../library/simplicity/services/i18nResolver.js";

export default class PostLikes extends HTMLElement {

    #post;

    get post() {
        return this.#post;
    }

    set post(value) {
        this.#post = value;
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
                    element : "button",
                    type : "button",
                    className : () => {
                        let like = this.#post.likes.find((like) => like.id === user.id);
                        if (like) {
                            return "active material-icons"
                        } else {
                            return "button material-icons"
                        }
                    },
                    text : "plus_one",
                    onClick :() => {
                        let like = this.#post.likes.find((like) => like.id === user.id);

                        if (like) {
                            let indexOf = this.#post.likes.indexOf(like);
                            this.#post.likes.splice(indexOf);
                        } else {
                            this.#post.likes.push(user);
                        }
                        jsonClient.put(`service/home/timeline/post?id=${this.#post.id}`, {body : this.#post})
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
                        fontSize : "small"
                    },
                    text : () => {
                        return `${i18n("Likes")} ${this.#post.likes.length}`
                    }
                }
            ]
        })
    }
}

customComponents.define("timeline-post-likes", PostLikes)

const i18n = i18nFactory({
    "Likes" : {
        "en-DE" : "Likes",
        "de-DE" : "Mag ich"
    }
});

import {builder, customComponents} from "../../library/simplicity/simplicity.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";

export default class Likes extends HTMLElement {

    #likeable;

    get likeable() {
        return this.#likeable;
    }

    set likeable(value) {
        this.#likeable = value;
    }

    render() {
        let app = document.querySelector("app-anjunar");
        let user = app.service;

        builder(this, {
            element: "div",
            style: {
                display: "flex",
                alignItems: "center"
            },
            children: [
                {
                    element: "button",
                    type: "button",
                    className: () => {
                        let like = this.#likeable.likes.find((like) => like.id === user.id);
                        if (like) {
                            return "active material-icons"
                        } else {
                            return "material-icons"
                        }
                    },
                    text: "plus_one",
                    onClick: (event) => {
                        event.stopPropagation();
                        let like = this.#likeable.likes.find((like) => like.id === user.id);

                        if (like) {
                            let indexOf = this.#likeable.likes.indexOf(like);
                            this.#likeable.likes.splice(indexOf);
                        } else {
                            this.#likeable.likes.push(user);
                        }
                        this.dispatchEvent(new CustomEvent("like"))
                        return false;
                    }
                },
                {
                    element: "div",
                    style: {
                        flex: "1"
                    }
                },
                {
                    element: "div",
                    style: {
                        fontSize: "12px"
                    },
                    text: () => {
                        return `${i18n("Likes")} ${this.#likeable.likes.length}`
                    }
                }
            ]
        })
    }
}

customComponents.define("timeline-post-likes", Likes)

const i18n = i18nFactory({
    "Likes": {
        "en-DE": "Likes",
        "de-DE": "Mag ich"
    }
});

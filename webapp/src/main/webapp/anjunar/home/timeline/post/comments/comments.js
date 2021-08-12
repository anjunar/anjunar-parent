import {builder, customComponents} from "../../../../../library/simplicity/simplicity.js";
import Comment from "./comment.js";
import {jsonClient} from "../../../../../library/simplicity/services/client.js";
import {i18nFactory} from "../../../../../library/simplicity/services/i18nResolver.js";

export default class Comments extends HTMLElement {

    #post;

    #comment = {text : ""};
    #items;

    #index = 0;
    #limit = 20;

    #size;
    #data;
    #links;


    get post() {
        return this.#post;
    }

    set post(value) {
        this.#post = value;
    }

    get items() {
        return this.#items;
    }

    set items(value) {
        this.#items = value;
    }

    load() {
        this.#items({index: this.#index, limit: this.#limit}, (_items, _size, _links) => {
            this.#size = _size;
            this.#data = _items;
            this.#links = _links;
        });
    }

    render() {

        this.load();

        this.style.display = "block";
        this.style.marginTop = "8px";

        builder(this, [
            {
                element : "div",
                children : {
                    items : () => {
                        return this.#data;
                    },
                    item : (comment) => {
                        return {
                            element : Comment,
                            comment : comment
                        }
                    }
                }
            },
            {
                element : "div",
                style : {
                    display : "flex",
                    alignItems : "center"
                },
                children : [
                    {
                        element : "img",
                        src : this.#post.owner.image.data,
                        style : {
                            height: "40px",
                            width: "40px",
                            borderRadius: "50%",
                            objectFit: "cover",
                            marginRight : "5px"
                        },
                        onClick : () => {
                            window.location.hash = `#/anjunar/control/user?id=${this.#post.owner.id}`
                        }
                    },
                    {
                        element : "textarea",
                        rows : "1",
                        placeholder : i18n("Comment here..."),
                        style: {
                            backgroundColor : "var(--main-dark2-color)"
                        },
                        value : {
                            input : () => {
                                return this.#comment.text;
                            },
                            output : (value) => {
                                this.#comment.text = value;
                            },
                        },
                        onFocus : () => {
                            let link = this.#links.find((link) => link.rel === "create");
                            jsonClient.get(link.url)
                                .then((response) => {
                                    this.#comment = response;
                                })
                        },
                        onKeyup : (event) => {
                            if (event.key === "Enter") {
                                this.#comment.post = this.#post.id;
                                jsonClient.post(`service/home/timeline/post/comments/comment`, {body : this.#comment})
                                    .then((response) => {
                                        this.load();
                                        this.#comment.text = "";
                                    })
                            }
                        }
                    }
                ]
            }
        ])
    }

}

customComponents.define("post-comments", Comments);

const i18n = i18nFactory({
    "Comment here..." : {
        "en-DE" : "Comment here...",
        "de-DE" : "Hier kommentieren..."
    }
});

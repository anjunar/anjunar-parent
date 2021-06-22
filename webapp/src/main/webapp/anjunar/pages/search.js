import {builder, customViews} from "../../library/simplicity/simplicity.js";
import MatInputContainer from "../../library/simplicity/components/form/containers/mat-input-container.js";
import DomInput from "../../library/simplicity/directives/dom-input.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import HateoasForm from "../../library/simplicity/hateoas/hateoas-form.js";
import {getLanguage, i18nFactory} from "../../library/simplicity/services/i18nResolver.js";

export default class Search extends HTMLElement {

    #search;
    #results = [];

    get search() {
        return this.#search;
    }

    set search(value) {
        this.#search = value;
    }

    render() {
        builder(this, {
            element : "div",
            style: {
                display: "block",
                position: "absolute",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)"
            },
            children : [
                {
                    element : "div",
                    style : {
                        marginBottom : "20px",
                        marginLeft : "-80px"
                    },
                    children : [
                        {
                            element : "h3",
                            text : i18n("Search")
                        }, {
                            element: "hr"
                        }
                    ]
                },
                {
                    element: HateoasForm,
                    model : this.#search,
                    children: [
                        {
                            element: MatInputContainer,
                            placeholder : i18n("Title"),
                            content : {
                                element : DomInput,
                                name : "title",
                                type : "text"
                            }
                        },
                        {
                            element: "button",
                            className : "button",
                            type : "button",
                            text : i18n("Like"),
                            onClick : () => {
                                jsonClient.post(`service/pages/like?title=${this.#search.title}&index=0&limit=20&lang=${getLanguage()}`)
                                    .then((response) => {
                                        this.#results = response.rows;
                                    })
                            }
                        },
                        {
                            element: "button",
                            className : "button",
                            type : "button",
                            text : i18n("Word"),
                            onClick : () => {
                                jsonClient.post(`service/pages/word?title=${this.#search.title}&index=0&limit=20&lang=${getLanguage()}`)
                                    .then((response) => {
                                        this.#results = response.rows;
                                    })
                            }
                        }
                    ]
                },
                {
                    element: "div",
                    style: {
                        margin : "12px"
                    },
                    children: {
                        items : () => {
                            return this.#results;
                        },
                        item : (result) => {
                            return {
                                element: "div",
                                children : [
                                    {
                                        element: "a",
                                        text : result.title,
                                        href : () => {
                                            return `#/anjunar/pages/page?id=${result.id}#/anjunar/pages/text`
                                        }
                                    }
                                ]
                            }
                        }
                    }
                }
            ]
        })
    }

}

customViews.define({
    name : "pages-search",
    class : Search,
    guard(activeRoute) {
        return {
            search : jsonClient.get("service/pages/search")
        }
    }
})

const i18n = i18nFactory({
    "Search" : {
        "en-DE" : "Search",
        "de-DE" : "Suche"
    },
    Like : {
        "en-DE" : "Like",
        "de-DE" : "Ähnlich"
    },
    Title : {
        "en-DE" : "Title",
        "de-DE" : "Titel"
    },
    Word : {
        "en-DE" : "Word",
        "de-DE" : "Wort"
    }
});

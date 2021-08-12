import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import MatInputContainer from "../../library/simplicity/components/form/containers/mat-input-container.js";
import DomInput from "../../library/simplicity/directives/dom-input.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";
import HateoasTable from "../../library/simplicity/hateoas/hateoas-table.js";

export default class Search extends HTMLWindow {

    #search;

    get search() {
        return this.#search;
    }

    set search(value) {
        this.#search = value;
    }

    render() {
        builder(this, {
            element : "div",
            children : [
                {
                    element: MatInputContainer,
                    placeholder : i18n("Title"),
                    content : {
                        element : DomInput,
                        name : "title",
                        type : "text",
                        onKeyup : (event) => {
                            let table = this.querySelector("table");
                            table.search({property : "title", value : event.target.value});
                        }
                    }
                },
                {
                    element: HateoasTable,
                    model : this.#search,
                    className : "hoverTable",
                    header : false,
                    configuration : false,
                    onRow : (event) => {
                        window.location.hash = `#/anjunar/pages/page?id=${event.detail.id}`;
                        window.location.hash = `#/anjunar/pages/page/questions?page=${event.detail.id}`;
                    },
                    onCreate : () => {
                        window.location.hash = "#/anjunar/pages/editor"
                    },
                    meta : {
                        body : [
                            {
                                element: (page) => {
                                    return {
                                        element: "div",
                                        children : [
                                            {
                                                element: "h3",
                                                text : page.title
                                            },
                                            {
                                                element: "p",
                                                text : page.text,
                                                style : {
                                                    overflow : "clip",
                                                    height : "48px"
                                                }
                                            }
                                        ]
                                    }
                                }
                            }
                        ]
                    }
                }
            ]
        })
    }

}

const i18n = i18nFactory({
    "Search" : {
        "en-DE" : "Search",
        "de-DE" : "Suche"
    },
    "Like" : {
        "en-DE" : "Like",
        "de-DE" : "Ähnlich"
    },
    "Title" : {
        "en-DE" : "Title",
        "de-DE" : "Titel"
    },
    "Word" : {
        "en-DE" : "Word",
        "de-DE" : "Wort"
    }
});

customViews.define({
    name : "pages-search",
    class : Search,
    header : i18n("Search"),
    resizable : true,
    width : 800,
    height : 600,
    guard(activeRoute) {
        return {
            search : jsonClient.get(`service/pages?lang=${user.language}`)
        }
    }
})
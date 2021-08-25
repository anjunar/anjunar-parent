import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import MatInputContainer from "../../library/simplicity/components/form/containers/mat-input-container.js";
import DomInput from "../../library/simplicity/directives/dom-input.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";
import HateoasTable from "../../library/simplicity/hateoas/hateoas-table.js";
import MatTabs from "../../library/simplicity/components/navigation/mat-tabs.js";
import MatTab from "../../library/simplicity/components/navigation/mat-tab.js";
import MatPages from "../../library/simplicity/components/navigation/mat-pages.js";
import MatPage from "../../library/simplicity/components/navigation/mat-page.js";
import MatList from "../../library/simplicity/components/table/mat-list.js";
import {hateoas} from "../../library/simplicity/services/tools.js";

export default class Search extends HTMLWindow {

    #pages;
    #questions;

    #page = 0;

    get pages() {
        return this.#pages;
    }

    set pages(value) {
        this.#pages = value;
    }

    get questions() {
        return this.#questions;
    }

    set questions(value) {
        this.#questions = value;
    }

    render() {
        builder(this, {
            element : "div",
            children : [
                {
                    element: MatTabs,
                    items : {
                        direct : () => {
                            return ["Pages", "Questions"]
                        }
                    },
                    onPage: (event) => {
                        let index = event.detail.page;
                        this.#page = index;
                    },
                    meta : {
                        element : (tab) => {
                            return {
                                element : MatTab,
                                content : {
                                    element : "div",
                                    text : tab
                                }
                            }
                        }
                    }
                },
                {
                    element: MatPages,
                    page: {
                        input: () => {
                            return this.#page;
                        }
                    },
                    contents : [
                        {
                            element : MatPage,
                            contents : [
                                {
                                    element: MatInputContainer,
                                    placeholder : i18n("Title"),
                                    content : {
                                        element : DomInput,
                                        name : "title",
                                        type : "text",
                                        onKeyup : (event) => {
                                            let table = this.querySelector("mat-list");
                                            table.search([{property : "title", value : event.target.value}]);
                                        }
                                    }
                                },
                                {
                                    element: MatList,
                                    items : {
                                        direct : (query, callback) => {
                                            let link = hateoas(this.#pages.sources, "list")

                                            link.body = link.body || {};

                                            if (query.search) {
                                                for (const search of query.search) {
                                                    link.body[search.property] = search.value
                                                }
                                            }

                                            link.body.sort = query.sort;
                                            link.body.index = query.index;
                                            link.body.limit = query.limit;

                                            jsonClient.post(link.url, {body: link.body})
                                                .then(response => {
                                                    callback(response.rows, response.size, response.links)
                                                })
                                        }
                                    },
                                    onItem : (event) => {
                                        window.location.hash = `#/anjunar/pages/page?id=${event.detail.id}`;
                                        window.location.hash = `#/anjunar/pages/page/questions?page=${event.detail.id}`;
                                    },
                                    onCreate: (event) => {
                                        window.location.hash = "#/anjunar/pages/editor"
                                    },
                                    meta : {
                                        element : (page) => {
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
                                }
                            ]
                        },
                        {
                            element : MatPage,
                            contents : [
                                {
                                    element: MatInputContainer,
                                    placeholder : "Word",
                                    content : {
                                        element : DomInput,
                                        name : "title",
                                        type : "text",
                                        onKeyup : (event) => {
                                            let table = this.querySelector("mat-list");
                                            let value = event.target.value;
                                            table.search([ {property : "topic", value : value}]);
                                        }
                                    }
                                },
                                {
                                    element: MatList,
                                    items : {
                                        direct : (query, callback) => {
                                            let link = hateoas(this.#questions.sources, "list")

                                            link.body = link.body || {};

                                            if (query.search) {
                                                for (const search of query.search) {
                                                    link.body[search.property] = search.value
                                                }
                                            }

                                            link.body.sort = query.sort;
                                            link.body.index = query.index;
                                            link.body.limit = query.limit;

                                            jsonClient.post(link.url, {body: link.body})
                                                .then(response => {
                                                    callback(response.rows, response.size, response.links)
                                                })
                                        }
                                    },
                                    onItem : (event) => {
                                        window.location.hash = `#/anjunar/pages/page/question/replies?id=${event.detail.id}`
                                    },
                                    meta : {
                                        element : (page) => {
                                            return {
                                                element: "div",
                                                children : [
                                                    {
                                                        element: "h3",
                                                        text : page.topic
                                                    },
                                                    {
                                                        element: "p",
                                                        text : page.editor.text,
                                                        style : {
                                                            overflow : "clip",
                                                            height : "48px"
                                                        }
                                                    }
                                                ]
                                            }
                                        }
                                    }
                                }
                            ]
                        }
                    ]
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
            pages : jsonClient.get(`service/pages?lang=${user.language}`),
            questions : jsonClient.get(`service/pages/page/topics`),
        }
    }
})
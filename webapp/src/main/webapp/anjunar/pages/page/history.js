import {builder, customViews, HTMLWindow} from "../../../library/simplicity/simplicity.js";
import {jsonClient} from "../../../library/simplicity/services/client.js";
import {dateFormat} from "../../../library/simplicity/services/tools.js";
import HateoasTable from "../../../library/simplicity/hateoas/hateoas-table.js";

export default class History extends HTMLWindow {

    #history;

    get history() {
        return this.#history;
    }

    set history(value) {
        this.#history = value;
    }

    render() {

        builder(this, {
            element: HateoasTable,
            header : true,
            model : this.#history,
            onRow : (event) => {
                let hash = `#/anjunar/pages/page?id=${event.detail.id}&revision=${event.detail.revision}#/anjunar/pages/text`;
                window.location.hash = hash
            },
            meta: {
                body: [
                    {
                        element : (page) => {
                            return {
                                element: "div",
                                text: page.revision
                            }
                        }
                    },
                    {
                        element : (page) => {
                            return {
                                element: "div",
                                text: dateFormat(page.modified)
                            }
                        }
                    },
                    {
                        element : (page) => {
                            return {
                                element: "div",
                                text: `${page.modifier.firstName} ${page.modifier.lastName}`
                            }
                        }
                    }
                ]
            }
        })
    }
}

customViews.define({
    name : "page-history",
    class : History,
    width : 800,
    height : 600,
    resizable : true,
    guard(activeRoute) {
        return {
            history : jsonClient.get(`service/pages/page/history?id=${activeRoute.queryParams.id}`)
        }
    }
})
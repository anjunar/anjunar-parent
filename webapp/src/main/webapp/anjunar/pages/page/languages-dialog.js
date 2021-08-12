import {builder, customComponents, customViews, HTMLWindow} from "../../../library/simplicity/simplicity.js";
import MatDialog from "../../../library/simplicity/components/modal/mat-dialog.js";
import {jsonClient} from "../../../library/simplicity/services/client.js";
import MatMultiSelect from "../../../library/simplicity/components/form/mat-multi-select.js";
import {i18nFactory} from "../../../library/simplicity/services/i18nResolver.js";

export default class LanguagesDialog extends HTMLWindow {

    #page;

    get page() {
        return this.#page
    }

    set page(value) {
        this.#page = value
    }

    render() {
        builder(this, {
            element: MatMultiSelect,
            label: "Links",
            value: {
                input: () => {
                    return this.#page.pageLinks
                },
                output: (value) => {
                    this.#page.pageLinks = value
                }
            },
            placeholder: "Page",
            items: {
                direct: (query, callback) => {
                    jsonClient.post(`service/pages?index=${query.index}&limit=${query.limit}`)
                        .then((response) => {
                            callback(response.rows, response.size)
                        })
                }
            },
            meta: {
                option: {
                    element: (page) => {
                        return {
                            element: "div",
                            text: page.title
                        }
                    }
                },
                selection : {
                    element: (page) => {
                        return {
                            element: "div",
                            text: page.title
                        }
                    }
                }
            }
        })
    }

}

customViews.define({
    name : "page-languages",
    class : LanguagesDialog,
    header : "Languages"
});

const i18n = i18nFactory({
    "Languages": {
        en: "Languages",
        de: "Sprachen"
    }
})
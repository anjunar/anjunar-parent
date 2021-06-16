import {builder, customComponents, i18nFactory} from "../../../library/simplicity/simplicity.js";
import MatDialog from "../../../library/simplicity/components/modal/mat-dialog.js";
import {jsonClient} from "../../../library/simplicity/services/client.js";
import MatMultiSelect from "../../../library/simplicity/components/form/mat-multi-select.js";

export default class LanguagesDialog extends HTMLElement {

    #page;

    get page() {
        return this.#page
    }

    set page(value) {
        this.#page = value
    }

    render() {
        builder(this, {
            element: "div",
            children: [
                {
                    element: MatDialog,
                    enclosing: this,
                    header: i18n("Languages"),
                    content: {
                        element: MatMultiSelect,
                        label: "Links",
                        value: {
                            input: () => {
                                return this.#page.form.pageLinks
                            },
                            output: (value) => {
                                this.#page.form.pageLinks = value
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
                    }
                }
            ]
        })
    }

}

customComponents.define("page-languages", LanguagesDialog);

const i18n = i18nFactory({
    "Languages": {
        en: "Languages",
        de: "Sprachen"
    }
})
import {builder, customViews, i18nFactory} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import MatEditor from "../../library/simplicity/components/form/mat-editor.js";
import LinkDialog from "./editor/link-dialog.js";
import MatInputHolder from "../../library/simplicity/hateoas/hateoas-input.js";
import HateoasButton from "../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../library/simplicity/hateoas/hateoas-form.js";
import LanguagesDialog from "./page/languages-dialog.js";
import DomInput from "../../library/simplicity/directives/dom-input.js";
import {getCookie} from "../../library/simplicity/services/tools.js";

export default class Editor extends HTMLElement {

    #html

    constructor() {
        super();

        document.addEventListener("language", (event) => {
            for (const pageLink of this.#html.pageLinks) {
                if (pageLink.language === event.detail) {
                    window.location.hash = `#/anjunar/pages/editor?id=${pageLink.id}`;
                    break;
                }
            }
        })
    }

    get html() {
        return this.#html;
    }

    set html(value) {
        this.#html = value;
    }

    render() {
        builder(this, {
            element : HateoasForm,
            model : this.#html,
            style : {
                margin : "auto",
                width : "800px"
            },
            children : [
                {
                    element: MatInputHolder,
                    name: "title"
                },
                {
                    element : MatEditor,
                    style : {
                        height : "calc(100vh - 160px)"
                    },
                    name : "content",
                    linkDialog : {
                        direct : () => {
                            let linkDialog = new LinkDialog();
                            linkDialog.page = this.#html;
                            return linkDialog;
                        }
                    }
                },
                {
                    element: DomInput,
                    type : "hidden",
                    name : "language",
                    value : {
                        input : () => {
                            return getCookie("language")
                        }
                    }
                },
                {
                    element : "div",
                    style : {
                        display : "flex"
                    },
                    children : [
                        {
                            element: HateoasButton,
                            hateoas : "save",
                            text : i18n("Save"),
                            onAfterSubmit : (event) => {
                                document.location.hash = `#/anjunar/pages/page?id=${event.detail.id}#/anjunar/pages/text`;
                            }
                        },
                        {
                            element: HateoasButton,
                            hateoas : "update",
                            text : i18n("Update"),
                            onAfterSubmit : (event) => {
                                document.location.hash = `#/anjunar/pages/page?id=${event.detail.id}#/anjunar/pages/text`;
                            }
                        },
                        {
                            element: HateoasButton,
                            hateoas : "delete",
                            text : i18n("Delete"),
                            onAfterSubmit : () => {
                                document.location.hash = `#/anjunar/pages/editor`;
                            }
                        },
                        {
                            element: "button",
                            type: "button",
                            className: "button",
                            text: i18n("Languages"),
                            onClick : () => {
                                let dialog = new LanguagesDialog();
                                dialog.page = this.#html;
                                document.body.appendChild(dialog)
                            }
                        }
                    ]
                }
            ]
        })
    }
}

customViews.define({
    name : "pages-editor",
    class : Editor,
    guard(activeRoute) {
        if (activeRoute.queryParams?.id) {
            let url = `service/pages/page?id=${activeRoute.queryParams.id}`;
            if (activeRoute.queryParams.revision) {
                url += `&revision=${activeRoute.queryParams.revision}`
            }
            return {
                html: jsonClient.get(url)
            }
        } else {
            return {
                html: jsonClient.get("service/pages/page/create")
            }
        }
    }
})

const i18n = i18nFactory({
    Save : {
        en : "Save",
        de : "Speichern"
    },
    Update : {
        en : "Update",
        de : "Aktualisieren"
    },
    Delete : {
        en : "Delete",
        de : "Löschen"
    },
    "Languages" : {
        en : "Languages",
        de : "Sprachen"
    }
});

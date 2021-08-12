import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import MatEditor from "../../library/simplicity/components/form/mat-editor.js";
import LinkDialog from "./editor/link-dialog.js";
import MatInputHolder from "../../library/simplicity/hateoas/hateoas-input.js";
import HateoasButton from "../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../library/simplicity/hateoas/hateoas-form.js";
import LanguagesDialog from "./page/languages-dialog.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";
import {windowManager} from "../../library/simplicity/services/window-manager.js";

export default class Editor extends HTMLWindow {

    #html

    constructor() {
        super();

        document.addEventListener("language", (event) => {
            this.#html.language = {locale : user.language}
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
        this.#html.language = {locale : user.language}
    }

    render() {
        builder(this, {
            element : HateoasForm,
            style : {
                height: "calc(100% - 80px)"
            },
            model : this.#html,
            children : [
                {
                    element: MatInputHolder,
                    name: "title",
                    placeholder : i18n("Title")
                },
                {
                    element : MatEditor,
                    style : {
                        height : "100%"
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
                                document.reloadAll();
                                document.location.hash = `#/anjunar/pages/page?id=${event.detail.id}#/anjunar/pages/text`;
                                windowManager.close(this.window);
                            }
                        },
                        {
                            element: HateoasButton,
                            hateoas : "update",
                            text : i18n("Update"),
                            onAfterSubmit : (event) => {
                                document.reloadAll();
                                document.location.hash = `#/anjunar/pages/page?id=${event.detail.id}#/anjunar/pages/text`;
                                windowManager.close(this.window);
                            }
                        },
                        {
                            element: HateoasButton,
                            hateoas : "delete",
                            text : i18n("Delete"),
                            onAfterSubmit : () => {
                                document.reloadAll();
                                document.location.hash = `#/anjunar/pages/editor`;
                                windowManager.close(this.window);
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
                                windowManager.openWindow(dialog, "/anjunar/pages/page/languages-dialog")
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
    header : "Editor",
    resizable : true,
    width : 800,
    height: 400,
    minWidth : 800,
    minHeight : 300,
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
    "Title" : {
        "en-DE" : "Title",
        "de-DE" : "Titel"
    },
    "Save" : {
        "en-DE" : "Save",
        "de-DE" : "Speichern"
    },
    "Update" : {
        "en-DE" : "Update",
        "de-DE" : "Aktualisieren"
    },
    "Delete" : {
        "en-DE" : "Delete",
        "de-DE" : "Löschen"
    },
    "Languages" : {
        "en-DE" : "Languages",
        "de-DE" : "Sprachen"
    }
});

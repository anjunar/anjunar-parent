import {builder, customViews, div, h3, hr, i18nFactory} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import {hateoasInput} from "../../library/simplicity/hateoas/hateoas-input.js";
import {hateoasButton} from "../../library/simplicity/hateoas/hateoas-button.js";
import {loadRoot} from "../service.js";
import {hateoasForm} from "../../library/simplicity/hateoas/hateoas-form.js";

export default class User extends HTMLElement {

    #user;

    get user() {
        return this.#user;
    }

    set user(value) {
        this.#user = value;
    }

    render() {
        let initialize = (element) => {
            element.addValidator({
                validate: (element) => {
                    let executor = (resolve, reject) => {
                        let link = this.#user.sources.find((link) => link.rel === "validate");
                        jsonClient.post(link.url, {body: element.value})
                            .then(() => {
                                resolve("naturalId");
                            })
                            .catch(() => {
                                reject("naturalId");
                            })
                    };
                    return new Promise(executor);
                }
            })
        };

        let postInitialize = (element) => {
            let form = element.querySelector("form");
            for (const elem of form.elements) {
                let matInputContainer = elem.queryUpwards("mat-input-container");
                if (matInputContainer) {
                    let container = builder(null, {
                        element: "div",
                        attributes: {
                            name: "naturalId"
                        },
                        text: "Already there"
                    });
                    matInputContainer.addError(container.firstElementChild)
                }
            }
        };

        let onAfterSubmit = () => {
            loadRoot(true);
            document.location.hash = "#/anjunar/control/users";
        };
        let formIf = () => {
            let form = this.querySelector("form");
            return form.dirty && form.valid
        };
        let resetIf = () => {
            let form = this.querySelector("form");
            return form.dirty;
        };
        let resetClick = () => {
            let form = this.querySelector("form");
            form.reset();
        };

        builder(this, hateoasForm({model: this.#user, initialize: initialize, postInitialize: postInitialize})(
            [
                div({ style: { display: "block", position: "absolute", top: "50%", left: "50%", transform: "translate(-50%, -50%)"}})(
                    [
                        div({style: {display: "flex"}})(
                            [
                                div({style: {marginLeft: "20px"}})(
                                    [
                                        div({style: {marginBottom: "20px"}})(
                                            [
                                                h3({text: i18n("User")}),
                                                hr()
                                            ]
                                        ),
                                        div({style: {marginLeft: "80px"}})(
                                            [
                                                hateoasInput({name: "firstName"}),
                                                hateoasInput({name: "lastName"}),
                                                hateoasInput({name: "birthdate"}),
                                                hateoasInput({name: "password"}),
                                                hateoasInput({name: "enabled"})
                                            ]
                                        )
                                    ]
                                ),
                                div({style: { marginLeft: "20px"}})(
                                    [
                                        div({ style: { marginBottom: "20px"}})(
                                            [
                                                h3({text: i18n("Image")}),
                                                hr()
                                            ]
                                        ),
                                        div({style: { marginLeft: "80px", minWidth: "200px", maxWidth: "300px", minHeight: "200px", maxHeight: "300px"}})(
                                            [
                                                hateoasInput({name: "picture"})
                                            ]
                                        ),
                                    ]
                                ),
                                div({style: { marginLeft: "20px"}})(
                                    [
                                        div({style: { marginBottom: "20px"}})(
                                            [
                                                h3({text: i18n("Roles")}),
                                                hr()
                                            ]
                                        ),
                                        div({style: { marginLeft: "80px"}})(
                                            [
                                                hateoasInput({name: "roles"})
                                            ]
                                        )
                                    ]
                                ),
                            ]),
                        div({style: { display: "flex"}})(
                            [
                                hateoasButton({hateoas: "runas", text: i18n("Run As"), onAfterSubmit: onAfterSubmit})(),
                                hateoasButton({hateoas: "delete", text: i18n("Delete"), onAfterSubmit: onAfterSubmit})(),
                                div({style: { display: "flex" }, if: formIf,})(
                                    [
                                        hateoasButton({ hateoas: "save", text: i18n("Save"), onAfterSubmit: onAfterSubmit})(),
                                        hateoasButton({ hateoas: "update", text: i18n("Update"), onAfterSubmit: onAfterSubmit})(),
                                        hateoasButton({ hateoas: "Reset", text: i18n("Reset"), onAfterSubmit: onAfterSubmit})()
                                    ]
                                )
                            ]
                        )
                    ]
                )
            ]
        ))
    }

}

customViews.define({
    name: "control-user",
    class: User,
    guard(activeRoute) {
        if (activeRoute.queryParams.id) {
            return {
                user: jsonClient.get(`service/control/users/user?id=${activeRoute.queryParams.id}`)
            }
        } else {
            return {
                user: jsonClient.get("service/control/users/user/create")
            }
        }
    }
})

const i18n = i18nFactory({
    Save: {
        en: "Save",
        de: "Speichern"
    },
    Update: {
        en: "Update",
        de: "Aktualisieren"
    },
    Reset: {
        en: "Reset",
        de: "Zurücksetzen"
    },
    Delete: {
        en: "Delete",
        de: "Löschen"
    },
    "Run As": {
        en: "Run As",
        de: "Ausführen"
    },
    User: {
        en: "User",
        de: "Benutzer"
    },
    Image: {
        en: "Image",
        de: "Bild"
    },
    Roles: {
        en: "Roles",
        de: "Rollen"
    }
})

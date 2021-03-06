import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import MatInputHolder from "../../library/simplicity/hateoas/hateoas-input.js";
import HateoasButton from "../../library/simplicity/hateoas/hateoas-button.js";
import {loadRoot} from "../service.js";
import HateoasForm from "../../library/simplicity/hateoas/hateoas-form.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";
import {windowManager} from "../../library/simplicity/services/window-manager.js";

export default class User extends HTMLWindow {

    #user;

    get user() {
        return this.#user;
    }

    set user(value) {
        this.#user = value;
    }

    get description() {
        return this.#user.firstName + " " + this.#user.lastName;
    }

    render() {
        builder(this, {
            element: HateoasForm,
            model: this.#user,
            initialize: (element) => {
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
            },
            postInitialize: (element) => {
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
            },
            children: [
                {
                    element: "div",
                    style: {
                        display: "flex"
                    },
                    children: [
                        {
                            element: "div",
                            style: {
                                marginLeft: "20px"
                            },
                            children: [
                                {
                                    element: "div",
                                    style: {
                                        marginBottom: "20px"
                                    },
                                    children: [
                                        {
                                            element: "h3",
                                            text: i18n("User")
                                        },
                                        {
                                            element: "hr"
                                        }
                                    ]
                                },
                                {
                                    element: "div",
                                    style: {
                                        marginLeft: "80px"
                                    },
                                    children: [
                                        {
                                            element: MatInputHolder,
                                            name: "firstName"
                                        },
                                        {
                                            element: MatInputHolder,
                                            name: "lastName"
                                        },
                                        {
                                            element: MatInputHolder,
                                            name: "birthdate"
                                        },
                                        {
                                            element: MatInputHolder,
                                            name: "password"
                                        },
                                        {
                                            element: MatInputHolder,
                                            name: "enabled"
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            element: "div",
                            style: {
                                marginLeft: "20px"
                            },
                            children: [
                                {
                                    element: "div",
                                    style: {
                                        marginBottom: "20px"
                                    },
                                    children: [
                                        {
                                            element: "h3",
                                            text: i18n("Image")
                                        },
                                        {
                                            element: "hr"
                                        }
                                    ]
                                }, {
                                    element: "div",
                                    style: {
                                        marginLeft: "80px",
                                        minWidth: "200px",
                                        maxWidth: "300px",
                                        minHeight: "200px",
                                        maxHeight: "300px"
                                    },
                                    children: [
                                        {
                                            element: MatInputHolder,
                                            name: "picture"
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            element: "div",
                            style: {
                                marginLeft: "20px"
                            },
                            children: [
                                {
                                    element: "div",
                                    style: {
                                        marginBottom: "20px"
                                    },
                                    children: [
                                        {
                                            element: "h3",
                                            text: i18n("Roles")
                                        },
                                        {
                                            element: "hr"
                                        }
                                    ]
                                },
                                {
                                    element: "div",
                                    style: {
                                        marginLeft: "80px"
                                    },
                                    children: [
                                        {
                                            element: MatInputHolder,
                                            name: "roles"
                                        }
                                    ]
                                }
                            ]
                        },
                    ]
                },
                {
                    element: "div",
                    style: {
                        display: "flex"
                    },
                    children: [
                        {
                            element: HateoasButton,
                            hateoas: "runas",
                            text: i18n("Run As"),
                            onAfterSubmit : () => {
                                loadRoot(true);
                                document.location.hash = "#/anjunar/control/users";
                                document.reloadAll();
                                windowManager.close(this.window);
                            }
                        },
                        {
                            element: HateoasButton,
                            hateoas: "delete",
                            text: i18n("Delete"),
                            onAfterSubmit : () => {
                                document.location.hash = "#/anjunar/control/users";
                                document.reloadAll();
                                windowManager.close(this.window);
                            }
                        },
                        {
                            element: "div",
                            style: {
                                display: "flex"
                            },
                            if : () => {
                                let form = this.querySelector("form");
                                return form.dirty && form.valid
                            },
                            children: [
                                {
                                    element: HateoasButton,
                                    hateoas: "save",
                                    text: i18n("Save"),
                                    onAfterSubmit : () => {
                                        document.location.hash = "#/anjunar/control/users";
                                        document.reloadAll();
                                        windowManager.close(this.window);
                                    }
                                },
                                {
                                    element: HateoasButton,
                                    hateoas: "update",
                                    text: i18n("Update"),
                                    onAfterSubmit : () => {
                                        document.location.hash = "#/anjunar/control/users";
                                        document.reloadAll();
                                        windowManager.close(this.window);
                                    }
                                },
                                {
                                    element: "button",
                                    className: "button",
                                    type: "button",
                                    text: i18n("Reset"),
                                    if : () => {
                                        let form = this.querySelector("form");
                                        return form.dirty;
                                    },
                                    onClick : () => {
                                        let form = this.querySelector("form");
                                        form.reset();
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
    Save : {
        "en-DE" : "Save",
        "de-DE" : "Speichern"
    },
    Update : {
        "en-DE" : "Update",
        "de-DE" : "Aktualisieren"
    },
    Reset : {
        "en-DE" : "Reset",
        "de-DE" : "Zur??cksetzen"
    },
    Delete : {
        "en-DE" : "Delete",
        "de-DE" : "L??schen"
    },
    "Run As" : {
        "en-DE" : "Run As",
        "de-DE" : "Ausf??hren"
    },
    User : {
        "en-DE" : "User",
        "de-DE" : "Benutzer"
    },
    Image : {
        "en-DE" : "Image",
        "de-DE" : "Bild"
    },
    Roles : {
        "en-DE" : "Roles",
        "de-DE" : "Rollen"
    }
})

customViews.define({
    name: "control-user",
    class: User,
    header : i18n("User"),
    resizable : false,
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


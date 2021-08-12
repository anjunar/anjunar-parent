import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import MatInputHolder from "../../library/simplicity/hateoas/hateoas-input.js";
import HateoasButton from "../../library/simplicity/hateoas/hateoas-button.js";
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
                                            name: "firstName",
                                            placeholder : i18n("First Name"),
                                            errors : [
                                                {
                                                    element: "div",
                                                    attributes: {
                                                        name: "naturalId"
                                                    },
                                                    text: i18n("Already there")
                                                }
                                            ]
                                        },
                                        {
                                            element: MatInputHolder,
                                            name: "lastName",
                                            placeholder : i18n("Last Name"),
                                            errors : [
                                                {
                                                    element: "div",
                                                    attributes: {
                                                        name: "naturalId"
                                                    },
                                                    text: i18n("Already there")
                                                }
                                            ]
                                        },
                                        {
                                            element: MatInputHolder,
                                            name: "birthDate",
                                            placeholder : i18n("Birthdate"),
                                            errors : [
                                                {
                                                    element: "div",
                                                    attributes: {
                                                        name: "naturalId"
                                                    },
                                                    text: i18n("Already there")
                                                }
                                            ]
                                        },
                                        {
                                            element: MatInputHolder,
                                            name: "password",
                                            placeholder : i18n("Password")
                                        },
                                        {
                                            element: MatInputHolder,
                                            name: "enabled",
                                            placeholder : i18n("Enabled")
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
                                            name: "roles",
                                            placeholder : i18n("Roles")
                                        }
                                    ]
                                },
                                {
                                    element: "div",
                                    style: {
                                        marginBottom: "20px"
                                    },
                                    children: [
                                        {
                                            element: "h3",
                                            text: i18n("Email")
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
                                            name: "email",
                                            placeholder : i18n("Email")
                                        },
                                        {
                                            element: MatInputHolder,
                                            name: "emailConfirmed",
                                            placeholder : i18n("Email Confirmed"),
                                            disabled : true
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
                                document.location.hash = "#/anjunar/control/users";
                                windowManager.close(this.window);
                                document.location.reload();
                            }
                        },
                        {
                            element: HateoasButton,
                            hateoas: "delete",
                            text: i18n("Delete"),
                            onAfterSubmit : () => {
                                document.location.hash = "#/anjunar/control/users";
                                windowManager.close(this.window);
                                document.location.reload();
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
    "First Name" : {
        "en-DE" : "First Name",
        "de-DE" : "Vorname"
    },
    "Last Name" : {
        "en-DE" : "Last Name",
        "de-DE" : "Nachname"
    },
    "Birthdate" : {
        "en-DE" : "Birthdate",
        "de-DE" : "Geburtstag"
    },
    "Password" : {
        "en-DE" : "Password",
        "de-DE" : "Passwort"
    },
    "Enabled" : {
        "en-DE" : "Enabled",
        "de-DE" : "Aktiviert"
    },
    "Save" : {
        "en-DE" : "Save",
        "de-DE" : "Speichern"
    },
    "Update" : {
        "en-DE" : "Update",
        "de-DE" : "Aktualisieren"
    },
    "Reset" : {
        "en-DE" : "Reset",
        "de-DE" : "Zurücksetzen"
    },
    "Delete" : {
        "en-DE" : "Delete",
        "de-DE" : "Löschen"
    },
    "Run As" : {
        "en-DE" : "Run As",
        "de-DE" : "Ausführen"
    },
    "User" : {
        "en-DE" : "User",
        "de-DE" : "Benutzer"
    },
    "Image" : {
        "en-DE" : "Image",
        "de-DE" : "Bild"
    },
    "Roles" : {
        "en-DE" : "Roles",
        "de-DE" : "Rollen"
    },
    "Email" : {
        "en-DE" : "Email",
        "de-DE" : "Email"
    },
    "Email Confirmed" : {
        "en-DE" : "Email Confirmed",
        "de-DE" : "eMail bestätigt"
    },
    "Already there" : {
        "en-DE" : "Already there",
        "de-DE" : "Schon vorhanden"
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


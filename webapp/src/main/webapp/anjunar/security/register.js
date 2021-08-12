import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import HateoasForm from "../../library/simplicity/hateoas/hateoas-form.js";
import MatInputHolder from "../../library/simplicity/hateoas/hateoas-input.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";
import HateoasButton from "../../library/simplicity/hateoas/hateoas-button.js";
import {windowManager} from "../../library/simplicity/services/window-manager.js";

export default class Register extends HTMLWindow {

    #user

    get user() {
        return this.#user;
    }

    set user(value) {
        this.#user = value;
    }

    render() {
        builder(this, {
            element: HateoasForm,
            model : this.#user,
            attributes : {
                autocomplete : "off"
            },
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
                    placeholder : i18n("Birthday"),
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
                    element: HateoasButton,
                    hateoas : "register",
                    text : i18n("Send"),
                    onAfterSubmit : () => {
                        windowManager.close(this.window);
                        document.location.reload();
                    }
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
    "Birthday" : {
        "en-DE" : "Birthday",
        "de-DE" : "Geburtstag"
    },
    "Password" : {
        "en-DE" : "Password",
        "de-DE" : "Passwort"
    },
    "Question for Password" : {
        "en-DE" : "Question for Password",
        "de-DE" : "Frage für Passwort"
    },
    "Send" : {
        "en-DE" : "Send",
        "de-DE" : "Senden"
    },
    "Register" : {
        "en-DE" : "Register",
        "de-DE" : "Registrieren"
    },
    "Already there" : {
        "en-DE" : "Already there",
        "de-DE" : "Schon vorhanden"
    }
});

customViews.define({
    name: "security-register",
    class: Register,
    header : i18n("Register"),
    resizable : false,
    guard(activeRoute) {
        return {
            user : jsonClient.get("service/security/register")
        }
    }
})
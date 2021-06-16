import {builder, customViews, i18nFactory} from "../library/simplicity/simplicity.js";
import {loadRoot} from "./service.js";

export default class Welcome extends HTMLElement {

    get service() {
        return loadRoot();
    }

    render() {
        builder(this, {
            element: "div",
            style: {
                display: "block",
                position: "absolute",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)"
            },
            children: [
                {
                    element: "div",
                    if : () => {
                        return this.service.links.find((link) => link.rel === "login");
                    },
                    children: [
                        {
                            element: "a",
                            href : () => {
                                return `#/anjunar/security/login`
                            },
                            text: i18n("please log in.")
                        },
                        {
                            element: "br"
                        },
                        {
                            element: "br"
                        },
                        {
                            element: "a",
                            href : () => {
                                return `#/anjunar/security/register`
                            },
                            text: i18n("Register here")
                        }
                    ]
                }, {
                    element: "div",
                    if : () => {
                        return this.service.links.find((link) => link.rel === "logout");
                    },
                    children: [
                        {
                            element: "span",
                            text : () => {
                                return `${i18n("Welcome")}, ${this.service.user.firstName} `
                            }
                        },
                        {
                            element: "a",
                            href : () => {
                                return `#/anjunar/security/logout`
                            },
                            text: i18n("click here to logout")
                        }
                    ]
                }
            ]
        })
    }

}

customViews.define({
    name: "app-welcome",
    class: Welcome
})

const i18n = i18nFactory({
    Welcome : {
        en : "Welcome",
        de : "Willkommen"
    },
    "click here to logout" : {
        en : "click here to logout",
        de : "hier klicken um abzumelden"
    },
    "Register here" : {
        en : "Register here",
        de : "Hier Registrieren"
    },
    "please log in." : {
        en : "please log in.",
        de : "Bitte anmelden"
    }
});
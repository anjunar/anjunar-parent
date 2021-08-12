import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";
import {jsonClient} from "../../library/simplicity/services/client.js";

export default class Confirm extends HTMLWindow {

    #confirm;

    get confirm() {
        return this.#confirm;
    }

    set confirm(value) {
        this.#confirm = value;
    }

    render() {
        builder(this, {
            element : "div",
            text : i18n("Successfully Confirmed eMail"),
            style : {
                display : this.#confirm === true ? "block" : "none"
            }
        })
    }

}

let i18n = i18nFactory({
    "Confirm" : {
        "en-DE" : "Confirm",
        "de-DE" : "Bestätigen"
    },
    "Successfully Confirmed eMail" : {
        "en-DE" : "Successfully Confirmed eMail",
        "de-DE" : "Erfolgreich eMail bestätigt"
    }
})

customViews.define({
    name : "control-confirm",
    class : Confirm,
    header : i18n("Confirm"),
    guard(activeRoute) {
        return {
            confirm : jsonClient.get(`service/control/users/user/confirm?id=${activeRoute.queryParams.id}&hash=${activeRoute.queryParams.hash}`)
        }
    }
})


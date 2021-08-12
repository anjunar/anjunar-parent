import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import MatInputHolder from "../../library/simplicity/hateoas/hateoas-input.js";
import HateoasButton from "../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../library/simplicity/hateoas/hateoas-form.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";
import {windowManager} from "../../library/simplicity/services/window-manager.js";

class Login extends HTMLWindow {

    #model;

    get model() {
        return this.#model;
    }

    set model(value) {
        this.#model = value;
    }

    render() {
        builder(this, {
            element: HateoasForm,
            model : this.#model,
            children: [
                {
                    element: MatInputHolder,
                    name: "firstname",
                    placeholder : i18n("First Name")
                },
                {
                    element: MatInputHolder,
                    name: "lastname",
                    placeholder : i18n("Last Name")
                },
                {
                    element: MatInputHolder,
                    name: "birthday",
                    placeholder : i18n("Birthday")
                },
                {
                    element: MatInputHolder,
                    name: "password",
                    placeholder : i18n("Password")
                },
                {
                    element: HateoasButton,
                    hateoas : "login",
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
    "Send" : {
        "en-DE" : "Send",
        "de-DE" : "Senden"
    }
});

export default customViews.define({
    name: "security-login",
    class: Login,
    header : "Login",
    resizable : false,
    guard(activeRoute) {
        return {
            model: jsonClient.get("service/security/login")
        }
    }
})
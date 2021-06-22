import {builder, customViews} from "../../library/simplicity/simplicity.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import {loadRoot} from "../service.js";
import MatInputHolder from "../../library/simplicity/hateoas/hateoas-input.js";
import HateoasButton from "../../library/simplicity/hateoas/hateoas-button.js";
import HateoasForm from "../../library/simplicity/hateoas/hateoas-form.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";

export default class Login extends HTMLElement {

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
            style: {
                display: "block",
                position: "absolute",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)"
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
                    element: HateoasButton,
                    hateoas : "login",
                    text : i18n("Send"),
                    onAfterSubmit : () => {
                        loadRoot(true);
                        document.location.hash = "#/anjunar/welcome";
                    }
                }
            ]
        })
    }

}

customViews.define({
    name: "security-login",
    class: Login,
    guard(activeRoute) {
        return {
            model: jsonClient.get("service/security/login")
        }
    }
})

const i18n = i18nFactory({
    "Send" : {
        "en-DE" : "Send",
        "de-DE" : "Senden"
    }
});

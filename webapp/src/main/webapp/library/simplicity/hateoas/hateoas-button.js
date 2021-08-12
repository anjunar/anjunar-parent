import {builder, customComponents} from "../simplicity.js";
import {jsonClient} from "../services/client.js";

export function hateoasButton(attributes) {
    return function () {
        return Object.assign({
            element : HateoasButton,
            children : Array.from(arguments)
        }, attributes)
    }
}

export default class HateoasButton extends HTMLElement {

    #hateoas;
    #text = "";
    #isInitialized = false;

    get hateoas() {
        return this.#hateoas;
    }

    set hateoas(value) {
        this.#hateoas = value;
    }

    get text() {
        return this.#text;
    }

    set text(value) {
        this.#text = value;
    }

    render() {

        if (! this.#isInitialized) {
            builder(this, {
                element : "button",
                type : "button",
                className : "button",
                text : this.#text,
                update : () => {
                    let form = this.queryUpwards((element) => {return element.localName === "hateoas-form"});
                    if (form.model) {
                        let link = form.model.actions.find((link) => link.rel === this.#hateoas);
                        if (link) {
                            this.style.display = "inline"
                        } else {
                            this.style.display = "none"
                        }
                    }
                },
                onClick : (event) => {
                    event.stopPropagation();
                    let hateoasForm = this.queryUpwards((element) => {return element.localName === "hateoas-form"});
                    let domForm = this.queryUpwards((element) => { return element.localName === "form"})
                    domForm.updateValue();
                    let link = hateoasForm.model.actions.find((link) => link.rel === this.#hateoas);
                    jsonClient.action(link.method, link.url, {body : domForm.value})
                        .then((response) => {
                            this.dispatchEvent(new CustomEvent("afterSubmit", {detail : response}))
                        })
                    return false;
                }
            })

            this.#isInitialized = true;
        }

    }

}

customComponents.define("hateoas-button", HateoasButton)
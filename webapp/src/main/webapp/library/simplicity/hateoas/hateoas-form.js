import {builder, customComponents} from "../simplicity.js";
import DomForm from "../directives/dom-form.js";

export function hateoasForm(attributes) {
    return function () {
        return Object.assign({
            element : HateoasForm,
            children : Array.from(arguments)
        }, attributes)
    }
}

export default class HateoasForm extends HTMLElement {

    #model;
    #validators = [];

    get model() {
        return this.#model;
    }

    set model(value) {
        this.#model = value;
    }

    field(name) {
        return this.#model.meta.properties.find(field => field.name === name);
    }

    addValidator(validator) {
        this.#validators.push(validator);
    }

    render() {
        this.style.display = "block";

        builder(this, {
            element : DomForm,
            style : {
                height : "100%"
            },
            value : {
                input : () => {
                    return this.#model
                },
                output : (value) => {
                    this.#model = value
                }
            },
            initialize : (element) => {
                let container = document.createDocumentFragment();
                for (const child of Array.from(this.children)) {
                    if (! (child instanceof DomForm)) {
                        container.appendChild(child);
                    }
                }
                element.appendChild(container);
            }
        })

        let form = this.querySelector("form");
        for (const validator of this.#validators) {
            form.addValidator(validator);
        }
    }


}

customComponents.define("hateoas-form", HateoasForm)


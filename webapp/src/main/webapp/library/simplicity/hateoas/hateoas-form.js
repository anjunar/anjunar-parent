import {builder, customComponents} from "../simplicity.js";
import DomForm from "../directives/dom-form.js";

export function hateoasForm(attributes) {
    return function (children) {
        return Object.assign({
            element : HateoasForm,
            children : children
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
        return this.#model.properties.find(field => field.name === name);
    }

    addValidator(validator) {
        this.#validators.push(validator);
    }

    render() {
        this.style.display = "block";

        builder(this, {
            element : DomForm,
            value : {
                input : () => {
                    return this.#model.form
                },
                output : (value) => {
                    this.#model.form = value
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


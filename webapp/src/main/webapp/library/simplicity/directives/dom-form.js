import {customComponents} from "../simplicity.js";
import {debounce} from "../services/tools.js";

export default class DomForm extends HTMLFormElement {

    #value;
    #elements = [];
    #validators = [];
    #errors = [];

    get value() {
        return this.#value;
    }

    set value(v) {
        this.#value = v;
    }

    get elements() {
        return this.#elements;
    }

    get validators() {
        return this.#validators;
    }

    addValidator(value) {
        this.#validators.push(value);
    }

    get errors() {
        return this.#errors;
    }

    get pristine() {
        return this.#elements.every((input) => {
            return input.pristine
        });
    }

    get valid() {
        let every = this.#elements.every((input) => {
            return input.valid
        });
        return every && this.#errors.length === 0;
    }

    reset() {
        for (const element of this.#elements) {
            element.reset();
        }
    }

    get dirty() {
        return !this.pristine;
    }

    updateValue() {
        for (const element of this.#elements) {
            if (element.type === "checkbox") {
                this.#value[element.name] = element.checked
            } else {
                this.#value[element.name] = element.value
            }

        }
    }


    constructor() {
        super();

        this.addEventListener("rendered", () => {
            let process = (element) => {
                this.#elements.push(element);

                let validationHandler = () => {
                    for (const validator of this.#validators) {
                        validator.validate(this)
                            .then((result) => {
                                let indexOf = this.#errors.indexOf(result);
                                if (indexOf > -1) {
                                    this.#errors.splice(indexOf, 1);
                                }
                            })
                            .catch((reason) => {
                                let indexOf = this.#errors.indexOf(reason);
                                if (indexOf === -1) {
                                    this.#errors.push(reason)
                                }
                            })
                    }
                }

                let inputHandler = () => {
                    let name = element.name;
                    if (element.type === "checkbox") {
                        this.#value[name] = element.checked;
                    } else {
                        this.#value[name] = element.value;
                    }
                }

                element.addEventListener("keyup", inputHandler);
                element.addEventListener("change", inputHandler);

                element.addEventListener("keyup", debounce(validationHandler, 300));
                element.addEventListener("change", debounce(validationHandler, 300));

                element.formular = this;

                if (element.type === "checkbox") {
                    element.checked = this.#value[element.name];
                } else {
                    element.value = this.#value[element.name];
                }

                element.dispatchEvent(new Event("change"))
            }

            function walk(element) {

                if (element.name) {
                    if (element.isInput) {
                        process(element);
                    }
                    if (element instanceof HTMLTextAreaElement) {
                        process(element);
                    }
                }

                for (const child of element.children) {
                    if (child instanceof HTMLFormElement) {
                        continue;
                    }
                    walk(child);
                }
            }

            walk(this);
        })
    }

}

customComponents.define("dom-form", DomForm, {extends: "form"})
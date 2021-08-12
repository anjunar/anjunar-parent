import {customComponents} from "../simplicity.js";

export default class DomInput extends HTMLInputElement {

    #isInitialized;
    #defaultValue;
    #errors = [];
    #formular;
    #use;
    #validation = {};
    #disabled = false;

    #validators = [
        {
            notEmpty : (element) => {
                if (this.#validation.notEmpty) {
                    return String(element.value).length > 0;
                }
                return true;
            }
        },{
            size : (element) => {
                if (this.#validation.size) {
                    return String(element.value).length >= this.#validation.size.min
                }
                if (this.#validation.size) {
                    return String(element.value).length <= this.#validation.size.max
                }
                return true;
            }
        },
        {
            email : (element) => {
                if (this.#validation.email) {
                    if (element.value === "") {
                        return true;
                    }
                    let regex = /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/
                    return regex.test(element.value)
                }
                return true;
            }
        }
    ];

    #formatter = (value) => {
        return value;
    };


    isInput() {
        return true;
    }

    get errors() {
        return this.#errors;
    }

    set errors(value) {
        this.#errors = value;
    }

    get isInitialized() {
        return this.#isInitialized;
    }

    set isInitialized(value) {
        this.#isInitialized = value;
    }

    get defaultValue() {
        return this.#defaultValue;
    }

    set defaultValue(value) {
        this.#defaultValue = value;
    }

    get formular() {
        return this.#formular;
    }

    set formular(value) {
        this.#formular = value;
    }

    get use() {
        return this.#use;
    }

    set use(value) {
        this.#use = value;
    }

    get validation() {
        return this.#validation;
    }

    set validation(value) {
        this.#validation = value;
    }

    get disabled() {
        return this.#disabled;
    }

    set disabled(value) {
        this.#disabled = value;
    }

    reset() {
        this.value = this.#defaultValue;
        this.dispatchEvent(new Event("change"));
    }

    get dirty() {
        return !this.pristine;
    }

    get pristine() {
        if (this.type === "checkbox") {
            return this.#defaultValue === this.checked;
        } else {
            return this.#defaultValue === this.value;
        }

    }

    get valid() {
        return this.#errors.length === 0;
    }

    get validators() {
        return this.#validators;
    }

    get formatter() {
        return this.#formatter;
    }

    set formatter(value) {
        this.#formatter = value;
    }

    render() {

        if (this.#disabled) {
            this.setAttribute("disabled", "true")
        }

        let handler = (event) => {
            if (!this.#isInitialized) {
                if (this.type === "checkbox") {
                    this.#defaultValue = this.checked;
                } else {
                    this.#defaultValue = this.value;
                }
                this.#isInitialized = true;
            }

            for (const validator of this.#validators) {
                let name = Object.keys(validator)[0];
                let result = validator[name](this);
                let indexOf = this.#errors.indexOf(name);
                if (result) {
                    if (indexOf > -1) {
                        this.#errors.splice(indexOf, 1);
                    }
                } else {
                    if (indexOf === -1) {
                        this.#errors.push(name)
                    }
                }
            }

            event.stopPropagation();
            return false;
        }

        this.addEventListener("focus", handler);
        this.addEventListener("blur", handler);
        this.addEventListener("keyup", handler);
        this.addEventListener("change", handler);

    }

}

customComponents.define("dom-input", DomInput, {extends : "input"})
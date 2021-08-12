import {customComponents} from "../simplicity.js";



export default class DomSelect extends HTMLSelectElement {

    #isInitialized = false;
    #defaultValue;

    #errors = [];

    isInput() {
        return true;
    }

    get pristine() {
        return this.#defaultValue === this.value;
    }

    reset() {
        this.value = this.#defaultValue;
        this.dispatchEvent(new Event("change"));
    }

    get valid() {
        return true;
    }

    get errors() {
        return this.#errors;
    }

    render() {
        this.addEventListener("change", () => {
            if (! this.#isInitialized) {
                this.#defaultValue = this.value;
                this.#isInitialized = true;
            }
        })

        let options = Array.from(this.options);

        let option = options.find(option => option.value === this.value);

        let number = options.indexOf(option);

        this.selectedIndex = number;
    }

}

customComponents.define("dom-select", DomSelect, {extends : "select"})
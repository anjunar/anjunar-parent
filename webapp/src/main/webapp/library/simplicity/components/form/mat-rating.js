import {builder, customComponents} from "../../simplicity.js";
import DomInput from "../../directives/dom-input.js";

export default class MatRating extends HTMLElement {

    #name;
    #value;

    constructor() {
        super();

        this.addEventListener("click", () => {
            let inputs = Array.from(this.querySelectorAll("input"));
            let checked;

            for(let input of inputs) {
                if (input.checked) {
                    checked = input;
                    this.#value = input.use
                    this.dispatchEvent(new Event("change"));
                }
            }

            for(let i = 0; i < inputs.length; i++) {
                if (i < inputs.indexOf(checked)) {
                    inputs[i].classList.add("selected");
                } else {
                    inputs[i].classList.remove("selected");
                }
            }
        })
    }

    get value() {
        return this.#value;
    }

    set value(v) {
        this.#value = v;
    }

    get name() {
        return this.#name;
    }

    set name(value) {
        this.#name = value
    }

    render() {
        builder(this, {
            element : "div",
            style : {
                display : "flex"
            },
            children : [
                {
                    element : DomInput,
                    type : "radio",
                    name : this.#name,
                    use : "1"
                },
                {
                    element : DomInput,
                    type : "radio",
                    name : this.#name,
                    use : "2"
                },
                {
                    element : DomInput,
                    type : "radio",
                    name : this.#name,
                    use : "3"
                },
                {
                    element : DomInput,
                    type : "radio",
                    name : this.#name,
                    use : "4"
                },
                {
                    element : DomInput,
                    type : "radio",
                    name : this.#name,
                    use : "5"
                }
            ]
        })
    }

}

customComponents.define("mat-rating", MatRating)

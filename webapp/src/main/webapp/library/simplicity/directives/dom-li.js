import {customComponents} from "../simplicity.js";

export default class DomLi extends HTMLElement {

    #open

    get open() {
        return this.#open;
    }

    set open(value) {
        this.#open = value;
    }

    render() {
        let open = false;

        this.addEventListener("click", () => {
            open = ! open;

            let el = this.querySelector("ul")

            if (el) {
                if (open) {
                    el.style.display = "block";
                } else {
                    el.style.display = "none";
                }
            }

        })

        let el = this.querySelector("ul");

        if (el) {
            el.style.display = "none";
        }

    }

}

customComponents.define("dom-li", DomLi)
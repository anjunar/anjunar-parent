import {builder, customComponents} from "../../simplicity.js";
import DomInputFile from "../../directives/dom-input[file].js";

export default class MatImageUpload extends HTMLElement {

    #isInitialized = false;
    #showButton = true;
    #value = {
        data : ""
    };
    #defaultValue;


    isInput() {
        return true;
    }

    get showButton() {
        return this.#showButton;
    }

    set showButton(value) {
        this.#showButton = value;
    }

    click() {
        let input = this.querySelector("input[type=file]");
        input.click();
    }

    get value() {
        return this.#value;
    }

    set value(v) {
        this.#value = v;
        if (! this.#isInitialized) {
            this.#defaultValue = JSON.stringify(this.#value);
            this.#isInitialized = true;
        }
    }

    get valid() {
        return true;
    }

    get pristine() {
        return this.#defaultValue === JSON.stringify(this.#value);
    }

    get dirty() {
        return ! this.pristine;
    }

    reset() {
        this.#value = JSON.parse(this.#defaultValue);
    }

    render() {

        this.style.display = "block";
        this.style.minWidth = "inherit";
        this.style.minHeight = "inherit"
        this.style.maxWidth = "inherit";
        this.style.maxHeight = "inherit"

        builder(this, {
            element : "div",
            style : {
                minWidth : "inherit",
                minHeight : "inherit",
                maxWidth : "inherit",
                maxHeight : "inherit"
            },
            children : [
                {
                    element : "img",
                    style : {
                        border : "1px solid var(--main-normal-color)",
                        minWidth : "inherit",
                        minHeight : "inherit",
                        maxWidth : "inherit",
                        maxHeight : "inherit"
                    },
                    src : () => {
                        if (this.#value) {
                            return this.#value.data
                        }
                        return "#"
                    },
                    onClick : () => {
                        let input = this.querySelector("input[type=file]");
                        input.click();
                    }
                },
                {
                    element : DomInputFile,
                    style : {
                        display : "none"
                    },
                    onLoadend : (event) => {
                        this.#value = event.detail.load;
                        this.dispatchEvent(new Event("change"));
                        this.dispatchEvent(new CustomEvent("load", {detail : event.detail}));
                    }
                }
            ]
        })

        this.addEventListener("change", () => {
            if (! this.#isInitialized) {
                this.#defaultValue = JSON.stringify(this.#value);
                this.#isInitialized = true;
            }
        })
    }

}

customComponents.define("mat-image-upload", MatImageUpload)
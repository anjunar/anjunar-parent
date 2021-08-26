import {builder, customComponents} from "../../simplicity.js";
import MatSelect from "./mat-select.js";

export default class MatMultiSelect extends HTMLElement {

    #isInitialized = false;

    #value = [];
    #defaultValue = [];

    #items;
    #selection;
    #placeholder

    #index = 0;
    #limit = 20;

    #id = "id";

    #meta;

    #validation = {};
    #errors = [];

    #validators = [];
    #disabled = false;

    constructor() {
        super();

        this.addEventListener("change", () => {
            if (!this.#isInitialized) {
                this.#defaultValue = Array.from(this.#value);
                this.#isInitialized = true;
            }
        })
    }

    isInput() {
        return true;
    }

    get value() {
        return this.#value;
    }

    set value(v) {
        this.#value = v
    }

    get items() {
        return this.#items;
    }

    set items(value) {
        this.#items = value
    }

    get selection() {
        return this.#selection;
    }

    set selection(value) {
        this.#selection = value;
    }

    get meta() {
        return this.#meta;
    }

    set meta(value) {
        this.#meta = value
    }

    get disabled() {
        return this.#disabled;
    }

    set disabled(value) {
        this.#disabled = value;
    }

    get placeholder() {
        return this.#placeholder;
    }

    set placeholder(value) {
        this.#placeholder = value;
    }

    get id() {
        return this.#id;
    }

    set id(value) {
        this.#id = value;
    }

    get validators() {
        return this.#validators;
    }

    get validation() {
        return this.#validation;
    }

    set validation(value) {
        this.#validation = value;
    }

    get errors() {
        return this.#errors;
    }

    get pristine() {
        if (this.#value.length !== this.#defaultValue.length) {
            return false;
        }

        let valueCore = this.#value.map((v => v[this.#id]));
        let defaultValueCore = this.#defaultValue.map((v => v[this.#id]));

        for (let i = 0; i < valueCore.length; i++) {
            if (valueCore[i] !== defaultValueCore[i]) {
                return false;
            }
        }

        return true;
    }

    get dirty() {
        return ! this.pristine
    }

    get valid() {
        return this.errors.length === 0;
    }

    reset() {
        this.#value = this.#defaultValue;
        this.dispatchEvent(new Event("change"));
    }

    render() {
        builder(this, [
            {
                element : MatSelect,
                placeholder: this.#placeholder,
                disabled : this.#disabled,
                attributes : {
                    disabled : () => {
                        return this.hasAttribute("disabled")
                    }
                },
                onChange : (event) => {
                    let item = this.#value.find((item) => item[this.#id] === event.target.value[this.#id]);
                    if (! item) {
                        this.#value = [...this.#value, event.target.value];
                        this.dispatchEvent(new Event("change"))
                    }
                },
                items: {
                    direct : (query, callback) => {
                        this.items({index: query.index, limit: query.limit, value: query.value}, (_data, _size) => {
                            callback(_data, _size)
                        });
                    },
                },
                meta: (item) => {
                    return {
                        element: "div",
                        initialize : (element) => {
                            let m = this.#meta.option
                            builder(element, m(item));
                        }
                    }
                }
            }, {
                element : "div",
                style : {
                    border : () => {
                        if (this.#value.length === 0 && this.#validation.notEmpty) {
                            return "1px solid var(--main-error-color)";
                        } else {
                            return "1px solid var(--main-dark1-color)";
                        }
                    },
                    height : "100px",
                    padding : "5px"
                },
                children : [
                    {
                        element : "div",
                        children : [
                            {
                                element: "div",
                                if : () => {
                                    return this.#value.length === 0  && this.#validation.notEmpty;
                                },
                                text : "not Emtpy"
                            },
                            {
                                element : "div",
                                children : {
                                    items : () => {
                                        return this.#value;
                                    },
                                    item : (item) => {
                                        return {
                                            element : "div",
                                            style : {
                                                display : "flex"
                                            },
                                            children : [
                                                {
                                                    element : "div",
                                                    initialize : (element) => {
                                                        let m = this.#meta.selection
                                                        builder(element, m(item));
                                                    }
                                                },
                                                {
                                                    element: "div",
                                                    style : {
                                                        flex : "1"
                                                    }
                                                },
                                                {
                                                    element : "button",
                                                    type : "button",
                                                    className : "icon",
                                                    text : "delete",
                                                    style : {
                                                        display : () => {
                                                            return this.hasAttribute("disabled") ? "none" : "block"
                                                        }
                                                    },
                                                    onClick : () => {
                                                        if (! this.#disabled) {
                                                            let indexOf = this.#value.indexOf(item);
                                                            this.#value = Array.from(this.#value);
                                                            this.#value.splice(indexOf, 1);
                                                            this.dispatchEvent(new Event("change"))
                                                        }
                                                    }
                                                }
                                            ]
                                        }
                                    }
                                }
                            }
                        ]
                    }
                ]

            }
        ])

    }

}

customComponents.define("mat-multi-select", MatMultiSelect)
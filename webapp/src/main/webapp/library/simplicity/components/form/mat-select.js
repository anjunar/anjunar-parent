import {builder, customComponents} from "../../simplicity.js";
import MatInputContainer from "./containers/mat-input-container.js";
import DomInput from "../../directives/dom-input.js";
import {debounce} from "../../services/tools.js";

export default class MatSelect extends HTMLElement {

    #placeholder = "";
    #open = false;
    #value;
    #items;

    #size = 0;
    #limit = 5;

    #index = 0;
    #window = [];

    #id = "id";
    #label = "name"

    #meta;
    #disabled = false;

    get isInput() {
        return true;
    }

    get placeholder() {
        return this.#placeholder;
    }

    set placeholder(value) {
        this.#placeholder = value;
    }

    get value() {
        return this.#value;
    }

    set value(v) {
        this.#value = v;
    }

    get meta() {
        return this.#meta;
    }

    set meta(value) {
        this.#meta = value;
    }

    get disabled() {
        return this.#disabled;
    }

    set disabled(value) {
        this.#disabled = value;
    }

    get items() {
        return this.#items;
    }

    set items(value) {
        this.#items = value;
    }

    get label() {
        return this.#label;
    }

    set label(value) {
        this.#label = value;
    }

    get id() {
        return this.#id;
    }

    set id(value) {
        this.#id = value;
    }

    render() {

        let up = () => {
            this.#index -= 5
            load();
        }

        let down = () => {
            this.#index += 5;
            load();
        }

        let load = () => {
            let input = this.querySelector("input");

            this.#items({index: this.#index, limit: this.#limit, value: input.value}, (data, size) => {
                this.#size = size;
                this.#window = data;
            });
        }

        document.addEventListener("click", () => {
            this.#open = false;
        })

        builder(this, {
            element : "div",
            style : {
                position : "relative"
            },
            children : [
                {
                    element : MatInputContainer,
                    placeholder : this.#placeholder,
                    content : {
                        element : DomInput,
                        type : "text",
                        disabled : this.#disabled,
                        value : {
                            input : () => {
                                if (this.#value?.meta) {
                                    return this.#value.meta.properties
                                        .filter((element) => element.naming)
                                        .map((element) => this.#value[element.name])
                                        .join(" ");
                                }
                                return this.#value || "";
                            },
                            output : (value) => {
                                this.#value = value;
                            }
                        },
                        onClick : (event) => {
                            event.stopPropagation();
                            this.#open = true;
                            load();
                            return false;
                        },
                        initialize : (element) => {
                            element.addEventListener("keyup", debounce(() => {
                               load();
                            }, 300))
                        }
                    }
                }, {
                    element: "div",
                    if : () => {
                        return this.#open;
                    },
                    style : {
                        position: "absolute",
                        top: "34px",
                        backgroundColor: "var(--main-background-color)",
                        width: "100%",
                        textAlign : "center",
                        zIndex : "1000",
                        boxShadow: `0 3px 1px -2px rgba(255, 255, 255, .2),
                                            0 2px 2px 0 rgba(255, 255, 255, .14),
                                            0 1px 5px 0 rgba(255, 255, 255, .12)`
                    },
                    onMouseLeave : () => {
                        this.#open = false;
                    },
                    children : [
                        {
                            element: "button",
                            type : "button",
                            className : "material-icons",
                            style : {
                                width : "100%",
                                display : () => {
                                    return this.#index > 0 ? "inline" : "none"
                                }
                            },
                            onClick : () => {
                                up();
                            },
                            text : "arrow_drop_up"
                        }, {
                            element: "div",
                            style : {
                                textAlign : "left",
                            },
                            children : {
                                items : () => {
                                    return this.#window;
                                },
                                item : (item) => {
                                    return {
                                        element: "div",
                                        className : "hover",
                                        style : {
                                            fontSize : "small",
                                            padding : "2px",
                                            lineHeight : "32px"
                                        },
                                        onClick : () => {
                                            if (! this.hasAttribute("disabled")) {
                                                let input = this.querySelector("input");
                                                // input.value = item[this.#label]
                                                this.#value = item;
                                                this.#open = false;
                                                this.dispatchEvent(new Event("change"));
                                            }
                                        },
                                        initialize : (element) => {
                                            let m = this.#meta
                                            builder(element, m(item));
                                        }
                                    }
                                }
                            }
                        },
                        {
                            element: "button",
                            type : "button",
                            className : "material-icons",
                            style : {
                                width : "100%",
                                display : () => {
                                    return this.#index + this.#limit < this.#size ? "inline" : "none"
                                }
                            },
                            onClick : () => {
                                down();
                            },
                            text : "arrow_drop_down"
                        }
                    ]
                }
            ]
        })
    }

}

customComponents.define("mat-select", MatSelect)
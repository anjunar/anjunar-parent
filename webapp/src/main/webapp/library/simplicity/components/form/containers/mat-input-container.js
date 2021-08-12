import {builder, customComponents} from "../../../simplicity.js";

export default class MatInputContainer extends HTMLElement {

    #placeholder = "";

    #prefix;
    #content;
    #suffix;

    #errors = [];
    #hints = [];

    get placeholder() {
        return this.#placeholder;
    }

    set placeholder(value) {
        this.#placeholder = value;
    }

    get prefix() {
        return this.#prefix;
    }

    set prefix(value) {
        this.#prefix = value;
    }

    get content() {
        return this.#content;
    }

    set content(value) {
        this.#content = value;
    }

    get suffix() {
        return this.#suffix;
    }

    set suffix(value) {
        this.#suffix = value;
    }

    get errors() {
        return this.#errors;
    }

    set errors(value) {
        this.#errors = value;
    }

    get hints() {
        return this.#hints;
    }

    set hints(value) {
        this.#hints = value;
    }

    addError(error) {
        error.style.display = "none";
        let errorContainer = this.querySelector("div.errorContainer");
        errorContainer.appendChild(error);
    }

    addHint(hint) {
        let hintContainer = this.querySelector("div.hintContainer");
        hintContainer.appendChild(hint);
    }

    render() {

        this.#content.placeholder = this.#placeholder;

        builder(this, {
            element : "div",
            children : [
                {
                    element : "div",
                    style : {
                        height : "14px"
                    },
                    children : [
                        {
                            element : "span",
                            if : () => {
                                if (this.#content.type === "date") {
                                    return true;
                                } else {
                                    return this.#content.value.length > 0;
                                }
                            },
                            text : () => {
                                return this.#placeholder;
                            },
                            style : {
                                fontSize : "x-small",
                                color: () => {
                                    if (document.activeElement === this.#content) {
                                        if (this.#content.errors.length) {
                                            return "var(--main-error-color)";
                                        } else {
                                            return "var(--main-selected-color)";
                                        }
                                    } else {
                                        return "var(--main-grey-color)";
                                    }
                                }
                            }
                        }
                    ]
                },
                {
                    element : "div",
                    style : {
                        display : "flex"
                    },
                    children: [
                        {
                            element: "div",
                            initialize : (element) => {
                                if (this.#prefix) {
                                    element.appendChild(this.#prefix);
                                }
                            }
                        },
                        {
                            element: "div",
                            style: {
                                flex : 1
                            },
                            initialize : (element) => {
                                if (this.#content) {
                                    element.appendChild(this.#content);
                                }
                            }
                        },
                        {
                            element: "div",
                            initialize : (element) => {
                                if (this.#suffix) {
                                    element.appendChild(this.#suffix);
                                }
                            }
                        }
                    ]
                },
                {
                    element: "hr",
                    style : {
                        backgroundColor : () => {
                            if (document.activeElement === this.#content) {
                                if (this.#content.errors.length) {
                                    return "var(--main-error-color)";
                                } else {
                                    return "var(--main-selected-color)";
                                }
                            } else {
                                return "var(--main-dark1-color)";
                            }
                        }
                    }
                },
                {
                    element: "div",
                    style : {
                        height : "12px"
                    },
                    children: [
                        {
                            element: "div",
                            style: {
                                fontSize : "x-small",
                                color : "var(--main-selected-color)",
                                display : "flex"
                            },
                            className : "hintContainer",
                            initialize : (element) => {
                                for (const hint of this.#hints) {
                                    element.appendChild(hint);
                                }
                            }
                        },
                        {
                            element : "div",
                            style: {
                                fontSize : "x-small",
                                color : "var(--main-error-color)",
                                display : "flex"
                            },
                            className : "errorContainer",
                            initialize : (element) => {
                                for (const error of this.#errors) {
                                    element.appendChild(error);
                                    error.style.display = "none";
                                }
                            },
                            update : (element) => {
                                for (const errorElement of element.children) {
                                    errorElement.style.display = "none";
                                }

                                for (const error of this.#content.errors) {
                                    let errorElement = element.querySelector(`div[name=${error}]`);
                                    errorElement.style.display = "inline"
                                }

                                let input = this.querySelector("input");

                                if (input?.formular) {
                                    for (const error of input.formular.errors) {
                                        let div = this.querySelector(`div[name=${error}]`);
                                        if (div) {
                                            if (input.formular.errors.length > 0) {
                                                div.style.display = "block";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    ]
                }
            ]
        })
    }


}

customComponents.define("mat-input-container", MatInputContainer);
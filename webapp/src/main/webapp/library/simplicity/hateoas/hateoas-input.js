import {builder, customComponents} from "../simplicity.js";
import MatInputContainer from "../components/form/containers/mat-input-container.js";
import DomInput from "../directives/dom-input.js";
import MatInputHolder from "./hateoas-input.js";
import MatMultiSelect from "../components/form/mat-multi-select.js";
import {jsonClient} from "../services/client.js";
import MatImageUpload from "../components/form/mat-image-upload.js";
import MatEditor from "../components/form/mat-editor.js";
import HateoasButton from "./hateoas-button.js";
import {hateoas} from "../services/tools.js";
import MatCheckboxContainer from "../components/form/containers/mat-checkbox-container.js";
import HateoasForm from "./hateoas-form.js";
import MatSelect from "../components/form/mat-select.js";
import {QueryBuilder} from "../services/querybuilder.js";

export function hateoasInput(attributes) {
    return Object.assign({
        element: HateoasInput
    }, attributes)
}


export default class HateoasInput extends HTMLElement {

    #isInitialized = false;
    #placeholder;
    #disabled = false;
    #errors = [];

    get placeholder() {
        return this.#placeholder;
    }

    set placeholder(value) {
        this.#placeholder = value;
    }


    get disabled() {
        return this.#disabled;
    }

    set disabled(value) {
        this.#disabled = value;
    }

    get errors() {
        return this.#errors;
    }

    set errors(value) {
        this.#errors = value;
    }

    render() {

        this.style.display = "block";

        let form = this.queryUpwards((element) => {
            return element.localName === "hateoas-form"
        });
        let field = form.field(this.name);

        function map(validators) {
            let result = {};
            for (const validator of validators) {
                result[validator.name] = validator;
            }
            return result;
        }

        switch (field.type) {
            case "form" : {
                builder(this, {
                    element: HateoasForm,
                    model: field,
                    children: [
                        {
                            element: "div",
                            children: field.properties.map((property) => {
                                return {
                                    element: MatInputHolder,
                                    name: property.name,
                                    placeholder : property.name
                                }
                            })
                        }, {
                            element: "div",
                            children: field.form.actions.map((link) => {
                                return {
                                    element: HateoasButton,
                                    hateoas: link.rel,
                                    text: link.rel,
                                    onAfterSubmit: (event) => {
                                        let link = hateoas(event.detail.links, "redirect")
                                        if (link) {
                                            window.location.hash = `#/anjunar/navigator/navigator?link=${btoa(link.url)}`
                                        }
                                    }
                                }

                            })
                        }
                    ]
                })
            }
                break;
            case "editor" : {
                builder(this, {
                    element: MatEditor,
                    name: field.name,
                    disabled : this.#disabled,
                    style : {
                        height : "300px"
                    }
                })
            }
                break;
            case "image" : {
                builder(this, {
                        element: "div",
                        style: {
                            minWidth: "200px",
                            maxWidth: "300px",
                            minHeight: "200px",
                            maxHeight: "300px"
                        },
                        children: [
                            {
                                element: MatImageUpload,
                                name: field.name,
                                placeholder : this.#placeholder,
                                disabled : this.#disabled
                            }
                        ]
                    }
                )
            }
                break;
            case "lazyselect" : {
                builder(this, {
                    element: MatSelect,
                    placeholder : this.#placeholder,
                    name: field.name,
                    disabled : this.#disabled,
                    items: {
                        direct: (query, callback) => {
                            let link = field.links.find((link) => link.rel === "list");

                            link.body.index = query.index;
                            link.body.limit = query.limit;
                            link.body.naming = query.value;

                            jsonClient.action(link.method, link.url, {body : link.body})
                                .then((response) => {
                                    callback(response.rows, response.size)
                                })
                        }
                    },
                    meta: {
                        option: {
                            element: (element) => {
                                let namingProperties = element.meta.properties
                                    .filter((element) => element.naming)
                                    .map((elem) => element[elem.name])
                                    .join(" ")
                                return {
                                    element: "div",
                                    text: namingProperties
                                }
                            }
                        }
                    }
                })
            }
                break;
            case "lazymultiselect" : {
                builder(this, {
                    element: MatMultiSelect,
                    placeholder : this.#placeholder,
                    name: field.name,
                    disabled : this.#disabled,
                    items: {
                        direct: (query, callback) => {
                            let link = field.links.find((link) => link.rel === "list");

                            link.body.index = query.index;
                            link.body.limit = query.limit;
                            link.body.naming = query.value;

                            jsonClient.action(link.method, link.url, {body : link.body})
                                .then((response) => {
                                    callback(response.rows, response.size)
                                })
                        }
                    },
                    meta: {
                        option: {
                            element: (element) => {
                                let namingProperties = element.meta.properties
                                    .filter((element) => element.naming)
                                    .map((elem) => element[elem.name])
                                    .join(" ")
                                return {
                                    element: "div",
                                    text: namingProperties
                                }
                            }
                        },
                        selection: {
                            element: (element) => {
                                let namingProperties = element.meta.properties
                                    .filter((element) => element.naming)
                                    .map((elem) => element[elem.name])
                                    .join(" ")
                                return {
                                    element: "div",
                                    text: namingProperties
                                }
                            }
                        }
                    }
                })
            }
                break;
            case "checkbox" : {
                builder(this, {
                    element: MatCheckboxContainer,
                    placeholder : this.#placeholder,
                    content: {
                        element: DomInput,
                        type: field.type,
                        name: this.name,
                        validation: map(field.validators),
                        disabled : this.#disabled
                    },
                });
            }
                break;
            case "textarea" : {
                builder(this, {
                    element: "textarea",
                    placeholder : this.#placeholder,
                    name: this.name,
                    disabled : this.#disabled
                })
            }
                break;
            default : {
                builder(this, {
                        element: MatInputContainer,
                        placeholder : this.#placeholder,
                        content: {
                            element: DomInput,
                            type: field.type,
                            name: this.name,
                            validation: map(field.validators),
                            disabled : this.#disabled
                        },
                        errors: field.validators.map(validator => {
                            return {
                                element: "div",
                                attributes: {
                                    name: validator.name
                                },
                                text: validator.message
                            }
                        }).concat(this.#errors),
                        hints: field.hints.map(hint => {
                            return {
                                element : "div",
                                text : hint.message
                            }
                        })
                    },
                )
            }
        }

    }

}

customComponents.define("hateoas-input-holder", HateoasInput);
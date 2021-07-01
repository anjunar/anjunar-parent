import {builder, customViews} from "../../library/simplicity/simplicity.js";
import MatInputContainer from "../../library/simplicity/components/form/containers/mat-input-container.js";
import DomInput from "../../library/simplicity/directives/dom-input.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import DomForm from "../../library/simplicity/directives/dom-form.js";

class Register extends HTMLElement {

    #user = {
        firstName: "",
        lastName: "",
        birthdate: "",
        question : "",
        password: ""
    };

    render() {
        builder(this, {
            element: DomForm,
            attributes : {
                autocomplete : "off"
            },
            value : {
                input : () => {
                    return this.#user;
                },
                output : (value) => {
                    this.#user = value;
                }
            },
            style: {
                display: "block",
                position: "absolute",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)"
            },
            children: [
                {
                    element: MatInputContainer,
                    placeholder: "First Name",
                    content: {
                        element: DomInput,
                        type: "text",
                        name: "firstName",
                        attributes : {
                            autocomplete : "nope"
                        }
                    }
                },
                {
                    element: MatInputContainer,
                    placeholder: "Last Name",
                    content: {
                        element: DomInput,
                        type: "text",
                        name: "lastName",
                        attributes : {
                            autocomplete : "nope"
                        }
                    }
                },
                {
                    element: MatInputContainer,
                    placeholder: "Birthdate",
                    content: {
                        element: DomInput,
                        type: "date",
                        name: "birthdate",
                        attributes : {
                            autocomplete : "nope"
                        }
                    }
                },
                {
                    element: MatInputContainer,
                    placeholder: "Question for Password",
                    content: {
                        element: DomInput,
                        type: "text",
                        name: "question",
                        attributes : {
                            autocomplete : "nope"
                        }
                    }
                },
                {
                    element: MatInputContainer,
                    placeholder: "Password",
                    content: {
                        element: DomInput,
                        type: "password",
                        name: "password",
                        attributes : {
                            autocomplete : "nope"
                        }
                    }
                }, {
                    element: "button",
                    type : "button",
                    text: "Register",
                    className : "button",
                    style : {
                        float : "right"
                    },
                    onClick : () => {
                        jsonClient.post(this.queryParams.link, {body : this.#user})
                            .then((response) => {
                                document.location.hash = "#/anjunar/welcome";
                            })
                    }
                }
            ]

        })
    }

}

export default customViews.define({
    name: "security-register",
    class: Register
})
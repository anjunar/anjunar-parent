import {builder, customComponents} from "../../../../../simplicity.js";
import DomInput from "../../../../../directives/dom-input.js";
import DomSelect from "../../../../../directives/dom-select.js";
import MatInputContainer from "../../../containers/mat-input-container.js";
import MatCheckboxContainer from "../../../containers/mat-checkbox-container.js";

export default class EditorNodeInspector extends HTMLElement {

    #node;

    #sizeX
    #sizeY
    #keep = true
    #ratio
    #float;

    #marginLeft
    #marginRight
    #marginTop
    #marginBottom;

    #paddingLeft
    #paddingRight
    #paddingTop
    #paddingBottom;

    get node() {
        return this.#node;
    }

    set node(value) {
        this.#node = value;
    }

    render() {

        let computedStyle = window.getComputedStyle(this.#node);

        let regex = /([\d.]+).*/

        this.#marginLeft = Number.parseInt(regex.exec(computedStyle.marginLeft)[1]);
        this.#marginRight = Number.parseInt(regex.exec(computedStyle.marginRight)[1]);
        this.#marginTop = Number.parseInt(regex.exec(computedStyle.marginTop)[1]);
        this.#marginBottom = Number.parseInt(regex.exec(computedStyle.marginBottom)[1]);

        this.#paddingLeft = Number.parseInt(regex.exec(computedStyle.paddingLeft)[1]);
        this.#paddingRight = Number.parseInt(regex.exec(computedStyle.paddingRight)[1]);
        this.#paddingTop = Number.parseInt(regex.exec(computedStyle.paddingTop)[1]);
        this.#paddingBottom = Number.parseInt(regex.exec(computedStyle.paddingBottom)[1]);

        this.#float = computedStyle.float || "none";

        this.#sizeX = Number.parseInt(regex.exec(computedStyle.width)[1]);
        this.#sizeY = Number.parseInt(regex.exec(computedStyle.height)[1]);

        this.#ratio = this.#sizeX / this.#sizeY;

        builder(this, {
            element: "div",
            children: [
                {
                    element: "div",
                    children: [
                        {
                            element: "div",
                            children: [
                                {
                                    element: MatInputContainer,
                                    placeholder: "Float",
                                    content: {
                                        element: DomSelect,
                                        value: {
                                            input : () => {
                                                return this.#float;
                                            },
                                            output : (value) => {
                                                this.#float = value;
                                            }
                                        },
                                        onChange : () => {
                                            if (this.#float === "none") {
                                                this.#node.style.float = ""
                                            } else {
                                                this.#node.style.float = this.#float;
                                            }
                                        },
                                        children: [
                                            {
                                                element: "option",
                                                value: "none",
                                                text: "Choose"
                                            },
                                            {
                                                element: "option",
                                                value: "right",
                                                text: "Right"
                                            },
                                            {
                                                element: "option",
                                                value: "left",
                                                text: "Left"
                                            }
                                        ]
                                    },
                                },
                                {
                                    element: "div",
                                    style: {
                                        display: "flex",
                                        alignItems : "center"
                                    },
                                    children: [
                                        {
                                            element: MatInputContainer,
                                            placeholder: "Width",
                                            style : {
                                                marginRight : "5px"
                                            },
                                            content: {
                                                element: DomInput,
                                                type: "number",
                                                value: {
                                                    input : () => {
                                                        return this.#sizeX;
                                                    },
                                                    output : (value) => {
                                                        this.#sizeX = value;
                                                    }
                                                },
                                                onChange : () => {
                                                    if (this.#keep) {
                                                        this.#sizeY = Math.round(this.#sizeX / this.#ratio);
                                                    }
                                                    this.#node.style.width = this.#sizeX + "px"
                                                    this.#node.style.height = this.#sizeY + "px"
                                                }
                                            },
                                        },
                                        {
                                            element: MatCheckboxContainer,
                                            placeholder: "Link",
                                            style : {
                                                marginRight : "5px"
                                            },
                                            content: {
                                                element: DomInput,
                                                type: "checkbox",
                                                style: {
                                                    width: "32px"
                                                },
                                                value: {
                                                    input : () => {
                                                        return this.#keep;
                                                    },
                                                    output : (value) => {
                                                        this.#keep = value;
                                                    }
                                                }
                                            },
                                        },
                                        {
                                            element: MatInputContainer,
                                            placeholder: "Height",
                                            content: {
                                                element: DomInput,
                                                type: "number",
                                                value: {
                                                    input : () => {
                                                        return this.#sizeY;
                                                    },
                                                    output : (value) => {
                                                        this.#sizeY = value;
                                                    }
                                                },
                                                onChange : () => {
                                                    if (this.#keep) {
                                                        this.#sizeX = Math.round(this.#sizeX * this.#ratio);
                                                    }
                                                    this.#node.style.width = this.#sizeX + "px"
                                                    this.#node.style.height = this.#sizeY + "px"
                                                }
                                            }

                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            element: "div",
                            style: {
                                display: "flex"
                            },
                            children: [
                                {
                                    element: MatInputContainer,
                                    placeholder: "Margin Left",
                                    style : {
                                        marginRight : "5px"
                                    },
                                    content: {
                                        element: DomInput,
                                        type: "number",
                                        value: {
                                            input : () => {
                                                return this.#marginLeft;
                                            },
                                            output : (value) => {
                                                this.#marginLeft = value;
                                            }
                                        },
                                        onChange : () => {
                                            this.#node.style.marginLeft = this.#marginLeft + "px";
                                        }
                                    }
                                },
                                {
                                    element: MatInputContainer,
                                    placeholder: "Margin Right",
                                    style : {
                                        marginRight : "5px"
                                    },
                                    content: {
                                        element: DomInput,
                                        type: "number",
                                        value: {
                                            input : () => {
                                                return this.#marginRight;
                                            },
                                            output : (value) => {
                                                this.#marginRight = value;
                                            }
                                        },
                                        onChange : () => {
                                            this.#node.style.marginRight = this.#marginRight + "px";
                                        }
                                    }
                                },
                                {
                                    element: MatInputContainer,
                                    placeholder: "Margin Top",
                                    style : {
                                        marginRight : "5px"
                                    },
                                    content: {
                                        element: DomInput,
                                        type: "number",
                                        value: {
                                            input : () => {
                                                return this.#marginTop;
                                            },
                                            output : (value) => {
                                                this.#marginTop = value;
                                            }
                                        },
                                        onChange : () => {
                                            this.#node.style.marginTop = this.#marginTop + "px";
                                        }
                                    }
                                },
                                {
                                    element: MatInputContainer,
                                    placeholder: "Margin Bottom",
                                    style : {
                                        marginRight : "5px"
                                    },
                                    content: {
                                        element: DomInput,
                                        type: "number",
                                        value: {
                                            input : () => {
                                                return this.#marginBottom;
                                            },
                                            output : (value) => {
                                                this.#marginBottom = value;
                                            }
                                        },
                                        onChange : () => {
                                            this.#node.style.marginBottom = this.#marginBottom + "px";
                                        }
                                    }
                                }
                            ]
                        },
                        {
                            element: "div",
                            style: {
                                display: "flex"
                            },
                            children: [
                                {
                                    element: MatInputContainer,
                                    placeholder: "Padding Left",
                                    style : {
                                        marginRight : "5px"
                                    },
                                    content: {
                                        element: DomInput,
                                        type: "number",
                                        value: {
                                            input : () => {
                                                return this.#paddingLeft;
                                            },
                                            output : (value) => {
                                                this.#paddingLeft = value;
                                            }
                                        },
                                        onChange : () => {
                                            this.#node.style.paddingLeft = this.#paddingLeft + "px";
                                        }
                                    }
                                },
                                {
                                    element: MatInputContainer,
                                    placeholder: "Padding Right",
                                    style : {
                                        marginRight : "5px"
                                    },
                                    content: {
                                        element: DomInput,
                                        type: "number",
                                        value: {
                                            input : () => {
                                                return this.#paddingRight;
                                            },
                                            output : (value) => {
                                                this.#paddingRight = value;
                                            }
                                        },
                                        onChange : () => {
                                            this.#node.style.paddingRight = this.#paddingRight + "px";
                                        }
                                    }
                                },
                                {
                                    element: MatInputContainer,
                                    placeholder: "Padding Top",
                                    style : {
                                        marginRight : "5px"
                                    },
                                    content: {
                                        element: DomInput,
                                        type: "number",
                                        value: {
                                            input : () => {
                                                return this.#paddingTop;
                                            },
                                            output : (value) => {
                                                this.#paddingTop = value;
                                            }
                                        },
                                        onChange : () => {
                                            this.#node.style.paddingTop = this.#paddingTop + "px";
                                        }
                                    }
                                },
                                {
                                    element: MatInputContainer,
                                    placeholder: "Padding Bottom",
                                    style : {
                                        marginRight : "5px"
                                    },
                                    content: {
                                        element: DomInput,
                                        type: "number",
                                        value: {
                                            input : () => {
                                                return this.#paddingBottom;
                                            },
                                            output : (value) => {
                                                this.#paddingBottom = value;
                                            }
                                        },
                                        onChange : () => {
                                            this.#node.style.paddingBottom = this.#paddingBottom + "px";
                                        }
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        })
    }

}

customComponents.define("editor-node-inspector", EditorNodeInspector);
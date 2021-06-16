import {builder, customComponents} from "../../../../simplicity.js";

export default class ToolbarJustify extends HTMLElement {

    #editor;

    #justify;
    #justifyLeft;
    #justifyRight;
    #justifyCenter;

    #indent;
    #outdent;
    #floatLeft;
    #floatRight;

    get editor() {
        return this.#editor;
    }

    set editor(value) {
        this.#editor = value;
    }

    render() {

        let justifyFullClick = () => {
            document.execCommand("justifyFull");

            this.#justify.classList.add("active")
            this.#justifyLeft.classList.remove("active")
            this.#justifyRight.classList.remove("active")
            this.#justifyCenter.classList.remove("active")
        }

        let justifyLeftClick = () => {
            document.execCommand("justifyLeft")

            this.#justify.classList.remove("active")
            this.#justifyLeft.classList.add("active")
            this.#justifyRight.classList.remove("active")
            this.#justifyCenter.classList.remove("active")
        }

        let justifyRightClick = () => {
            document.execCommand("justifyRight");

            this.#justify.classList.remove("active")
            this.#justifyLeft.classList.remove("active")
            this.#justifyRight.classList.add("active")
            this.#justifyCenter.classList.remove("active")
        }

        let justifyCenterClick = () => {
            document.execCommand("justifyCenter");

            this.#justify.classList.remove("active")
            this.#justifyLeft.classList.remove("active")
            this.#justifyRight.classList.remove("active")
            this.#justifyCenter.classList.add("active")
        }

        let indentClick = () => {
            document.execCommand("indent");
        }

        let outdentClick = () => {
            document.execCommand("outdent");
        }

        let floatLeftClick = () => {
            let selection = window.getSelection();
            let parentElement = selection.anchorNode;
            if (parentElement.nodeType === 3) {
                parentElement = parentElement.parentElement;
            }

            let computedStyle = window.getComputedStyle(parentElement);

            if (computedStyle.float === "left") {
                parentElement.style.float = "";
            } else {
                parentElement.style.float = "left";
            }



            let float = computedStyle.float
            switch (float) {
                case "left" : {
                    this.#floatLeft.classList.add("active")
                    this.#floatRight.classList.remove("active")
                } break
                case "right" : {
                    this.#floatLeft.classList.remove("active")
                    this.#floatRight.classList.add("active")
                } break;
                default : {
                    this.#floatLeft.classList.remove("active")
                    this.#floatRight.classList.remove("active")
                }
            }
        }

        let floatRightClick = () => {
            let selection = window.getSelection();
            let parentElement = selection.anchorNode;
            if (parentElement.nodeType === 3) {
                parentElement = parentElement.parentElement;
            }

            let computedStyle = window.getComputedStyle(parentElement);

            if (computedStyle.float === "right") {
                parentElement.style.float = "";
            } else {
                parentElement.style.float = "right";
            }


            let float = computedStyle.float
            switch (float) {
                case "left" : {
                    this.#floatLeft.classList.add("active")
                    this.#floatRight.classList.remove("active")
                } break
                case "right" : {
                    this.#floatLeft.classList.remove("active")
                    this.#floatRight.classList.add("active")
                } break;
                default : {
                    this.#floatLeft.classList.remove("active")
                    this.#floatRight.classList.remove("active")
                }
            }
        }

        let content = this.#editor.querySelector("div[contenteditable]")

        content.addEventListener("click", (event) => {
            let computedStyle = window.getComputedStyle(event.target);

            let textAlign = computedStyle.textAlign;
            if (textAlign === "justify") {
                this.#justify.classList.add("active");
            } else {
                this.#justify.classList.remove("active");
            }

            if (textAlign === "left") {
                this.#justifyLeft.classList.add("active");
            } else {
                this.#justifyLeft.classList.remove("active");
            }

            if (textAlign === "right") {
                this.#justifyRight.classList.add("active");
            } else {
                this.#justifyRight.classList.remove("active");
            }

            if (textAlign === "center") {
                this.#justifyCenter.classList.add("active");
            } else {
                this.#justifyCenter.classList.remove("active");
            }

            let float = computedStyle.float
            switch (float) {
                case "left" : {
                    this.#floatLeft.classList.add("active")
                    this.#floatRight.classList.remove("active")
                } break
                case "right" : {
                    this.#floatLeft.classList.remove("active")
                    this.#floatRight.classList.add("active")
                } break;
                default : {
                    this.#floatLeft.classList.remove("active")
                    this.#floatRight.classList.remove("active")
                }
            }

        })


        builder(this, [
            {
                element : "div",
                style : {
                    display : "flex"
                },
                children : [
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Justify Full",
                        text : "format_align_justify",
                        onClick : () => {
                            justifyFullClick();
                        },
                        initialize : (element) => {
                            this.#justify = element;
                        }
                    },
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Justify Left",
                        text : "format_align_left",
                        onClick : () => {
                            justifyLeftClick();
                        },
                        initialize : (element) => {
                            this.#justifyLeft = element;
                        }
                    },
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Justify Full",
                        text : "format_align_right",
                        onClick : () => {
                            justifyRightClick();
                        },
                        initialize : (element) => {
                            this.#justifyRight = element;
                        }
                    },
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Justify Center",
                        text : "format_align_center",
                        onClick : () => {
                            justifyCenterClick();
                        },
                        initialize : (element) => {
                            this.#justifyCenter = element;
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
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Indent",
                        text : "format_indent_increase",
                        onClick : () => {
                            indentClick();
                        },
                        initialize : (element) => {
                            this.#indent = element;
                        }
                    },
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Outdent",
                        text : "format_indent_decrease",
                        onClick : () => {
                            outdentClick();
                        },
                        initialize : (element) => {
                            this.#outdent = element;
                        }
                    },
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Float Left",
                        text : "view_sidebar",
                        onClick : () => {
                            floatLeftClick();
                        },
                        initialize : (element) => {
                            this.#floatLeft = element;
                        }
                    },
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Float Right",
                        text : "vertical_split",
                        onClick : () => {
                            floatRightClick();
                        },
                        initialize : (element) => {
                            this.#floatRight = element;
                        }
                    }
                ]
            }
        ])
    }

}

customComponents.define("toolbar-justify", ToolbarJustify)
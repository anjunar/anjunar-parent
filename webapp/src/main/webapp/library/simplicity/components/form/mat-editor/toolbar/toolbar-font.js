import {builder, customComponents} from "../../../../simplicity.js";
import DomSelect from "../../../../directives/dom-select.js";
import {i18nFactory} from "../../../../services/i18nResolver.js";

export default class ToolbarFont extends HTMLElement {

    #editor;

    #fontName = "none";
    #fontSize = "none";
    #formatBlock = "none";

    #bold;
    #italic;
    #strikeThrough;
    #subScript;
    #superScript;

    get editor() {
        return this.#editor;
    }

    set editor(value) {
        this.#editor = value;
    }

    render() {

        function fontSizeTranslate(number) {
            switch (number) {
                case 9 : return "0"
                case 10 : return "1"
                case 13 : return "2"
                case 16 : return "3"
                case 18 : return "4"
                case 32 : return "5"
                default : return "none";
            }
        }

        function formatBlockRestriction(localName) {
            switch (localName) {
                case "h1" : return "H1"
                case "h2" : return "H2"
                case "h3" : return "H3"
                case "h4" : return "H4"
                case "h5" : return "H5"
                case "h6" : return "H6"
                default : return "none";
            }
        }

        let fontAdjustUpper = (computedStyle, event) => {
            let regex = /(\d+).*/

            this.#fontName = computedStyle.fontFamily.replace(/"/g, "") || "none";
            let number = Number.parseInt(regex.exec(computedStyle.fontSize)[1]);
            this.#fontSize = fontSizeTranslate(number);
            this.#formatBlock = formatBlockRestriction(event.target.localName);
        }

        let fontAdjustLower = (computedStyle) => {
            if (computedStyle.fontWeight === "700") {
                this.#bold.classList.add("active");
            } else {
                this.#bold.classList.remove("active");
            }

            if (computedStyle.fontStyle === "italic") {
                this.#italic.classList.add("active");
            } else {
                this.#italic.classList.remove("active");
            }

            if (computedStyle.textDecorationLine === "line-through") {
                this.#strikeThrough.classList.add("active");
            } else {
                this.#strikeThrough.classList.remove("active");
            }

            if (computedStyle.verticalAlign === "sub") {
                this.#subScript.classList.add("active");
            } else {
                this.#subScript.classList.remove("active");
            }

            if (computedStyle.verticalAlign === "super") {
                this.#superScript.classList.add("active");
            } else {
                this.#superScript.classList.remove("active");
            }
        }

        function fontUpperClick(event) {
            let selection = window.getSelection();
            let parentElement = selection.anchorNode.parentElement;
            let computedStyle = window.getComputedStyle(parentElement);
            fontAdjustUpper(computedStyle, event);
        }

        function fontLowerClick() {
            let selection = window.getSelection();
            let parentElement = selection.anchorNode.parentElement;
            let computedStyle = window.getComputedStyle(parentElement);
            fontAdjustLower(computedStyle);
        }


        function fontNameClick(event) {
            document.execCommand("fontname", false, event.target.value);
            fontUpperClick(event)
        }

        function fontSizeClick(event) {
            document.execCommand("fontSize", false, event.target.value);
            fontUpperClick(event)
        }

        function formatBlockClick(event) {
            document.execCommand("formatBlock", false, event.target.value)
            fontUpperClick(event)
        }




        function boldClick() {
            document.execCommand("styleWithCSS", false, true);
            document.execCommand('bold', false, null)
            fontLowerClick();
        }

        function italicClick() {
            document.execCommand("styleWithCSS", false, true);
            document.execCommand("italic", false, null);
            fontLowerClick();
        }

        function strikethroughClick() {
            document.execCommand("styleWithCSS", false, true);
            document.execCommand("strikethrough", false, null)
            fontLowerClick();
        }

        function subscriptClick() {
            document.execCommand("styleWithCSS", false, true);
            document.execCommand("subscript", false, null)
            fontLowerClick();
        }

        function superscriptClick() {
            document.execCommand("styleWithCSS", false, true);
            document.execCommand("superscript", false, null)
            fontLowerClick();
        }

        let content = this.#editor.querySelector("div[contenteditable]");

        content.addEventListener("click", (event) => {
            let computedStyle = window.getComputedStyle(event.target);

            fontAdjustUpper(computedStyle, event);
            fontAdjustLower(computedStyle);

        })

        builder(this, [
            {
                element: "div",
                style: {
                    display: "flex"
                },
                children: [
                    {
                        element: "div",
                        children : [
                            {
                                element : DomSelect,
                                style : {
                                    margin : "5px"
                                },
                                value : {
                                    input : () => {
                                        return this.#fontName;
                                    },
                                    output : (value) => {
                                        this.#fontName = value;
                                    }
                                },
                                onChange : (event) => {
                                    fontNameClick(event);
                                },
                                children : [
                                    {
                                        element : "option",
                                        value : "none",
                                        text : i18n("Choose")
                                    },
                                    {
                                        element : "option",
                                        value : "Georgia, serif",
                                        text : "Georgia"
                                    },
                                    {
                                        element : "option",
                                        value : "Palatino Linotype, serif",
                                        text : "Palatino"
                                    },
                                    {
                                        element : "option",
                                        value : "Times New Roman, serif",
                                        text : "Times new Roman"
                                    },
                                    {
                                        element : "option",
                                        value : "Arial, serif",
                                        text : "Arial"
                                    },
                                    {
                                        element : "option",
                                        value : "Comic Sans MS, serif",
                                        text : "Comic Sans MS"
                                    },
                                    {
                                        element : "option",
                                        value : "Helvetica, serif",
                                        text : "Helvetica"
                                    },
                                    {
                                        element : "option",
                                        value : "Impact, serif",
                                        text : "Impact"
                                    },
                                    {
                                        element : "option",
                                        value : "Lucida, serif",
                                        text : "Lucida"
                                    },
                                    {
                                        element : "option",
                                        value : "Tahoma, serif",
                                        text : "Tahoma"
                                    },
                                    {
                                        element : "option",
                                        value : "Trebuchet MS, serif",
                                        text : "Trebuchet"
                                    },
                                    {
                                        element : "option",
                                        value : "Verdana, serif",
                                        text : "Verdana"
                                    },
                                    {
                                        element : "option",
                                        value : "Courier New, serif",
                                        text : "Couria New"
                                    },
                                    {
                                        element : "option",
                                        value : "Lucida Console, serif",
                                        text : "Lucida"
                                    }
                                ]
                            },
                            {
                                element: "br"
                            },
                            {
                                element : "button",
                                type : "button",
                                className : "iconBig",
                                title : "Bold",
                                text : "format_bold",
                                initialize : (element) => {
                                    this.#bold = element;
                                },
                                onClick : () => {
                                    boldClick();
                                }
                            },
                            {
                                element : "button",
                                type : "button",
                                className : "iconBig",
                                title : "Italic",
                                text : "format_italic",
                                initialize : (element) => {
                                    this.#italic = element;
                                },
                                onClick : () => {
                                    italicClick();
                                }
                            },
                            {
                                element : "button",
                                type : "button",
                                className : "iconBig",
                                title : "Strikethrough",
                                text : "strikethrough_s",
                                initialize : (element) => {
                                    this.#strikeThrough = element;
                                },
                                onClick : () => {
                                    strikethroughClick();
                                }
                            },
                            {
                                element : "button",
                                type : "button",
                                className : "iconBig",
                                title : "SubScript",
                                text : "subscript",
                                initialize : (element) => {
                                    this.#subScript = element;
                                },
                                onClick : () => {
                                    subscriptClick();
                                }
                            },
                            {
                                element : "button",
                                type : "button",
                                className : "iconBig",
                                title : "SuperScript",
                                text : "superscript",
                                initialize : (element) => {
                                    this.#superScript = element;
                                },
                                onClick : () => {
                                    superscriptClick();
                                }
                            },
                        ]
                    },
                    {
                        element: "div",
                        children: [
                            {
                                element : DomSelect,
                                style : {
                                    margin : "5px"
                                },
                                value : {
                                    input : () => {
                                        return this.#fontSize;
                                    },
                                    output : (value) => {
                                        this.#fontSize = value;
                                    }
                                },
                                onChange : (event) => {
                                    fontSizeClick(event);
                                },
                                children: [
                                    {
                                        element: "option",
                                        value : "none",
                                        text : i18n("Choose")
                                    },
                                    {
                                        element: "option",
                                        value : "1",
                                        text : "x-small"
                                    },
                                    {
                                        element: "option",
                                        value : "2",
                                        text : "small"
                                    },
                                    {
                                        element: "option",
                                        value : "3",
                                        text : "medium"
                                    },
                                    {
                                        element: "option",
                                        value : "4",
                                        text : "large"
                                    },
                                    {
                                        element: "option",
                                        value : "5",
                                        text : "x-large"
                                    },
                                    {
                                        element: "option",
                                        value : "6",
                                        text : "xx-large"
                                    }
                                ]
                            },
                            {
                                element: "br"
                            },
                            {
                                element: DomSelect,
                                style : {
                                    margin : "5px"
                                },
                                value : {
                                    input : () => {
                                        return this.#formatBlock;
                                    },
                                    output : (value)  => {
                                        this.#formatBlock = value;
                                    }
                                },
                                onChange : (event) => {
                                    formatBlockClick(event);
                                },
                                children : [
                                    {
                                        element: "option",
                                        value : "none",
                                        text : i18n("Choose")
                                    },
                                    {
                                        element: "option",
                                        value : "H1",
                                        text : "H1"
                                    },
                                    {
                                        element: "option",
                                        value : "H2",
                                        text : "H2"
                                    },
                                    {
                                        element: "option",
                                        value : "H3",
                                        text : "H3"
                                    },
                                    {
                                        element: "option",
                                        value : "H4",
                                        text : "H4"
                                    },
                                    {
                                        element: "option",
                                        value : "H5",
                                        text : "H5"
                                    },
                                    {
                                        element: "option",
                                        value : "H6",
                                        text : "H6"
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ])
    }

}

customComponents.define("toolbar-font", ToolbarFont);

const i18n = i18nFactory({
    "Choose" : {
        "en-DE" : "Choose",
        "de-DE" : "Auswählen"
    }
});

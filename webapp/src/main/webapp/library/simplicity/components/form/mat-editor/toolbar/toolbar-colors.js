import {builder, customComponents} from "../../../../simplicity.js";
import DomSelect from "../../../../directives/dom-select.js";
import {i18nFactory} from "../../../../services/i18nResolver.js";

export default class ToolbarColors extends HTMLElement {

    #editor;
    #color = "none";
    #backgroundColor = "none";

    get editor() {
        return this.#editor;
    }

    set editor(value) {
        this.#editor = value;
    }

    render() {

        let content = this.#editor.querySelector("div[contenteditable]");

        content.addEventListener("click", (event) => {
            let rgbToHex = (color) => {
                color = ""+ color;
                if (!color || color.indexOf("rgb") < 0) {
                    return;
                }

                if (color.charAt(0) === "#") {
                    return color;
                }

                let nums = /(.*?)rgb\((\d+),\s*(\d+),\s*(\d+)\)/i.exec(color),
                    r = parseInt(nums[2], 10).toString(16),
                    g = parseInt(nums[3], 10).toString(16),
                    b = parseInt(nums[4], 10).toString(16);

                return "#"+ (
                    (r.length === 1 ? "0"+ r : r) +
                    (g.length === 1 ? "0"+ g : g) +
                    (b.length === 1 ? "0"+ b : b)
                );
            }

            let computedStyle = window.getComputedStyle(event.target);

            this.#color = rgbToHex(computedStyle.color) || "none";
            this.#backgroundColor = computedStyle.backgroundColor || "none";
        })

        builder(this, [
            {
                element : DomSelect,
                style : {
                    margin : "5px"
                },
                value : {
                    input : () => {
                        return this.#color;
                    },
                    output : (value) => {
                        this.#color = value;
                    }
                },
                onChange : (event) => {
                    document.execCommand("styleWithCSS", false, true);
                    document.execCommand("foreColor", false, event.target.value)
                },
                children : [
                    {
                        element : "option",
                        value : "none",
                        text : i18n("Choose")
                    },
                    {
                        element : "option",
                        value : "#dddddd",
                        style : {
                            color : "#dddddd"
                        },
                        text : "Font"
                    },
                    {
                        element : "option",
                        value : "#3c3f41",
                        style : {
                            color : "#3c3f41"
                        },
                        text : "Normal"
                    },
                    {
                        element : "option",
                        value : "#ff463f",
                        style : {
                            color : "#ff463f"
                        },
                        text : "Error"
                    },
                    {
                        element : "option",
                        value : "#aaaaaa",
                        style : {
                            color : "#aaaaaa"
                        },
                        text : "Grey"
                    },
                    {
                        element : "option",
                        value : "#2b2b2b",
                        style : {
                            color : "#2b2b2b"
                        },
                        text : "Background"
                    },
                    {
                        element : "option",
                        value : "#a4c35b",
                        style : {
                            color : "#a4c35b"
                        },
                        text : "Selected"
                    }
                ]
            },
            {
                element : "label",
                text : i18n("Font Color")
            },
            {
                element : "br"
            },
            {
                element : DomSelect,
                style : {
                    margin : "5px"
                },
                value : {
                    input : () => {
                        return this.#backgroundColor;
                    },
                    output : (value) => {
                        this.#backgroundColor = value;
                    }
                },
                onChange : (event) => {
                    document.execCommand("styleWithCSS", false, true);
                    document.execCommand("backColor", false, event.target.value);
                },
                children : [
                    {
                        element : "option",
                        value : "none",
                        text : i18n("Choose")
                    },
                    {
                        element : "option",
                        value : "rgb(221, 221, 221)",
                        style : {
                            color : "#dddddd"
                        },
                        text : "Font"
                    },
                    {
                        element : "option",
                        value : "rgb(60, 63, 65)",
                        style : {
                            color : "#3c3f41"
                        },
                        text : "Normal"
                    },
                    {
                        element : "option",
                        value : "rgb(255, 70, 63)",
                        style : {
                            color : "#ff463f"
                        },
                        text : "Error"
                    },
                    {
                        element : "option",
                        value : "rgb(170, 170, 170)",
                        style : {
                            color : "#aaaaaa"
                        },
                        text : "Grey"
                    },
                    {
                        element : "option",
                        value : "rgb(43, 43, 43)",
                        style : {
                            color : "#2b2b2b"
                        },
                        text : "Background"
                    },
                    {
                        element : "option",
                        value : "rgb(164, 195, 91)",
                        style : {
                            color : "#a4c35b"
                        },
                        text : "Selected"
                    }
                ]
            },
            {
                element : "label",
                text : i18n("Back Color")
            }

        ])
    }

}

customComponents.define("toolbar-colors", ToolbarColors)

const i18n = i18nFactory({
    "Choose" : {
        "en-DE" : "Choose",
        "de-DE" : "Auswählen"
    },
    "Font Color" : {
        "en-DE" : "Font Color",
        "de-DE" : "Font Farbe"
    },
    "Back Color" : {
        "en-DE" : "Back Color",
        "de-DE" : "Hin. Farbe"
    }
});

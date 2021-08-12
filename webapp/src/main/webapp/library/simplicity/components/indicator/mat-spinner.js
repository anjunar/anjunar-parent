import {builder, customComponents} from "../../simplicity.js";

class MatSpinner extends HTMLElement {

    render() {
        builder(this, {
            element: "svg",
            attributes: {
                "width": "65px",
                "height": "65px",
                "viewBox": "0 0 66 66",
                "class" : "spinner"
            },
            children: [
                {
                    element: "circle",
                    attributes: {
                        "fill": "none",
                        "stroke-width": "6",
                        "stroke-linecap": "round",
                        "cx": "33",
                        "cy": "33",
                        "r": "30",
                        "class" : "path"
                    }
                }
            ]
        })
    }

}

export default customComponents.define("mat-spinner", MatSpinner)
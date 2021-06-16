import {builder, customComponents} from "../../simplicity.js";

export default class MatProgress extends HTMLElement {

    render() {

        this.style.display = "block"

        builder(this, {
            element : "div",
            style : {
                position: "relative",
                height: "4px",
                display: "block",
                width: "100%",
                backgroundColor: "var(--main-normal-color)",
                borderRadius: "2px",
                backgroundClip: "padding-box",
                overflow: "hidden"
            },
            children : [
                {
                    element : "div",
                    style: {
                        backgroundColor: "var(--main-selected-color)"
                    },
                    className : "indeterminate"
                }
            ]
        })
    }

}

customComponents.define("mat-progress", MatProgress)
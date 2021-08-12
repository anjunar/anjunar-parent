import {builder, customComponents} from "../../simplicity.js";

export default class MatScrollbarHorizontal extends HTMLElement {

    #position;

    get position() {
        return this.#position;
    }

    set position(value) {
        this.#position = value;
    }

    show() {
        let element = this.querySelector("div.cursor");
        element.style.display = "block";
    }

    hide() {
        let element = this.querySelector("div.cursor");
        element.style.display = "none";
    }

    render() {

        this.style.display = "block";
        this.style.width = "100%";

        let sliderHorizontal = (element) => {
            let delta = element.offsetLeft, pointer = element.offsetLeft;

            let elementDrag = (event) => {
                event.preventDefault();
                delta = pointer - event.clientX;
                pointer = event.clientX;
                let computedStyle = Number.parseInt(window.getComputedStyle(element).left.replace("px", ""));
                let number = computedStyle - delta;
                if (number < 0) {
                    number = 0;
                }
                if (number > this.offsetWidth - 16) {
                    number = this.offsetWidth - 16;
                }
                this.#position = number / (this.offsetWidth - 16);
                element.style.left = number + "px";
                this.dispatchEvent(new CustomEvent("scroll"));
            }

            let closeDragElement = () => {
                document.onmouseup = null;
                document.onmousemove = null;
            }

            return (event) => {
                event.preventDefault();
                pointer = event.clientY;
                document.onmouseup = closeDragElement;
                document.onmousemove = elementDrag;
            };
        }



        builder(this, {
            element : "div",
            style : {
                position: "relative",
                height : "16px",
                width : "100%",
                backgroundColor: "var(--main-dark2-color)",
            },
            children : [
                {
                    element : "div",
                    className : "cursor",
                    style : {
                        position : "absolute",
                        height: "16px",
                        width:  "16px",
                        backgroundColor : "var(--main-dark1-color)"
                    },
                    initialize : (element) => {
                        let vertical = sliderHorizontal(element);
                        element.addEventListener("mousedown", vertical);
                    }
                }
            ]
        })

    }

}

customComponents.define("mat-scrollbar-horizontal", MatScrollbarHorizontal)
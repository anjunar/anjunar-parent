import {builder, customComponents} from "../../simplicity.js";

export default class MatScrollbarVertical extends HTMLElement {

    #position;

    get position() {
        return this.#position;
    }

    set position(value) {
        this.#position = value;
        let element = this.querySelector("div.cursor");
        let number = (this.offsetHeight - 16) * value;
        element.style.top = number + "px";
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
        this.style.height = "100%";

        let sliderVertical = (element) => {
            let delta = element.offsetTop, pointer = element.offsetTop;

            let elementDrag = (event) => {
                event.preventDefault();
                delta = pointer - event.clientY;
                pointer = event.clientY;
                let computedStyle = Number.parseInt(window.getComputedStyle(element).top.replace("px", ""));
                let number = computedStyle - delta;
                if (number < 0) {
                    number = 0;
                }
                if (number > this.offsetHeight - 16) {
                    number = this.offsetHeight - 16;
                }
                this.#position = number / (this.offsetHeight - 16);
                element.style.top = number + "px";
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
                height : "100%",
                width : "16px",
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
                        let vertical = sliderVertical(element);
                        element.addEventListener("mousedown", vertical);
                    }
                }
            ]
        })

    }

}

customComponents.define("mat-scrollbar-vertical", MatScrollbarVertical)
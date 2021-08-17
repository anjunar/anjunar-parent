import {builder, customComponents, HTMLWindow, HTMLWindowLazy} from "../simplicity.js";
import MatScrollbarVertical from "../components/navigation/mat-scrollbar-vertical.js";
import MatScrollbarHorizontal from "../components/navigation/mat-scrollbar-horizontal.js";

export default class DomWindow extends HTMLElement {

    #content;
    #scrollX;
    #scrollY;

    get content() {
        return this.#content;
    }

    set content(value) {
        this.#content = value;
    }

    get scrollX() {
        return this.#scrollX;
    }

    get scrollY() {
        return this.#scrollY;
    }

    checkScrollBars() {
        let contentDiv = this.querySelector("div.content");
        let clientOffsetHeight = this.#content.offsetHeight - contentDiv.offsetHeight;
        let clientOffsetWidth = this.#content.offsetWidth - contentDiv.offsetWidth;
        let matScrollBarHorizontal = this.querySelector("mat-scrollbar-horizontal");
        let matScrollBarVertical = this.querySelector("mat-scrollbar-vertical");

        if (clientOffsetHeight <= 0) {
            matScrollBarVertical.hide();
        } else {
            matScrollBarVertical.show();
        }

        if (clientOffsetWidth <= 0) {
            matScrollBarHorizontal.hide();
        } else {
            matScrollBarHorizontal.show();
        }
    }

    render() {

        this.style.display = "block";

        let windowProcess = (element) => {
            this.addEventListener("scroll", (event) => {
                let clientOffsetHeight = this.#content.offsetHeight - element.offsetHeight + 16;
                let clientOffsetWidth = this.#content.offsetWidth - element.offsetWidth + 16;
                let top = clientOffsetHeight * (event.target.scrollY || 0);
                let left = clientOffsetWidth * (event.target.scrollX || 0);

                this.#content.style.transform = `translate3d(${- left}px, ${- top}px, 0px)`
            });

            function getMatrix(element) {
                if (element.style.transform === "") {
                    return {
                        x : 0,
                        y : 0,
                        z : 0
                    }
                }

                let regex = /translate3d\((-?[\d.]+)px,\s*(-?[\d.]+)px,\s*(-?[\d.]+)px\)/;
                let transform = regex.exec(element.style.transform);
                return {
                    x: Number.parseInt(transform[1]),
                    y: Number.parseInt(transform[2]),
                    z: Number.parseInt(transform[3])
                };
            }

            this.addEventListener("wheel", (event) => {
                let matrix = getMatrix(this.#content);
                let top = - matrix.y + event.deltaY;
                let clientOffsetHeight = this.#content.offsetHeight - element.offsetHeight + 16;
                if (clientOffsetHeight > 0) {
                    if (top < 0) {
                        top = 0;
                    }
                    if (top > clientOffsetHeight) {
                        top = clientOffsetHeight;
                    }
                    let position = top / clientOffsetHeight;
                    let matScrollbarVertical = this.querySelector("mat-scrollbar-vertical");
                    matScrollbarVertical.position = position;

                    this.#content.style.transform = `translate3d(0px, ${- top}px, 0px)`
                }
            })
        }

        builder(this, [{
            element: "div",
            style: {
                display: "flex",
                height: "calc(100% - 1px)"
            },
            children: [
                {
                    element: "div",
                    style: {
                        paddingTop: "16px",
                        paddingLeft: "16px",
                        width : "calc(100% - 32px)"
                    },
                    children : [
                        {
                            element : "div",
                            className : "content",
                            style : {
                                overflow : "hidden",
                                height: "100%",
                            },
                            initialize: (element) => {
                                element.appendChild(this.#content);
                                this.#content.style.transition = "all .5s cubic-bezier(0.2, .84, .5, 1)"
                                if (this.#content instanceof HTMLWindow) {
                                    windowProcess(element);
                                }
                                if (this.#content instanceof HTMLWindowLazy) {
                                    windowProcess(element);
                                }
                            }
                        }
                    ]
                }, {
                    element: MatScrollbarVertical,
                    onScroll: (event) => {
                        this.#scrollY = event.target.position;
                        this.dispatchEvent(new CustomEvent("scroll"))
                    }
                }
            ]
        }, {
            element: MatScrollbarHorizontal,
            onScroll: (event) => {
                this.#scrollX = event.target.position;
                this.dispatchEvent(new CustomEvent("scroll"))
            }
        }])

    }

}

customComponents.define("dom-window", DomWindow)
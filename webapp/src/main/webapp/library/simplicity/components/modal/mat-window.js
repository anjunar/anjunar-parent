import {builder, customComponents} from "../../simplicity.js";
import {windowManager} from "../../services/window-manager.js";

export default class MatWindow extends HTMLElement {

    #content;
    #header;
    #resizable;

    constructor() {
        super();

        this.addEventListener("click", () => {
            windowManager.clickWindow(this);
        })
    }

    get content() {
        return this.#content
    }

    set content(value) {
        this.#content = value;
    }

    get header() {
        return this.#header;
    }

    set header(value) {
        this.#header = value;
    }

    get resizable() {
        return this.#resizable;
    }

    set resizable(value) {
        this.#resizable = value;
    }

    render() {

        this.style.position = "absolute";
        this.style.border = "1px solid var(--main-normal-color)";
        this.style.backgroundColor = "var(--main-dimmed-color)";
        this.style.display = "block";

        let dragElement = (element) => {
            let deltaX = 0, deltaY = 0, pointerX = 0, pointerY = 0;

            let elementDrag = (e) => {
                e.preventDefault();
                deltaX = pointerX - e.clientX;
                deltaY = pointerY - e.clientY;
                pointerX = e.clientX;
                pointerY = e.clientY;
                element.style.top = (element.offsetTop - deltaY) + "px";
                element.style.left = (element.offsetLeft - deltaX) + "px";
                this.dispatchEvent(new CustomEvent("windowDrag", {detail: this}));
            }

            let closeDragElement = () => {
                document.onmouseup = null;
                document.onmousemove = null;
                this.dispatchEvent(new CustomEvent("windowEndDrag", {detail: this}));
            }

            let dragMouseDown = (e) => {
                e.preventDefault();
                pointerX = e.clientX;
                pointerY = e.clientY;
                document.onmouseup = closeDragElement;
                document.onmousemove = elementDrag;
                this.dispatchEvent(new CustomEvent("windowStartDrag", {detail: this}));
            }

            return dragMouseDown
        }

        let nResizeTop = (element) => {
            let delta = element.offsetTop, pointer = element.offsetTop;

            let elementDrag = (event) => {
                event.preventDefault();
                delta = pointer - event.clientY;
                pointer = event.clientY;
                element.style.height = ((element.offsetHeight - 2) + delta) + "px";
                element.style.top = (element.offsetTop - delta) + "px";
                this.dispatchEvent(new CustomEvent("windowResize", {detail: this}));
            }

            let closeDragElement = () => {
                document.onmouseup = null;
                document.onmousemove = null;
                this.dispatchEvent(new CustomEvent("windowEndResize", {detail: this}));
            }

            let dragMouseDown = (event) => {
                if (this.#resizable) {
                    event.preventDefault();
                    pointer = event.clientY;
                    document.onmouseup = closeDragElement;
                    document.onmousemove = elementDrag;
                    this.dispatchEvent(new CustomEvent("windowStartResize", {detail: this}));
                }
            }

            return dragMouseDown
        }

        let seResize = (element) => {
            let deltaY = element.offsetTop, pointerY = element.offsetTop;
            let deltaX = element.offsetLeft, pointerX = element.offsetLeft;

            let elementDrag = (event) => {
                event.preventDefault();
                deltaY = pointerY - event.clientY;
                pointerY = event.clientY;
                element.style.height = ((element.offsetHeight - 2) + deltaY) + "px";
                element.style.top = (element.offsetTop - deltaY) + "px";

                deltaX = pointerX - event.clientX;
                pointerX = event.clientX;
                element.style.width = ((element.offsetWidth - 2) + deltaX) + "px";
                element.style.left = (element.offsetLeft - deltaX) + "px";
                this.dispatchEvent(new CustomEvent("windowResize", {detail: this}));
            }

            let closeDragElement = () => {
                document.onmouseup = null;
                document.onmousemove = null;
                this.dispatchEvent(new CustomEvent("windowEndResize", {detail: this}));
            }

            let dragMouseDown = (event) => {
                if (this.#resizable) {
                    event.preventDefault();
                    pointerY = event.clientY;
                    pointerX = event.clientX;
                    document.onmouseup = closeDragElement;
                    document.onmousemove = elementDrag;
                    this.dispatchEvent(new CustomEvent("windowStartResize", {detail: this}));
                }
            }

            return dragMouseDown;
        }

        let eResizeLeft = (element) => {
            let delta = element.offsetLeft, pointer = element.offsetLeft;

            let elementDrag = (event) => {
                event.preventDefault();
                delta = pointer - event.clientX;
                pointer = event.clientX;
                element.style.width = ((element.offsetWidth - 2) + delta) + "px";
                element.style.left = (element.offsetLeft - delta) + "px";
                this.dispatchEvent(new CustomEvent("windowResize", {detail: this}));
            }

            let closeDragElement = () => {
                document.onmouseup = null;
                document.onmousemove = null;
                this.dispatchEvent(new CustomEvent("windowEndResize", {detail: this}));
            }

            let dragMouseDown = (event) => {
                if (this.#resizable) {
                    event.preventDefault();
                    pointer = event.clientX;
                    document.onmouseup = closeDragElement;
                    document.onmousemove = elementDrag;
                    this.dispatchEvent(new CustomEvent("windowStartResize", {detail: this}));
                }
            }

            return dragMouseDown;
        }

        let neResize = (element) => {
            let deltaY = element.offsetTop, pointerY = element.offsetTop;
            let deltaX = element.offsetLeft, pointerX = element.offsetLeft;

            let elementDrag = (event) => {
                event.preventDefault();
                deltaY = pointerY - event.clientY;
                pointerY = event.clientY;
                element.style.height = ((element.offsetHeight - 2) - deltaY) + "px";
                element.style.bottom = ((element.offsetTop + (element.offsetHeight - 2)) - deltaY) + "px";

                deltaX = pointerX - event.clientX;
                pointerX = event.clientX;
                element.style.width = ((element.offsetWidth - 2) + deltaX) + "px";
                element.style.left = (element.offsetLeft - deltaX) + "px";
                this.dispatchEvent(new CustomEvent("windowResize", {detail: this}));
            }

            let closeDragElement = () => {
                document.onmouseup = null;
                document.onmousemove = null;
                this.dispatchEvent(new CustomEvent("windowEndResize", {detail: this}));
            }

            let dragMouseDown = (event) => {
                if (this.#resizable) {
                    event.preventDefault();
                    pointerY = event.clientY;
                    pointerX = event.clientX;
                    document.onmouseup = closeDragElement;
                    document.onmousemove = elementDrag;
                    this.dispatchEvent(new CustomEvent("windowStartResize", {detail: this}));
                }
            }

            return dragMouseDown;
        }

        let swResize = (element) => {
            let deltaY = element.offsetTop, pointerY = element.offsetTop;
            let deltaX = element.offsetLeft, pointerX = element.offsetLeft;

            let elementDrag = (event) => {
                event.preventDefault();
                deltaY = pointerY - event.clientY;
                pointerY = event.clientY;
                element.style.height = ((element.offsetHeight - 2) + deltaY) + "px";
                element.style.top = (element.offsetTop - deltaY) + "px";

                deltaX = pointerX - event.clientX;
                pointerX = event.clientX;
                element.style.width = ((element.offsetWidth - 2) - deltaX) + "px";
                element.style.right = ((element.offsetLeft + (element.offsetWidth - 2)) - deltaX) + "px";
                this.dispatchEvent(new CustomEvent("windowResize", {detail: this}));
            }

            let closeDragElement = () => {
                document.onmouseup = null;
                document.onmousemove = null;
                this.dispatchEvent(new CustomEvent("windowEndResize", {detail: this}));
            }

            let dragMouseDown = (event) => {
                if (this.#resizable) {
                    event.preventDefault();
                    pointerY = event.clientY;
                    pointerX = event.clientX;
                    document.onmouseup = closeDragElement;
                    document.onmousemove = elementDrag;
                    this.dispatchEvent(new CustomEvent("windowStartResize", {detail: this}));
                }
            }

            return dragMouseDown;
        }

        let eResizeRight = (element) => {
            let delta = element.offsetLeft, pointer = element.offsetLeft;

            let elementDrag = (event) => {
                event.preventDefault();
                delta = pointer - event.clientX;
                pointer = event.clientX;
                element.style.width = ((element.offsetWidth - 2) - delta) + "px";
                element.style.right = ((element.offsetLeft + (element.offsetWidth - 2)) - delta) + "px";
                this.dispatchEvent(new CustomEvent("windowResize", {detail: this}));
            }

            let closeDragElement = () => {
                document.onmouseup = null;
                document.onmousemove = null;
                this.dispatchEvent(new CustomEvent("windowEndResize", {detail: this}));
            }

            let dragMouseDown = (event) => {
                if (this.#resizable) {
                    event.preventDefault();
                    pointer = event.clientX;
                    document.onmouseup = closeDragElement;
                    document.onmousemove = elementDrag;
                    this.dispatchEvent(new CustomEvent("windowStartResize", {detail: this}));
                }
            }

            return dragMouseDown;
        }

        let nwResize = (element) => {
            let deltaY = element.offsetTop, pointerY = element.offsetTop;
            let deltaX = element.offsetLeft, pointerX = element.offsetLeft;

            let elementDrag = (event) => {
                event.preventDefault();
                deltaY = pointerY - event.clientY;
                pointerY = event.clientY;
                element.style.height = ((element.offsetHeight - 2) - deltaY) + "px";
                element.style.bottom = ((element.offsetTop + (element.offsetHeight - 2)) - deltaY) + "px";

                deltaX = pointerX - event.clientX;
                pointerX = event.clientX;
                element.style.width = ((element.offsetWidth - 2) - deltaX) + "px";
                element.style.right = ((element.offsetLeft + (element.offsetWidth - 2)) - deltaX) + "px";
                this.dispatchEvent(new CustomEvent("windowResize", {detail: this}));
            }

            let closeDragElement = () => {
                document.onmouseup = null;
                document.onmousemove = null;
                this.dispatchEvent(new CustomEvent("windowEndResize", {detail: this}));
            }

            let dragMouseDown = (event) => {
                if (this.#resizable) {
                    event.preventDefault();
                    pointerY = event.clientY;
                    pointerX = event.clientX;
                    document.onmouseup = closeDragElement;
                    document.onmousemove = elementDrag;
                    this.dispatchEvent(new CustomEvent("windowStartResize", {detail: this}));
                }
            }

            return dragMouseDown;
        }

        let nResizeBottom = (element) => {
            let delta = element.offsetTop, pointer = element.offsetTop;

            let elementDrag = (event) => {
                event.preventDefault();
                delta = pointer - event.clientY;
                pointer = event.clientY;
                element.style.height = ((element.offsetHeight - 2) - delta) + "px";
                element.style.bottom = ((element.offsetTop + (element.offsetHeight - 2)) - delta) + "px";
                this.dispatchEvent(new CustomEvent("windowResize", {detail: this}));
            }

            let closeDragElement = () => {
                document.onmouseup = null;
                document.onmousemove = null;
                this.dispatchEvent(new CustomEvent("windowEndResize", {detail: this}));
            }

            return (event) => {
                if (this.#resizable) {
                    event.preventDefault();
                    pointer = event.clientY;
                    document.onmouseup = closeDragElement;
                    document.onmousemove = elementDrag;
                    this.dispatchEvent(new CustomEvent("windowStartResize", {detail: this}));
                }
            };
        }

        let dragMouseDown = dragElement(this);

        let nResizeTopMouseDown = nResizeTop(this);
        let seResizeMouseDown = seResize(this);
        let eResizeLeftMouseDown = eResizeLeft(this);
        let neResizeMouseDown = neResize(this);
        let swResizeMouseDown = swResize(this);
        let eResizeRightMouseDown = eResizeRight(this);
        let nwResizeMouseDown = nwResize(this);
        let nResizeBottomMouseDown = nResizeBottom(this);

        builder(this, [
            {
                element: "div",
                style: {
                    height: "20px",
                    margin: "-10px",
                    cursor: "n-resize"
                },
                onMousedown: nResizeTopMouseDown
            }, {
                element: "div",
                style: {
                    display: "flex",
                    height: "100%"
                },
                children: [
                    {
                        element: "div",
                        style: {
                            width: "20px",
                            margin: "-10px",
                        },
                        children: [
                            {
                                element: "div",
                                style: {
                                    width: "20px",
                                    height: "20px",
                                    cursor: "se-resize"
                                },
                                onMousedown: seResizeMouseDown
                            },
                            {
                                element: "div",
                                style: {
                                    width: "20px",
                                    height: "calc(100% - 40px)",
                                    cursor: "e-resize"
                                },
                                onMousedown: eResizeLeftMouseDown
                            },
                            {
                                element: "div",
                                style: {
                                    width: "20px",
                                    height: "20px",
                                    cursor: "ne-resize"
                                },
                                onMousedown: neResizeMouseDown
                            }
                        ]
                    },
                    {
                        element: "div",
                        style: {
                            flex: "1",
                            alignItems: "stretch"
                        },
                        children: [
                            {
                                element: "div",
                                style: {
                                    lineHeight: "24px",
                                    backgroundColor: "var(--main-background-color)",
                                    paddingLeft: "3px",
                                    cursor: "all-scroll",
                                    display : "flex"
                                },
                                onMousedown: dragMouseDown,
                                children : [
                                    {
                                        element : "div",
                                        text: this.#header
                                    }, {
                                        element: "div",
                                        style : {
                                            flex : "1"
                                        }
                                    }, {
                                        element: "div",
                                        style : {
                                            marginBottom : "-7px"
                                        },
                                        children : [
                                            {
                                                element : "button",
                                                type : "button",
                                                text : "minimize",
                                                className : "material-icons",
                                                onClick : (event) => {
                                                    event.stopPropagation();
                                                    windowManager.minimize(this);
                                                    return false;
                                                }
                                            },
                                            {
                                                element : "button",
                                                type : "button",
                                                text : "close",
                                                className : "material-icons",
                                                onClick : (event) => {
                                                    event.stopPropagation();
                                                    windowManager.close(this);
                                                    return false;
                                                }
                                            }
                                        ]
                                    }
                                ]
                            }, {
                                element: "div",
                                style: {
                                    overflow: "auto",
                                    padding: "20px",
                                    height: "calc(100% - 64px)"
                                },
                                initialize: (element) => {
                                    element.appendChild(this.#content);
                                }
                            }]
                    },
                    {
                        element: "div",
                        style: {
                            width: "20px",
                            margin: "-10px"
                        },
                        children: [
                            {
                                element: "div",
                                style: {
                                    width: "20px",
                                    height: "20px",
                                    cursor: "sw-resize"
                                },
                                onMousedown: swResizeMouseDown
                            },
                            {
                                element: "div",
                                style: {
                                    width: "20px",
                                    height: "calc(100% - 40px)",
                                    cursor: "e-resize"
                                },
                                onMousedown: eResizeRightMouseDown
                            },
                            {
                                element: "div",
                                style: {
                                    width: "20px",
                                    height: "20px",
                                    cursor: "nw-resize"
                                },
                                onMousedown: nwResizeMouseDown
                            }
                        ]
                    }
                ]
            }, {
                element: "div",
                style: {
                    height: "20px",
                    margin: "-10px",
                    cursor: "n-resize"
                },
                onMousedown: nResizeBottomMouseDown
            }
        ])

    }

}

customComponents.define("mat-window", MatWindow);
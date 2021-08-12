import MatInfiniteScrollPart from "./mat-infinite-scroll-part.js";
import {customComponents} from "../../simplicity.js";

export default class MatInfiniteScroll extends HTMLElement {

    #chunkSize = 5;
    #items;
    #meta;

    get items() {
        return this.#items;
    }

    set items(value) {
        this.#items = value;
    }

    get meta() {
        return this.#meta;
    }

    set meta(value) {
        this.#meta = value;
    }

    reload() {
        for (const child of this.children) {
            child.reload();
        }
    }

    render() {
        let router = this.queryUpwards((element) => element.localName === "dom-window")
        router.addEventListener("scroll", (event) => {
            let scrollPosition = event.target.scrollY

            if (scrollPosition < 0.10) {
                if (this.firstElementChild.index !== 0) {
                    this.lastElementChild.remove();
                    let timelinePart = new MatInfiniteScrollPart();
                    let firstComponent = this.firstElementChild;
                    timelinePart.index = firstComponent.index - this.#chunkSize;
                    timelinePart.limit = this.#chunkSize;
                    timelinePart.items = this.#items;
                    timelinePart.meta = this.#meta;
                    this.insertBefore(timelinePart, this.firstElementChild);
                }
            }


            if (scrollPosition > 0.90) {
                if (this.lastElementChild.data.length === this.#chunkSize) {
                    this.firstElementChild.remove();
                    let timelinePart = new MatInfiniteScrollPart();
                    let lastComponent = this.lastElementChild;
                    timelinePart.index = lastComponent.index + this.#chunkSize;
                    timelinePart.limit = this.#chunkSize;
                    timelinePart.items = this.#items;
                    timelinePart.meta = this.#meta;
                    this.appendChild(timelinePart);
                }
            }
        })

        let part = new MatInfiniteScrollPart();
        part.meta = this.#meta;
        part.index = 0;
        part.limit = this.#chunkSize;
        part.items = this.#items;
        part.addEventListener("items", () => {
            this.dispatchEvent(new CustomEvent("items"))
        })
        part.addEventListener("loadend", (event) => {
            if (this.#chunkSize === event.detail.data.length) {
                let part = new MatInfiniteScrollPart();
                part.meta = this.#meta;
                part.index = this.#chunkSize;
                part.limit = this.#chunkSize;
                part.items = this.#items;
                part.addEventListener("items", () => {
                    this.dispatchEvent(new CustomEvent("items"))
                })
                part.addEventListener("loadend", (event) => {
                    if (this.#chunkSize === event.detail.data.length) {
                        let part = new MatInfiniteScrollPart();
                        part.meta = this.#meta;
                        part.index = this.#chunkSize * 2;
                        part.limit = this.#chunkSize;
                        part.items = this.#items;
                        part.addEventListener("items", () => {
                            this.dispatchEvent(new CustomEvent("items"))
                        })
                        this.appendChild(part);
                        this.dispatchEvent(new CustomEvent("items"));
                    }
                })
                this.appendChild(part);
                this.dispatchEvent(new CustomEvent("items"));
            }
        })
        this.appendChild(part);
        this.dispatchEvent(new CustomEvent("items"));
    }

}

customComponents.define("mat-infinite-scroll", MatInfiniteScroll)
import {customComponents} from "../../../../simplicity";

export const name = "editor-flexbox";

export default class EditorDivFlexElement extends HTMLDivElement {

    connectedCallback() {
        let element = this;

        document.addEventListener("keydown", (event) => {
            let selection = document.getSelection();
            let rangeAt = selection.getRangeAt(0);
            let endOffset = rangeAt.endOffset;
            let endContainer = rangeAt.endContainer;

            let divs = element.querySelectorAll("div");
            let parentElement = endContainer.parentElement;
            if (parentElement && parentElement.parentElement === element) {
                if (divs.item(divs.length - 1).isEqualNode(parentElement)) {
                    let length = endContainer.textContent.length;

                    if (length === endOffset) {

                        if (event.code === "Enter") {
                            event.preventDefault();

                            let newColumn = document.createElement("div");
                            newColumn.style.flex = "1"
                            element.appendChild(newColumn);

                            return false;
                        }

                        if (event.code === "ArrowRight") {
                            let newChild = document.createElement("div");
                            newChild.className = "root"
                            element.insertAdjacentElement("afterend", newChild)
                        }
                    }
                }
            }
        });
    }

}

customComponents.define(name, EditorDivFlexElement, {extends: "div"})

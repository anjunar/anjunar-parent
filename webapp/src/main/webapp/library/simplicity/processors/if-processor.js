export default function ifProcessorFactory(property, leaf, value) {
    return new class IfProcessor {

        test() {
            return property === "if"
        }

        initialize(element) {
            let templateElement = document.createElement("template");
            element.insertAdjacentElement("beforebegin", templateElement)
            templateElement.content.appendChild(element);
            leaf.template = templateElement;
        }

        update(element) {
            let state = leaf.if();
            let elementChild = leaf.template.content.firstElementChild;

            if (state) {
                if (elementChild) {
                    leaf.template.insertAdjacentElement("afterend", elementChild);
                }
            } else {
                if (!elementChild) {
                    leaf.template.content.appendChild(element);
                }
            }
        }

    }
}

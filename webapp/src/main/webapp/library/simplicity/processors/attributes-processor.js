export default function attributesProcessorFactory(property, leaf, value) {
    return new class AttributesProcessor {

        test() {
            return property === "attributes"
        }

        initialize(element) {
            this.update(element);

            for (const name of Object.keys(value)) {
                let valueElement = value[name];
                if (! (valueElement instanceof Function)) {
                    element.setAttribute(name, valueElement);
                }
            }
        }

        update(element) {
            for (const attribute of Object.keys(value)) {
                let currentAttribute = value[attribute];
                if (currentAttribute instanceof Function) {
                    if (attribute === "disabled") {
                        let state = currentAttribute();
                        if (state) {
                            element.setAttribute("disabled", "true")
                        } else {
                            element.removeAttribute("disabled")
                        }
                    } else {
                        element.setAttribute(attribute, currentAttribute());
                    }
                }
            }
        }

    }
}

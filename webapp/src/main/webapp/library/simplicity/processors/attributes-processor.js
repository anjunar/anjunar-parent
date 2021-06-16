export default function attributesProcessorFactory(property, leaf, value) {
    return new class AttributesProcessor {

        test() {
            return property === "attributes"
        }

        initialize(element) {
            for (const name of Object.keys(value)) {
                element.setAttribute(name, value[name]);
            }
        }

        update(element) {
            for (const attribute of Object.keys(value)) {
                let currentAttribute = value[attribute];
                if (currentAttribute instanceof Function) {
                    if (attribute === "disabled") {
                        let state = currentAttribute();
                        if (JSON.parse(state)) {
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

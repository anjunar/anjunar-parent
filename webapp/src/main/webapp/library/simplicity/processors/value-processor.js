export default function valueProcessorFactory(property, leaf, value) {
    return new class ValueProcessor {

        test() {
            return property === "value"
        }

        initialize(element) {
            this.update(element);

            if (element.type === "checkbox") {

                let handler = () => {
                    value.output.call(element, element.checked);
                }

                element.addEventListener("click", handler)
            } else {

                let handler = () => {
                    value.output.call(element, element.value);
                }

                element.addEventListener("change", handler)
                element.addEventListener("keyup", handler)
            }
        }

        update(element) {
            if (element.type === "radio") {
                let value = value.input.call(element);
                element.value = element.use
                if (value === element.value) {
                    element.checked = true;
                }
            } else {
                if (element.type === "checkbox") {
                    element.checked = value.input.call(element);
                } else {
                    if (value.input instanceof Function) {
                        element.value = value.input.call(element);
                    } else {
                        element.value = value;
                    }
                }
            }
        }

    }
}

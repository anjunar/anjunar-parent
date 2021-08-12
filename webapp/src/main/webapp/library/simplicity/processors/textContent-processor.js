export default function textContentProcessorFactory(property, leaf, value) {
    return new class TextContentProcessor {

        test(element) {
            return property === "text" && ! Object.getPrototypeOf(element).hasOwnProperty("text")
        }

        initialize(element) {
            if (value instanceof Function) {
                element.textContent = value.call(element);
            } else {
                element.textContent = value;
            }
        }

        update(element) {
            if (value instanceof Function) {
                element.textContent = value.call(element);
            }
        }

    }
}

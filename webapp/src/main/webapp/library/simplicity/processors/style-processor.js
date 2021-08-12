export default function styleProcessorFactory(property, leaf, value) {
    return new class StyleProcessor {

        test() {
            return property === "style"
        }

        initialize(element) {
            for (const style of Object.keys(value)) {
                let currentStyle = value[style];
                if (currentStyle instanceof Function) {
                    element.style[style] = currentStyle.call(element);
                } else {
                    element.style[style] = currentStyle;
                }
            }
        }

        update(element) {
            for (const style of Object.keys(value)) {
                let currentStyle = value[style];
                if (currentStyle instanceof Function) {
                    element.style[style] = currentStyle.call(element);
                }
            }
        }
    }
}

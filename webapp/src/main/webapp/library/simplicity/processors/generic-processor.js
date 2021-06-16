export default function genericProcessorFactory(property, leaf, value) {
    return new class GenericProcessor {

        test() {
            if (value instanceof Array) {
                if (value.length > 0 && value[0].element) {
                    return false;
                }
            }
            return property !== "element" && property !== "children" && ! value?.element && property !== "postInitialize"
        }

        initialize(element) {
            if (value instanceof Function) {
                element[property] = value.call(element);
            } else {
                element[property] = value;
            }
        }

        update(element) {
            if (value instanceof Function) {
                element[property] = value.call(element);
            }
        }

    }
}

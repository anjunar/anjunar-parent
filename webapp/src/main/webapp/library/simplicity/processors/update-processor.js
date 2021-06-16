export default function updateProcessorFactory(property, leaf, value) {
    return new class UpdateProcessor {

        test() {
            return property === "update"
        }

        initialize(element) {
            value.call(element, element);
        }

        update(element) {
            value.call(element, element);
        }

    }
}


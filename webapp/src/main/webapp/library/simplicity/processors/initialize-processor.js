export default function initializeProcessorFactory(property, leaf, value) {
    return new class InitializeProcessor {

        test() {
            return property === "initialize"
        }

        initialize(element) {
            value.call(element, element);
        }

    }
}

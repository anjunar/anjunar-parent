export default function directProcessorFactory(property, leaf, value) {
    return new class DirectProcessor {

        test() {
            return value?.direct;
        }

        initialize(element) {
            element[property] = value.direct;
        }

    }
}

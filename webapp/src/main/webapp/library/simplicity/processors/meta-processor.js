export default function metaProcessorFactory(property, leaf, value) {
    return new class MetaProcessor {

        test() {
            return property === "meta"
        }

        initialize(element) {
            this.update(element);
        }

        update(element) {
            element[property] = value;
        }

    }
}

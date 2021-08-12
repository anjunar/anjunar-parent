export default function inputProcessorFactory(property, leaf, value) {
    return new class ValueProcessor {

        test() {
            return value?.input
        }

        initialize(element) {
            this.update(element);
        }

        update(element) {
            element[property] = value.input();
        }

    }
}

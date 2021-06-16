export default function eventProcessorFactory(property, leaf, value) {
    return new class EventProcessor {

        test() {
            return property.startsWith("on");
        }

        initialize(element) {
            let eventName = property.substring(2,3).toLowerCase() + property.substring(3);
            element.addEventListener(eventName, (event) => {
                value.call(element, event);
            });
        }

    }
}

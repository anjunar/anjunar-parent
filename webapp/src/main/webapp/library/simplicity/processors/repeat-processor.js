import {builder} from "../simplicity.js";

const renderedItems = new WeakMap();

export default function repeatProcessorFactory(property, leaf, value) {
    return new class RepeatProcessor {

        test() {
            return value?.item && value?.items
        }

        update(element) {
            let force = false;
            let items = value.items();
            if (items) {
                let newVar = renderedItems.get(value.parentElement());
                if (value.type === "json") {
                    if ((JSON.stringify(newVar) !== JSON.stringify(items)) || force) {
                        for (const child of Array.from(value.parentElement().children)) {
                            child.remove();
                        }
                        items.forEach((item, index, array) => {
                            let tree = value.item(item, index, array);
                            builder(value.parentElement(), tree);
                        })
                        renderedItems.set(value.parentElement(), items);
                    }
                } else {
                    if ((newVar !== items) || force) {
                        for (const child of Array.from(value.parentElement().children)) {
                            child.remove();
                        }
                        items.forEach((item, index, array) => {
                            let tree = value.item(item, index, array);
                            builder(value.parentElement(), tree);
                        })
                        renderedItems.set(value.parentElement(), items);
                    }
                }
            }
        }

    }
}

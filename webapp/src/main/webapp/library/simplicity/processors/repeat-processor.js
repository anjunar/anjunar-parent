import {builder} from "../simplicity.js";

const renderedItems = new WeakMap();

function isEqual(lhs, rhs) {
    if (lhs && rhs && lhs.length === rhs.length) {
        for (let i = 0; i < lhs.length; i++) {
            const lh = lhs[i];
            const rh = rhs[i];
            if (lh !== rh) {
                return false;
            }
        }
        return true;
    }
    return false;
}

export default function repeatProcessorFactory(property, leaf, value) {
    return new class RepeatProcessor {

        test() {
            return value?.item && value?.items
        }

        initialize(element) {
            this.update(element)
        }

        update(element, force) {
            let items = value.items();
            if (items) {
                let newVar = renderedItems.get(value.parentElement());

                if (! isEqual(newVar,items) || force) {
                    for (const child of Array.from(value.parentElement().children)) {
                        child.remove();
                    }
                    items.forEach((item, index, array) => {
                        let tree = value.item(item, index, array);
                        builder(value.parentElement(), tree);
                    })
                    renderedItems.set(value.parentElement(), Array.from(items));
                    element.dispatchEvent(new CustomEvent("items"))
                }
            }
        }

    }
}

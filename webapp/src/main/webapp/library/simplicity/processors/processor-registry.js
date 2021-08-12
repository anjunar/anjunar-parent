import attributesProcessorFactory from "./attributes-processor.js";
import directProcessorFactory from "./direct-processor.js";
import eventProcessorFactory from "./event-processor.js";
import genericProcessorFactory from "./generic-processor.js";
import ifProcessorFactory from "./if-processor.js";
import initializeProcessorFactory from "./initialize-processor.js";
import repeatProcessorFactory from "./repeat-processor.js";
import styleProcessorFactory from "./style-processor.js";
import textContentProcessorFactory from "./textContent-processor.js";
import updateProcessorFactory from "./update-processor.js";
import valueProcessorFactory from "./value-processor.js";
import inputProcessorFactory from "./input-processor.js";

export default function registry(property, leaf, value) {
    return [
        valueProcessorFactory(property, leaf, value),
        attributesProcessorFactory(property, leaf, value),
        directProcessorFactory(property, leaf, value),
        eventProcessorFactory(property, leaf, value),
        ifProcessorFactory(property, leaf, value),
        repeatProcessorFactory(property, leaf, value),
        initializeProcessorFactory(property, leaf, value),
        inputProcessorFactory(property, leaf, value),
        styleProcessorFactory(property, leaf, value),
        textContentProcessorFactory(property, leaf, value),
        updateProcessorFactory(property, leaf, value),

        genericProcessorFactory(property, leaf, value)
    ]
}
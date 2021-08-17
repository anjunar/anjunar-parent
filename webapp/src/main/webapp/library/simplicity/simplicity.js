import {register} from "./services/router.js"
import {lifeCycle} from "./processors/lifecycle-processor.js";
import registry from "./processors/processor-registry.js";

export class HTMLWindow extends HTMLElement {

    #window

    get window() {
        return this.#window;
    }

    set window(value) {
        this.#window = value;
    }
}

export class HTMLWindowLazy extends HTMLElement {

    #window

    get window() {
        return this.#window;
    }

    set window(value) {
        this.#window = value;
    }

}

function traverseAndNotify(element) {
    element.dispatchEvent(new CustomEvent("rendered"))
    let iterator = document.createNodeIterator(element, NodeFilter.SHOW_ELEMENT)

    let currentNode;
    while (currentNode = iterator.nextNode()) {
        currentNode.dispatchEvent(new CustomEvent("rendered"));
    }
}

document.reloadAll = () => {
    let traversal = document.createNodeIterator(document.body, NodeFilter.SHOW_ELEMENT);

    let currentNode;
    while (currentNode = traversal.nextNode()) {
        if (currentNode.reload) {
            currentNode.reload();
        }
    }
}

Node.prototype.queryUpwards = function (callback) {
    if (callback(this)) {
        return this;
    } else {
        if (this.parentElement === null) {
            return null;
        }
        return this.parentElement.queryUpwards(callback);
    }
}

const blackList = ["mousemove", "mouseover", ""]

EventTarget.prototype.addEventListener = (function (_super) {
    return function (name, callback) {
        _super.apply(this, [name, (event) => {
            callback(event)
            if (blackList.indexOf(name) === -1) {
                window.setTimeout(() => {
                    lifeCycle();
                }, 10)
            }
        }])
    }
})(EventTarget.prototype.addEventListener);

Node.prototype.appendChild = (function (_super) {
    return function (child) {
        _super.apply(this, [child])
        if (child.isConnected) {
            traverseAndNotify(child);
            lifeCycle();
        }
    }
})(Node.prototype.appendChild);

HTMLElement.prototype.insertAdjacentElement = (function (_super) {
    return function (position, element) {
        _super.apply(this, [position, element]);
        if (element.isConnected) {
            traverseAndNotify(element);
            lifeCycle();
        }
    }
})(HTMLElement.prototype.insertAdjacentElement);

HTMLElement.prototype.insertBefore = (function (_super) {
    return function (newChild, refChild) {
        _super.apply(this, [newChild, refChild])
        if (newChild.isConnected) {
            traverseAndNotify(newChild);
            lifeCycle();
        }
    }
})(HTMLElement.prototype.insertBefore);

function elementFactory(property, array, tree) {

    let properties = []
    let processors = [];
    let instance;

    return new class Element {

        constructor() {
            if (property !== "meta") {
                let value = tree.element;
                if (value instanceof HTMLElement) {
                    instance = value;
                } else {
                    if (value instanceof Function) {
                        instance = new value();
                    } else {
                        if (htmlTags.indexOf(value) === -1) {
                            instance = document.createElementNS("http://www.w3.org/2000/svg", value);
                        } else {
                            instance = document.createElement(value);
                        }
                    }
                }
            }
        }

        connectElements(parent) {
            if (parent) {
                if (property === "children") {
                    parent.instance.appendChild(instance)
                } else {
                    if (array) {
                        parent.instance[property].push(instance);
                    } else {
                        parent.instance[property] = instance;
                    }
                }
            }

            for (const element of properties) {
                element.connectElements(this);
            }
        }

        initialize() {
            for (const filteredProcessor of processors) {
                if (filteredProcessor.initialize) {
                    filteredProcessor.initialize(instance)
                }
            }

            for (const element of properties) {
                element.initialize();
            }
        }

        postInitialize() {
            if (this.postInitializes) {
                this.postInitializes(instance);
            }

            for (const element of properties) {
                element.postInitialize();
            }
        }

        update(force) {
            for (const processor of processors) {
                if (processor.update) {
                    processor.update(instance, force);
                }
            }

            for (const element of properties) {
                element.update(force);
            }
        }

        addProperty(value) {
            properties.push(value);
        }

        get properties() {
            return properties;
        }

        get processors() {
            return processors;
        }

        get property() {
            return property;
        }

        get instance() {
            return instance;
        }

        get isArray() {
            return array;
        }

    }
}

function visitTree(tree, array, property) {

    let element = elementFactory(property, array, tree);

    for (const property of Object.keys(tree)) {
        let leaf = tree[property];

        for (const part of registry(property, tree, leaf)) {
            if (part.test(element.instance)) {
                element.processors.push(part);
                break;
            }
        }

        if (property === "postInitialize") {
            element.postInitializes = leaf;
        }

        if (leaf) {
            if (leaf.items && leaf.item) {
                leaf.parentElement = () => element.instance;
            }

            if (leaf instanceof Array) {
                for (const child of leaf) {
                    if (child.element) {
                        element.addProperty(visitTree(child, true, property));
                    }
                }
            }
            if (leaf.element) {
                element.addProperty(visitTree(leaf, false, property));
            }
        }
    }

    return element;
}

const htmlTags = ["a", "abbr", "address", "area", "article", "aside", "audio", "b", "base", "bdi", "bdo", "blockquote",
    "body", "br", "button", "canvas", "caption", "cite", "code", "col", "colgroup", "data", "datalist", "dd", "del", "details",
    "dfn", "dialog", "div", "dl", "dt", "em", "embed", "fieldset", "figcaption", "figure", "footer", "form", "h1", "h2", "h3", "h4",
    "h5", "h6", "head", "header", "hgroup", "hr", "html", "i", "iframe", "img", "input", "ins", "kbd", "label", "legend", "li", "link",
    "main", "map", "mark", "menu", "meta", "meter", "nav", "noscript", "object", "ol", "optgroup", "option", "output", "p", "param",
    "picture", "pre", "progress", "q", "rp", "rt", "ruby", "s", "samp", "script", "section", "select", "slot", "small", "source",
    "span", "strong", "style", "sub", "summary", "sup", "table", "tbody", "td", "template", "textarea", "tfoot", "th", "thead", "time",
    "title", "tr", "track", "u", "ul", "var", "video", "wbr"];

export const metaCache = new Set;

export function meta(tree) {
    metaCache.add([visitTree(tree)]);
}

export function builder(scope, tree) {

    let treeArray
    if (!(tree instanceof Array)) {
        treeArray = [tree];
    } else {
        treeArray = tree;
    }

    let container = document.createDocumentFragment();

    let result = [];
    for (const tree of treeArray) {
        let element = visitTree(tree, false);
        result.push(element);

        element.connectElements(null);
        element.initialize();
        container.appendChild(element.instance);
    }

    metaCache.add(result);

    if (scope) {
        scope.appendChild(container);
    }

    for (const element of result) {
        element.postInitialize();
    }

    return container;
}

export const customComponents = new class CustomComponents {
    define(name, clazz, options) {

        clazz.prototype.connectedCallback = function () {
            if (! this.rendered) {
                if (this.render) {
                    this.render();
                    this.rendered = true;
                }
            }
        }

        customElements.define(name, clazz, options);
        return clazz;
    }
}

export const customViews = new class CustomViews {
    define(configure) {

        register(configure.name, configure)

        configure.class.prototype.connectedCallback = function () {
            if (! this.rendered) {
                if (this.render) {
                    this.render();
                    this.rendered = true;
                }
            }
        }

        customElements.define(configure.name, configure.class)

        return configure.class;
    }
}
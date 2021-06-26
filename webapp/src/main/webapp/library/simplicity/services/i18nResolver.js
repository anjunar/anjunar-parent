import {language, loadRoot} from "../../../anjunar/service.js";

export const resolver = new class I18nResolver {
    get language() {
        let root = loadRoot();
        return root.language.replace("_", "-")
    }

    set language(value) {
        language(value);
    }
}

export function i18nFactory(language) {
    return function (value) {
        return language[value][resolver.language || "en-DE"];
    }
}
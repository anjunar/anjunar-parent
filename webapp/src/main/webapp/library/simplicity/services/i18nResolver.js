import {language, loadRoot} from "../../../anjunar/service.js";

export function getLanguage() {
    let root = loadRoot();
    return root.user.language.replace("_", "-")
}

export function setLanguage(value) {
    language(value);
}

export function i18nFactory(language) {
    return function (value) {
        return language[value][getLanguage() || "en-DE"];
    }
}
export function i18nFactory(language) {
    return function (value) {
        return language[value][user.language || "en-DE"];
    }
}
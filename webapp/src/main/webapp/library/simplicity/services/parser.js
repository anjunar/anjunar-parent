import { between, char, choice, sepBy, optionalWhitespace, endOfInput, many, recursiveParser, possibly, sequenceOf, regex} from "../../arcsecond/index.js";

function jsonExpressionFactory() {
    return regex(/^({.*})/g).map((result => {
        return {type: 'JSONExpression', value: result}
    }));
}

function jsonArrayExpressionFactory() {
    return regex(/^(\[{.*}\])/g).map((result => {
        return {type: 'JSONExpression', value: result}
    }));
}

function interpolationExpressionFactory() {
    return regex(/^`.*\${([\w.]+)}.*`$/g).map((result => {
        const regex = /^`.*\${([\w.]+)}.*`$/g
        let exec = regex.exec(result);
        return {type: 'InterpolationExpression', value: exec[1]}
    }));
}


function reservedExpressionFactory() {
    return regex(/^(true|false|class|undefined|\$event)/g).map((result => {
        return {type: 'ReservedExpression', value: result}
    }));
}

function numberExpressionFactory() {
    return regex(/^(\d+)/g).map((result => {
        return {type: 'NumberExpression', value: result}
    }));
}

function literalExpressionFactory() {
    return regex(/^('[^']*')/g).map((result => {
        return {type: 'LiteralExpression', value: result}
    }));
}

function memberExpressionFactory() {
    return regex(/^([\w\d\.]+)/g).map((result => {
        return {type: 'MemberExpression', value: result}
    }));
}

function objectExpressionFactory() {
    const value = recursiveParser(() => choice([
        memberExpression,
        numberExpression,
        literalExpression,
        matchArray,
    ]));

    const betweenSquareBrackets = between(char('['))(char(']'));
    const objects = many(value);
    const matchArray = betweenSquareBrackets(objects);

    const sequence = sequenceOf([memberExpression, matchArray, objects])

    return sequence.map(result => {
        return {type: "ObjectExpression", value: result}
    });
}

function callExpressionFactory() {
    const surroundedBy = parser => between(parser)(parser);
    const betweenSquareBrackets = between(char('('))(char(')'));
    const commaSeparated = sepBy(surroundedBy(optionalWhitespace)(char(',')));
    const listElement = recursiveParser(() => choice([
        argumentList,
        ...expressionsDerived
    ]));
    const argumentList = betweenSquareBrackets(commaSeparated(listElement)).map(values => ({
        type: 'Arguments',
        value: values
    }));

    return sequenceOf([memberExpression, argumentList]).map(result => {
        return {type: "CallExpression", value: {value: result[0], args: result[1]}}
    });
}

function equationExpressionFactory() {
    const value = recursiveParser(() => choice([
        objectExpression,
        reservedExpression,
        numberExpression,
        literalExpression,
        interpolationExpression,
        memberExpression,
        jsonExpression,
        jsonArrayExpression,
        regex(/^([\*+-\:/=<>&|]+)/g).map(result => {
            return {type: "Operator", value: result}
        }),
        matchArray,
    ]));

    const surroundedBy = parser => between(parser)(parser);
    const betweenSquareBrackets = between(char('('))(char(')'));
    const objects = many(surroundedBy(optionalWhitespace)(value));

    const matchArray = betweenSquareBrackets(objects);

    return objects.map(result => {
        return {type: "EquationExpression", value: result}
    });
}

function ifThenElseExpressionFactory() {
    return sequenceOf([optionalWhitespace, char("?"), optionalWhitespace, choice(expressionsDerived), optionalWhitespace, char(":"), optionalWhitespace, choice(expressionsDerived)]).map(result => {
        return {first: result[3], second: result[[7]]}
    });
}

const jsonExpression = jsonExpressionFactory();
const jsonArrayExpression = jsonArrayExpressionFactory();
const reservedExpression = reservedExpressionFactory();
const numberExpression = numberExpressionFactory();
const literalExpression = literalExpressionFactory();
const memberExpression = memberExpressionFactory();
const objectExpression = objectExpressionFactory();
const interpolationExpression = interpolationExpressionFactory();

const expressionsDerived = [reservedExpression, numberExpression, literalExpression, memberExpression];

const equationExpression = equationExpressionFactory();
const ifThenElse = ifThenElseExpressionFactory();
const callExpression = callExpressionFactory();


export const parser = sequenceOf([
    possibly(regex(/^(!)\s*/g)),
    choice([
        callExpression,
        equationExpression,
    ]),
    possibly(ifThenElse),
    endOfInput
]).map(result => result[1])


// These parsers are based heavily on the work documented at
// https://blog.jcoglan.com/2017/07/07/precedence-and-associativity-in-recursive-descent/

// Utility method to print out the entire array of parsed expressions to the console
/*
const whitespaceSurrounded = parser =>
    between(optionalWhitespace)(optionalWhitespace)(parser);

const betweenParentheses = parser =>
    between(whitespaceSurrounded(char('(')))(whitespaceSurrounded(char(')')))(parser);

const plus = char('+');
const minus = char('-');
const times = char('*');
const divide = char('/');

const binaryExpression = op => parser =>
    sequenceOf([
        whitespaceSurrounded(parser),
        many1(sequenceOf([whitespaceSurrounded(op), whitespaceSurrounded(parser)]))
    ])
        .map(([initialTerm, expressions]) =>
            // Flatten the expressions
            [initialTerm, ...expressions].reduce((acc, curr) =>
                // Reduce the array into a left-recursive tree
                Array.isArray(curr) ? [curr[0], acc, curr[1]] : curr
            )
        );

const factor = recursiveParser(() => choice([digits, betweenParentheses(expression)]));
const term = recursiveParser(() => choice([multiplicationOrDivision, factor]));
const expression = recursiveParser(() => choice([additionOrSubtraction, term]));
const additionOrSubtraction = binaryExpression(choice([plus, minus]))(term);
const multiplicationOrDivision = binaryExpression(choice([times, divide]))(factor);

// Handle precedence of multiplication and division over addition and subtraction
console.log(many(expression).run('9 + 5 - 4 * 4 / 3').result);
// Result: [ [ '-', [ '+', '9', '5' ], [ '/', [ '*', '4', '4' ], '3' ] ] ]

// Handle precedence of expressions in parentheses
console.log(many(expression).run('9 + (5 - 4) * (4 / 3)').result)
// Result: [ [ '+', '9', [ '*', [ '-', '5', '4' ], [ '/', '4', '3' ] ] ] ]*/

